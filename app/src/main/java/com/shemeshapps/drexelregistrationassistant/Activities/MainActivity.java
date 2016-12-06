package com.shemeshapps.drexelregistrationassistant.Activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.shemeshapps.drexelregistrationassistant.Adapters.DrawerListAdapter;
import com.shemeshapps.drexelregistrationassistant.Fragments.BrowseFragment;
import com.shemeshapps.drexelregistrationassistant.Fragments.MyWatchList;
import com.shemeshapps.drexelregistrationassistant.Fragments.RegistrationFragment;
import com.shemeshapps.drexelregistrationassistant.Fragments.SearchFragment;
import com.shemeshapps.drexelregistrationassistant.Models.DrawerItem;
import com.shemeshapps.drexelregistrationassistant.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView drawerList;
    private DrawerListAdapter adapter;
    private ArrayList<DrawerItem> drawerItems;
    private DrawerLayout drawerLayout;
    private android.support.v7.app.ActionBarDrawerToggle drawerToggle;
    fragments currentFrag = null;


    public static enum fragments{
        MYWISHLIST,SEARCH,BROWSE,REGISTRATION, NOTIFICATIONS,SETTINGS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar();
        setupDrawer();
        if (savedInstanceState != null) {
            loadScreen(savedInstanceState.getInt("currentPage"));
        }
        else
        {
            loadScreen(fragments.MYWISHLIST);
        }
    }

    private void setupActionBar()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setupDrawer()
    {
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerList = (ListView)findViewById(R.id.left_drawer);
        drawerItems = new ArrayList<>();
        String[] drawerTitles = getResources().getStringArray(R.array.nav_drawer_items);
        TypedArray drawerIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        for(int i=0; i<drawerTitles.length;i++)
        {
            drawerItems.add(new DrawerItem(drawerTitles[i],drawerIcons.getResourceId(i,-1)));
        }
        drawerIcons.recycle();

        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.dragon_header, drawerList, false);
        try
        {
            drawerList.addHeaderView(header, null, false);
        }
        catch (Exception e)
        {

        }

        adapter = new DrawerListAdapter(this,drawerItems);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loadScreen(i-1);
            }
        });

        drawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        for (fragments f:fragments.values())
        {
            Fragment frag = getFragmentManager().findFragmentByTag(f.name());
            if(frag!=null)
            {
                ft.hide(frag);
            }
        }
        ft.commitAllowingStateLoss();

    }

    public void loadScreen(fragments f)
    {
        if(f != currentFrag)
        {
            FragmentManager fm = getFragmentManager();
            fm.executePendingTransactions();
            drawerList.setItemChecked(f.ordinal()+1,true);
            getSupportActionBar().setTitle(drawerItems.get(f.ordinal()).title);


            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if(currentFrag != null)
            {
                Fragment prevFrag = fm.findFragmentByTag(currentFrag.name());
                if(prevFrag != null)
                {
                    ft.hide(fm.findFragmentByTag(currentFrag.name()));
                }
            }

            Fragment nextFrag = fm.findFragmentByTag(f.name());
            if(nextFrag == null)
            {
                ft.add(R.id.content_frame,getFragmentFromEnum(f),f.name());
            }
            else
            {
                ft.show(nextFrag);
            }
            ft.commitAllowingStateLoss();
            invalidateOptionsMenu();
            currentFrag = f;

        }
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    public Fragment getFragmentFromEnum(fragments frag)
    {
        switch (frag) {
            case MYWISHLIST:
                return new MyWatchList();
            case SEARCH:
                return new SearchFragment();
            case BROWSE:
                return new BrowseFragment();
            case REGISTRATION:
                return new RegistrationFragment();
            case NOTIFICATIONS:
                return new MyWatchList();
            case SETTINGS:
                return new MyWatchList();
            default:
                return new MyWatchList();
        }

    }
    public void loadScreen(int position)
    {

        loadScreen(fragments.values()[position]);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("currentPage", currentFrag.ordinal());
        super.onSaveInstanceState(savedInstanceState);
    }

    //when back is pressed check with lower fragments to see if we should close the app or let them handle it like in browse
    @Override
    public void onBackPressed()
    {
        boolean backHandled = false;
        if(drawerLayout.isDrawerOpen(Gravity.LEFT))
        {
            drawerLayout.closeDrawer(Gravity.LEFT);
            backHandled = true;
        }
        else if(currentFrag == fragments.BROWSE)
        {
            backHandled = ((BrowseFragment)getFragmentManager().findFragmentByTag(fragments.BROWSE.name())).goBack();
        }

        if(!backHandled)
        {
            super.onBackPressed();
        }
    }

    public void dismissKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            dismissKeyboard();
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
