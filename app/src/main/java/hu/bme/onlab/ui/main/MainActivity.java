package hu.bme.onlab.ui.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.bme.onlab.model.post.Post;
import hu.bme.onlab.network.NetworkSessionStore;
import hu.bme.onlab.onlab2.R;
import hu.bme.onlab.ui.login.LoginActivity;
import hu.bme.onlab.ui.signup.SignupActivity;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
        implements MainScreen, NavigationView.OnNavigationItemSelectedListener {

    private PostListAdapter postListAdapter;
    private ProgressDialog progressDialog;
    private NavigationView navigationView;

    public static final String BUNDLE_POSTS_KEY = "BUNDLE_POSTS_KEY";
    public static final String BUNDLE_PAGE_KEY = "BUNDLE_PAGE_KEY";
    private SwipeRefreshLayout listPostSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        Toolbar toolbar =  (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        RecyclerView listPostRecyclerView = (RecyclerView) findViewById(R.id.post_list);
        listPostRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listPostRecyclerView.setLayoutManager(mLayoutManager);
        postListAdapter = new PostListAdapter();
        listPostRecyclerView.setAdapter(postListAdapter);

        listPostRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(!progressDialog.isShowing()) {
                    if(!recyclerView.canScrollVertically(1)) {
                        // User is at the end of the list and trying to scroll down.
                        MainPresenter.getInstance().loadPosts();
                    }
                }
            }
        });

        listPostSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.post_list_swiperefresh);
        listPostSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postListAdapter.getPosts().clear();
                MainPresenter.getInstance().reloadPosts();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Töltés");
        progressDialog.setMessage("Kérem várjon...");
        progressDialog.setCancelable(false);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MainPresenter.getInstance().attachScreen(this);
        if(savedInstanceState == null) {
            MainPresenter.getInstance().init();
        } else {
            MainPresenter.getInstance().setPage(savedInstanceState.getInt(BUNDLE_PAGE_KEY, 0));
            List<Post> savedPosts = (ArrayList<Post>)savedInstanceState.getSerializable(BUNDLE_POSTS_KEY);
            refreshPostList(savedPosts != null ? savedPosts : new ArrayList<Post>());
        }
    }

    @Override
    protected void onResume() {
        super.onStart();

        setMenuVisibilities();
    }

    @Override
    public void setMenuVisibilities() {
        if(NetworkSessionStore.getUser() != null) {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_signup).setVisible(false);
        } else {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_signup).setVisible(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MainPresenter.getInstance().detachScreen();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(BUNDLE_POSTS_KEY, new ArrayList<>(postListAdapter.getPosts()));
        outState.putInt(BUNDLE_PAGE_KEY, MainPresenter.getInstance().getPage());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_signup) {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            MainPresenter.getInstance().logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void refreshPostList(List<Post> posts) {
        postListAdapter.getPosts().addAll(posts);
        postListAdapter.notifyDataSetChanged();
    }

    @Override
    public void startLoading() {
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        progressDialog.dismiss();
        listPostSwipeRefreshLayout.setRefreshing(false);
    }
}
