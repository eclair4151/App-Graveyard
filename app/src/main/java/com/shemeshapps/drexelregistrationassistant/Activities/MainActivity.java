package com.shemeshapps.drexelregistrationassistant.Activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.shemeshapps.drexelregistrationassistant.Adapters.DrawerListAdapter;
import com.shemeshapps.drexelregistrationassistant.Fragments.BrowseFragment;
import com.shemeshapps.drexelregistrationassistant.Fragments.MyWatchList;
import com.shemeshapps.drexelregistrationassistant.Fragments.SearchClasses;
import com.shemeshapps.drexelregistrationassistant.Models.DrawerItem;
import com.shemeshapps.drexelregistrationassistant.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView drawerList;
    private DrawerListAdapter adapter;
    private ArrayList<DrawerItem> drawerItems;
    private DrawerLayout drawerLayout;
    private android.support.v7.app.ActionBarDrawerToggle drawerToggle;
    ArrayList<Fragment> appFragments;
    private Fragment currentFragment;
    int currentFragIndex = -1;

    public static enum fragments{
        MYWISHLIST,BROWSE,MINE,ATTENDING,CREATE,LOGOUT
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
        appFragments = new ArrayList<>();
        appFragments.add(new MyWatchList());
        appFragments.add(new SearchClasses());
        appFragments.add(new BrowseFragment());
        //appFragments.add(new MyWatchList());
        //appFragments.add(new MyWatchList());
        //appFragments.add(new MyWatchList());

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
        View header = (View)inflater.inflate(R.layout.dragon_header, drawerList, false);
        drawerList.addHeaderView(header, null, false);

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


    }

    public void loadScreen(fragments f){loadScreen(f.ordinal());}
    public void loadScreen(int position)
    {
        if(position != currentFragIndex)
        {
            currentFragment = appFragments.get(position);
            currentFragIndex = position;
            drawerList.setItemChecked(position+1,true);
            getSupportActionBar().setTitle(drawerItems.get(position).title);
            getFragmentManager().beginTransaction().replace(R.id.content_frame,appFragments.get(position)).commitAllowingStateLoss();
            invalidateOptionsMenu();
        }
        drawerLayout.closeDrawer(Gravity.LEFT);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("currentPage", currentFragIndex);
        super.onSaveInstanceState(savedInstanceState);
    }

    //when back is pressed check with lower fragments to see if we should close the app or let them handle it like in browse
    @Override
    public void onBackPressed()
    {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT))
        {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        else if(currentFragIndex == 1 && !(getFragmentManager().findFragmentById(R.id.content_frame) instanceof SearchClasses))
        {
            getFragmentManager().popBackStack();
        }
        else
        {
            super.onBackPressed();
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
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
