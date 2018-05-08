package nl.wolfpackit.wolfpack.modules;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.lang.reflect.InvocationTargetException;

import nl.wolfpackit.wolfpack.R;
import nl.wolfpackit.wolfpack.database.Account;

public class ModuleFragment extends Fragment{
    protected BaseActivity activity;
    protected Account account = Account.getAccount();
    protected View view;
    protected int layoutID;

    public ModuleFragment(){
        // Required empty public constructor
    }

    public static ModuleFragment createInstance(int layoutID, Class<? extends ModuleFragment> c){
        ModuleFragment fragment = null;
        try {
            fragment = c.getConstructor().newInstance();
            Bundle args = new Bundle();
            args.putInt("layoutID", layoutID);
            fragment.setArguments(args);
            return fragment;
        }catch (java.lang.InstantiationException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new Error("no valid modulefragment class was provided");
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            layoutID = getArguments().getInt("layoutID");
        }else{
            throw new Error("a layout ID should be provided");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(layoutID, container, false);
        return view;
    }
    public void onViewCreated(View view, Bundle bundle){
        Activity act = getActivity();
        if(!(act instanceof BaseActivity)){
            throw new Error("Fragment should only be opened in a base activity");
        }else{
            activity = (BaseActivity)act;
            setup();
        }
    }
    protected void setup(){
        setToolbar();
    }

    public ActionBar setToolbar(){
        Toolbar toolbar = activity.findViewById(getToolbar());
        String toolbarText = getToolbarText();
        if(toolbarText.length()>0)
            toolbar.setTitle(toolbarText);
        return activity.setToolbar(toolbar);
    }
    public String getToolbarText(){
        return getString(R.string.app_name);
    }
    public int getToolbar(){
        return R.id.custom_toolbar;
    }
}
