package com.abhishek.retrofitv2;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView newsText;
    RecyclerView recyclerView;
    SwipeRefreshLayout srp;
    PopupMenu popupMenu;
    FloatingActionButton fab;
    ExpandableListView expandableListView;
    ExpandableListadAdap mMenuAdapter;
    List<ExpandedMenuModel> listDataHeader;
    TextView textView;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    String coun,cate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        srp = findViewById(R.id.pull_to_ref);
        fab = findViewById(R.id.fab);
        expandableListView = findViewById(R.id.navigationmenu);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        coun = "in";
        cate = "";
        generatedata();
        mMenuAdapter = new ExpandableListadAdap(this, listDataHeader, listDataChild, expandableListView);

        // setting list adapter
        expandableListView.setAdapter(mMenuAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                //Log.d("DEBUG", "submenu item clicked");
                if(i==2)
                {
                    if(i1==0)
                        coun = "in";
                    if(i1==1)
                        coun = "us";
                    if(i1==2)
                        coun = "id";
                    fetchdata(coun,cate);
                }
                if(i==1) {
                    if(i1==0)
                       cate = "general";
                    if(i1==1)
                        cate = "sports";
                    if(i1==2)
                        cate = "business";
                    if(i1==3)
                        cate = "entertainment";
                    if(i1==4)
                        cate = "health";
                    if(i1==5)
                        cate = "science";
                    if(i1==6)
                        cate = "technology";
                    fetchdata(coun,cate);

                }
                return false;
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //Log.d("DEBUG", "heading clicked");
                return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        srp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchdata("in","");
                srp.setRefreshing(false);
            }
        });
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        fetchdata("in","");


    }

    private void generatedata() {
        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, List<String>>();

        ExpandedMenuModel item = new ExpandedMenuModel();
        item.setName("News");
        listDataHeader.add(item);

        ExpandedMenuModel item1 = new ExpandedMenuModel();
        item1.setName("Category");
        // Adding data header
        listDataHeader.add(item1);

        ExpandedMenuModel item2 = new ExpandedMenuModel();
        item2.setName("Language");
        listDataHeader.add(item2);

        ExpandedMenuModel item3 = new ExpandedMenuModel();
        item3.setName("About");
        listDataHeader.add(item3);

        // Adding child data
        List<String> heading = new ArrayList<String>();
        heading.add("Top Headlines");
        heading.add("Everything");
        heading.add("Back to Main");


        List<String> heading1 = new ArrayList<String>();
        heading1.add("General");
        heading1.add("Sports");
        heading1.add("Business");
        heading1.add("Entertainment");
        heading1.add("Health");
        heading1.add("Science");
        heading1.add("Technology");

        List<String> heading2 = new ArrayList<String>();
        heading2.add("India");
        heading2.add("USA");
        heading2.add("Indonesia");


        List<String> heading3 = new ArrayList<String>();
        heading3.add("About Creator");
        heading3.add("Contact us");

        listDataChild.put(listDataHeader.get(0), heading);// Header, Child data
        listDataChild.put(listDataHeader.get(1), heading1);// Header, Child data
        listDataChild.put(listDataHeader.get(2), heading2);
        listDataChild.put(listDataHeader.get(3), heading3);


    }

    private void fetchdata(String country,String category) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NewsApi newsApi = retrofit.create(NewsApi.class);
        Call<NewsModel> call = newsApi.getNews(country,NewsApi.apikey,97,category);
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                if(!response.isSuccessful()) {
                    newsText.setText(response.code());
                    return;
                }
                NewsModel newsModel = response.body();
                List<ArticleModel> articleModels = newsModel.getArticles();
                NewsAdapter newsAdapter = new NewsAdapter(Main2Activity.this,articleModels);
                recyclerView.setAdapter(newsAdapter);
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Log.d("bosdk",t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(Main2Activity.this, "Searcg Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
