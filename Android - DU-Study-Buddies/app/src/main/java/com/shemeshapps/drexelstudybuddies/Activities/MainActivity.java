package com.shemeshapps.drexelstudybuddies.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shemeshapps.drexelstudybuddies.Fragments.BrowseGroups;
import com.shemeshapps.drexelstudybuddies.Fragments.GroupsAttending;
import com.shemeshapps.drexelstudybuddies.Fragments.MyStudyGroups;
import com.shemeshapps.drexelstudybuddies.Fragments.SuggestedGroups;
import com.shemeshapps.drexelstudybuddies.Helpers.DrawerAdapter;
import com.shemeshapps.drexelstudybuddies.Models.DrawerItem;
import com.shemeshapps.drexelstudybuddies.NetworkingServices.RequestUtil;
import com.shemeshapps.drexelstudybuddies.R;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private ListView drawerList;
    private DrawerAdapter adapter;
    private ArrayList<DrawerItem> drawerItems;
    private DrawerLayout drawerLayout;
    private android.support.v7.app.ActionBarDrawerToggle drawerToggle;
    ArrayList<Fragment> appFragments;
    private Fragment currentFragment;
    int currentFragIndex = 0;

    public enum fragments{
        SUGGESTED,BROWSE,MINE,ATTENDING,CREATE,LOGOUT
    }

    //setup drawer and action bar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar();
        setupDrawer();
        if (savedInstanceState != null) {
            currentFragIndex = savedInstanceState.getInt("currentPage");
        }
        loadScreen(currentFragIndex);
    }

    //setup action bar for drawer and such
    private void setupActionBar()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    //setup drawer items and allow them to change the fragment on press
    private void setupDrawer()
    {
        appFragments = new ArrayList<>();
        appFragments.add(new SuggestedGroups());
        appFragments.add(new BrowseGroups());
        appFragments.add(new MyStudyGroups());
        appFragments.add(new GroupsAttending());

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerList = (ListView)findViewById(R.id.drawer_list_view);
        drawerItems = new ArrayList<>();
        String[] drawerTitles = getResources().getStringArray(R.array.nav_drawer_items);
        TypedArray drawerIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        for(int i=0; i<drawerTitles.length;i++)
        {
            drawerItems.add(new DrawerItem(drawerTitles[i],drawerIcons.getResourceId(i,-1)));
        }
        drawerIcons.recycle();
        adapter = new DrawerAdapter(this,drawerItems);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loadScreen(i);
            }
        });

        drawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    //load a screen based on menu position
    public void loadScreen(fragments f){loadScreen(f.ordinal());}
    public void loadScreen(int position)
    {
        if(position == fragments.LOGOUT.ordinal())
        {
            RequestUtil.logout();
        }
        else if(position == fragments.CREATE.ordinal())
        {
            Intent i = new Intent(this, CreateGroupActivity.class);
            startActivityForResult(i,0);
            drawerLayout.closeDrawer(Gravity.LEFT);
            drawerList.setItemChecked(currentFragIndex,true);
        }
        else
        {
            currentFragment = appFragments.get(position);
            currentFragIndex = position;
            drawerList.setItemChecked(position,true);
            drawerLayout.closeDrawer(Gravity.LEFT);
            getSupportActionBar().setTitle(drawerItems.get(position).title);
            getFragmentManager().beginTransaction().replace(R.id.content_frame,appFragments.get(position)).commitAllowingStateLoss();
            invalidateOptionsMenu();
        }
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
            super.onBackPressed();
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
