package com.shayesapps.gifts;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import com.shayesapps.gifts.Adapters.DrawerListAdapter;
import com.shayesapps.gifts.DatabaseHelper.OrmFetcher;
import com.shayesapps.gifts.DatabaseHelper.OrmHelper;
import com.shayesapps.gifts.Fragments.Browse;
import com.shayesapps.gifts.Fragments.More;
import com.shayesapps.gifts.Fragments.ParentListFragment;
import com.shayesapps.gifts.Fragments.Saved;
import com.shayesapps.gifts.Fragments.Search;
import com.shayesapps.gifts.Fragments.TopRated;
import com.shayesapps.gifts.Models.DrawerItem;
import com.shayesapps.gifts.NetworkingServices.RequestUtil;


public class MainActivity extends Activity {

    private ListView drawerList;
    private DrawerListAdapter adapter;
    private ArrayList<DrawerItem> drawerItems;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private TextView actionBarTitle;
    ArrayList<Fragment> appFragments;
    private Fragment currentFragment;
    int currentFragIndex = 0;

    public enum fragments{
        TOPRATED,BROWSE,SEARCH,SAVED,MORE
    }

    //setup database, drawer and action bar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.shayesapps.gifts.R.layout.activity_main);
        new RequestUtil().init(this);
        OrmFetcher.init(this);
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
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);
        //getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowCustomEnabled(true);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER );
        getActionBar().setCustomView(com.shayesapps.gifts.R.layout.actionbar_layout);


        actionBarTitle = (TextView)getActionBar().getCustomView().findViewById(com.shayesapps.gifts.R.id.actionBarTitle);
    }

    //setup drawer items and allow them to change the fragment on press
    private void setupDrawer()
    {
        appFragments = new ArrayList<Fragment>();
        appFragments.add(new TopRated());
        appFragments.add(new Browse());
        appFragments.add(new Search());
        appFragments.add(new Saved());
        appFragments.add(new More());

        drawerLayout = (DrawerLayout)findViewById(com.shayesapps.gifts.R.id.drawer_layout);
        drawerList = (ListView)findViewById(com.shayesapps.gifts.R.id.drawer_list_view);
        drawerItems = new ArrayList<DrawerItem>();
        String[] drawerTitles = getResources().getStringArray(com.shayesapps.gifts.R.array.nav_drawer_items);
        TypedArray drawerIcons = getResources().obtainTypedArray(com.shayesapps.gifts.R.array.nav_drawer_icons);

        for(int i=0; i<drawerTitles.length;i++)
        {
            drawerItems.add(new DrawerItem(drawerTitles[i],drawerIcons.getResourceId(i,-1)));
        }
        drawerIcons.recycle();
        adapter = new DrawerListAdapter(this,drawerItems);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loadScreen(i);
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout, com.shayesapps.gifts.R.drawable.ic_drawer, com.shayesapps.gifts.R.string.app_name, com.shayesapps.gifts.R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    //load a screen based on menu position
    private void loadScreen(int position)
    {
        currentFragment = appFragments.get(position);
        currentFragIndex = position;
        drawerList.setItemChecked(position,true);
        drawerLayout.closeDrawer(Gravity.LEFT);
        actionBarTitle.setText(drawerItems.get(position).title);

        getFragmentManager().beginTransaction().replace(com.shayesapps.gifts.R.id.content_frame,appFragments.get(position)).commitAllowingStateLoss();
        invalidateOptionsMenu();
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
        if(currentFragment == appFragments.get(fragments.BROWSE.ordinal()) || currentFragment == appFragments.get(fragments.SEARCH.ordinal()))
        {
            if(((ParentListFragment)currentFragment).backPressed())
            {
                super.onBackPressed();
            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.shayesapps.gifts.R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
