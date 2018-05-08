package nl.wolfpackit.wolfpack.modules.declarations.add;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import nl.wolfpackit.wolfpack.ImageTools;
import nl.wolfpackit.wolfpack.R;
import nl.wolfpackit.wolfpack.database.Database;
import nl.wolfpackit.wolfpack.database.Declaration;
import nl.wolfpackit.wolfpack.database.Receipt;
import nl.wolfpackit.wolfpack.database.TravelDeclaration;
import nl.wolfpackit.wolfpack.modules.declarations.DeclarationAcceptedListFragment;
import nl.wolfpackit.wolfpack.modules.declarations.DeclarationSubmittedListFragment;

public class DeclarationAddActivity extends AppCompatActivity {
    Declaration declaration = new Declaration();
    DeclarationAddGeneralFragment generalFrag;
    DeclarationAddDistanceFragment distanceFrag;
    Receipt receipt;
    ViewPager viewPager;
    TabLayout tabs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declaration_add);

        declaration.setName(getIntent().getStringExtra("name"));
        Log.w("TEST", getIntent().getStringExtra("name")+"");

        //set up tabs
        viewPager = findViewById(R.id.declarationAddViewPager);
        tabs = findViewById(R.id.declarationTabs);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                viewPager.setCurrentItem(index);
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            public int getCount() {
                return 2;
            }

            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        generalFrag = new DeclarationAddGeneralFragment();
                        return generalFrag;
                    case 1:
                        distanceFrag = new DeclarationAddDistanceFragment();
                        return distanceFrag;
                    default:
                        return null;
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                int tabIndex = tabs.getSelectedTabPosition();
                if (tabIndex != position)
                    tabs.getTabAt(position).select();
            }
        });

        //set up total and buttons
        setTotal(0);
        ((ImageButton) findViewById(R.id.declarationCancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                finish();
            }
        });
        ((ImageButton) findViewById(R.id.declarationAccept)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                setResult(Activity.RESULT_OK,
                        new Intent().putExtra("declaration", declaration.exportString()));
                finish();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==DeclarationAddGeneralFragment.fileSelectResult && resultCode==Activity.RESULT_OK){
            if(data!=null){
                final Uri uri = data.getData();

                //read the file as base 64, and send to fragment when finished
                (new AsyncTask<Void, Void, Void>(){
                    protected Void doInBackground(Void... voids){
                        try {
                            InputStream iStream = getContentResolver().openInputStream(uri);
                            byte[] inputData = getBytes(iStream);
                            String stringData = Base64.encodeToString(inputData, Base64.DEFAULT);
                            if(generalFrag!=null) generalFrag.selectedFile(stringData);
                            if(distanceFrag!=null) distanceFrag.updateReceipt();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).doInBackground();
            }
        }else if(requestCode==DeclarationAddGeneralFragment.imageSelectResult && resultCode==Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String image = ImageTools.getStringFromImage(imageBitmap);

            if(generalFrag!=null) generalFrag.selectedImage(image);
            if(distanceFrag!=null) distanceFrag.updateReceipt();
        }
    }
    public byte[] getBytes(InputStream inputStream) throws IOException{
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while((len=inputStream.read(buffer))!=-1){
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    protected Declaration getDeclaration(){
        return declaration;
    }

    //receipt methods
    protected Receipt createReceipt(){
        receipt = new Receipt();
        declaration.setReceipt(receipt);
        return receipt;
    }
    protected void removeReceipt(){
        receipt = null;
        declaration.setReceipt(null);
        if(generalFrag!=null) generalFrag.updateReceipt();
        if(distanceFrag!=null) distanceFrag.updateReceipt();
    }
    protected Receipt getReceipt(){
        return receipt;
    }

    //travel declaration methods
    protected void getTravelDeclaration(final Database.QueryResult<TravelDeclaration> result){

        declaration.getTravelDeclaration(new Database.QueryResult<TravelDeclaration>(){
            public void onResult(TravelDeclaration td){
                if(td==null){
                    td = new TravelDeclaration();
                    declaration.setTravelDeclaration(td);
                }
                result.onResult(td);
            }
        });
    }

    //price methods
    protected void setGeneralAmount(float amount){
        declaration.setAmount(amount);
        updateTotal();
    }
    protected void setBTWAmount(float amount){
        declaration.setAmountBTW(amount);
        updateTotal();
    }
    protected void setDistanceAmount(final float distance, final float compensation){
        getTravelDeclaration(new Database.QueryResult<TravelDeclaration>(){
            public void onResult(TravelDeclaration td){
                td.setDistance(distance);
                td.setCompensation(compensation);
                updateTotal();
            }
        });
    }
    protected void updateTotal(){
        declaration.getTravelDeclaration(new Database.QueryResult<TravelDeclaration>() {
            public void onResult(TravelDeclaration td){
                float amount = declaration.getAmountBTW()+declaration.getAmount();
                if(td!=null)
                    amount += td.getCompensation()*td.getDistance();
                setTotal(amount);
            }
        });
    }
    protected void setTotal(float amount){
        amount = Math.round(amount*100)/100f;
        ((TextView)findViewById(R.id.declarationAmount)).setText(String.format("%.2f", amount));
    }
}
