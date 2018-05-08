package nl.wolfpackit.wolfpack.modules;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;

import nl.wolfpackit.wolfpack.ImageTools;
import nl.wolfpackit.wolfpack.R;
import nl.wolfpackit.wolfpack.activities.SigninActivity;
import nl.wolfpackit.wolfpack.database.Account;
import nl.wolfpackit.wolfpack.modules.declarations.DeclarationModule;
import nl.wolfpackit.wolfpack.modules.news.NewsModule;
import nl.wolfpackit.wolfpack.activities.PersonalInfoActivity;

public class BaseActivity extends AppCompatActivity {
    Account account = Account.getAccount();
    public DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    ModuleFragment openedModule;
    int selectedModuleMenuItemID;
    Toolbar currentToolbar;
    Menu navigationMenu;
    View header;
    boolean isAccountMenuVisible = false;

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);

        //make the drawer open over the toolbar;
        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        currentToolbar = toolbar;
        initActionBar();

        //create reference to the base layout
        mDrawerLayout = findViewById(R.id.drawer_layout);

        //handeling the navigation
        navigationView = findViewById(R.id.nav_view);
        NavigationView.OnNavigationItemSelectedListener listener =
            new NavigationView.OnNavigationItemSelectedListener(){
                public boolean onNavigationItemSelected(MenuItem menuItem){
                    Intent intent;
                    switch(menuItem.getItemId()){
                        case R.id.news:         setModule(NewsModule.class);            break;
                        case R.id.declarations: setModule(DeclarationModule.class);     break;

                        //profile buttons
                        case R.id.profile:
                            intent = new Intent(BaseActivity.this, PersonalInfoActivity.class);
                            startActivity(intent);
                            return true;
                        case R.id.signout:
                            intent = new Intent(BaseActivity.this, SigninActivity.class);
                            intent.putExtra("signout", true);
                            startActivity(intent);
                            return true;
                    }

                    //only reaches this point if a module button was pressed
                    menuItem.setChecked(true);
                    selectedModuleMenuItemID = menuItem.getItemId();
                    mDrawerLayout.closeDrawers();

                    return true;
                }
            };
        navigationView.setNavigationItemSelectedListener(listener);
        navigationMenu = navigationView.getMenu();
        setupDrawerStyling();

        //open the default module(first)
        listener.onNavigationItemSelected(navigationView.getMenu().getItem(0));

        //set account information
        header = navigationView.getHeaderView(0);

        final ImageView imageView = header.findViewById(R.id.accountImage);
        Uri imageUri = account.getPicture(new ImageTools.ImageResultListener(){
            public void onImageRecieve(Bitmap image) {
                imageView.setImageBitmap(image);
            }
        }, this);

        TextView name = header.findViewById(R.id.accountName);
        name.setText(account.getName());

        TextView description = header.findViewById(R.id.accountDescription);
        description.setText(account.getRole().name());

        ImageButton accountButton = header.findViewById(R.id.accountMenuButton);
        accountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showAccountMenu(!isAccountMenuVisible);
            }
        });

        //hide account menu when drawer closes
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener(){
            public void onDrawerSlide(View drawerView, float slideOffset){}
            public void onDrawerOpened(View drawerView){}
            public void onDrawerClosed(View drawerView){
                if(isAccountMenuVisible) showAccountMenu(false);
            }
            public void onDrawerStateChanged(int newState){}
        });
    }

    boolean first = true;
    protected void setupDrawerStyling(){
//        iterate(mDrawerLayout);
        //go through children of the navigation view, and if it is a viewgroup, set its background to the proper background
        //I have no access to this element directly, which is the reason for this hacky solution
        for(int i=0; i<navigationView.getChildCount(); i++){
            View v = navigationView.getChildAt(i);
            if(v instanceof ViewGroup){
                v.setBackgroundResource(R.drawable.menu_bg);
            }
        }
    }
    protected void showAccountMenu(boolean show){
        MenuInflater inflater = new MenuInflater(this);

        //remove the current menu items
        for(int i=navigationMenu.size()-1; i>=0; i--){
            navigationMenu.removeItem(navigationMenu.getItem(i).getItemId());
        }

        //add the new menu items
        if(show){
            ((ImageButton)header.findViewById(R.id.accountMenuButton)).setImageResource(android.R.drawable.arrow_up_float);
            inflater.inflate(R.menu.account_options, navigationMenu);

            //select all the buttons, so their color appears white
            for(int i=navigationMenu.size()-1; i>=0; i--)
                navigationMenu.getItem(i).setChecked(true);
        }else{
            ((ImageButton)header.findViewById(R.id.accountMenuButton)).setImageResource(android.R.drawable.arrow_down_float);
            inflater.inflate(R.menu.nav, navigationMenu);

            //select the correct module
            for(int i=navigationMenu.size()-1; i>=0; i--){
                MenuItem mi = navigationMenu.getItem(i);
                if(mi.getItemId()==selectedModuleMenuItemID)
                    mi.setChecked(true);
            }
        }
        isAccountMenuVisible = show;
    }

    protected void iterate(ViewGroup viewGroup){ //test method to find the viewgroup structure of a viewgroup
        Log.w("VIEWGROUP", "{");
        for(int i=0; i<viewGroup.getChildCount(); i++){
            View v = viewGroup.getChildAt(i);
            if(v instanceof ViewGroup){
                ViewGroup vGroup = (ViewGroup)v;
                vGroup.setBackgroundResource(R.drawable.menu_bg);
                iterate(vGroup);
                Log.w("VIEWGROUP", vGroup.getId()+"-"+getResources().getResourceEntryName(vGroup.getId()));
            }
        }
        Log.w("VIEWGROUP", "}");
    }

    public void setModule(Class<? extends ModuleFragment> moduleClass){
        try {
            Object o = moduleClass.getMethod("createInstance").invoke(moduleClass);
            ModuleFragment fragment = (ModuleFragment)o;

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if(openedModule!=null)
                fragmentTransaction.remove(openedModule);
            fragmentTransaction.add(R.id.module_frame, fragment);
            fragmentTransaction.commit();

            openedModule = fragment;

            //close drawer if it was still open
            mDrawerLayout.closeDrawers();
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new Error("A valid module class should be provided");
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public ActionBar setToolbar(Toolbar toolbar){
        return setToolbar(toolbar, true);
    }
    public ActionBar setToolbar(Toolbar toolbar, boolean initialise){
        if(currentToolbar!=null)
            currentToolbar.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);
        currentToolbar = toolbar;
        if(initialise)
            return initActionBar();
        else return getSupportActionBar();
    }
    public ActionBar initActionBar(){
        //create the menu button
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menu_icon);
        return actionbar;
    }
}
