package hu.bme.onlab.ui.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import java.util.Calendar;
import java.util.List;

import hu.bme.onlab.model.post.ListPostsRequest;
import hu.bme.onlab.model.post.Post;
import hu.bme.onlab.network.NetworkSessionStore;
import hu.bme.onlab.onlab2.R;
import hu.bme.onlab.ui.filter.FilterActivity;
import hu.bme.onlab.ui.login.LoginActivity;
import hu.bme.onlab.ui.newpost.NewPostActivity;
import hu.bme.onlab.ui.signup.SignupActivity;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
        implements MainScreen, NavigationView.OnNavigationItemSelectedListener {

    private PostListAdapter postListAdapter;
    private ProgressDialog progressDialog;
    private NavigationView navigationView;
    private SwipeRefreshLayout listPostSwipeRefreshLayout;
    private FloatingActionButton fab;

    private MenuItem clearFilterMenuItem;

    private ListPostsRequest savedListPostsRequest;

    private static final String BUNDLE_POSTS_KEY = "BUNDLE_POSTS_KEY";
    private static final String BUNDLE_PAGE_KEY = "BUNDLE_PAGE_KEY";
    private static final String BUNDLE_LIST_POSTS_REQUEST_KEY = "BUNDLE_LIST_POSTS_REQUEST_KEY";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int REQUEST_CODE_NEW_POST = 2;
    private static final int REQUEST_CODE_FILTER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        Toolbar toolbar =  (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToNewPost();
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
                        MainPresenter.getInstance().loadPosts(savedListPostsRequest);
                    }
                }
            }
        });

        listPostSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.post_list_swiperefresh);
        listPostSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MainPresenter.getInstance().reloadPosts(savedListPostsRequest);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Töltés");
        progressDialog.setMessage("Kérem várjon...");
        progressDialog.setCancelable(false);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MainPresenter.getInstance().attachScreen(this);
        if(NetworkSessionStore.getSessionId() == null) {
            MainPresenter.getInstance().init();
        } else if(savedInstanceState != null) {
            MainPresenter.getInstance().setPage(savedInstanceState.getInt(BUNDLE_PAGE_KEY, 0));
            List<Post> savedPosts = (ArrayList<Post>)savedInstanceState.getSerializable(BUNDLE_POSTS_KEY);
            savedListPostsRequest = (ListPostsRequest) savedInstanceState.getSerializable(BUNDLE_LIST_POSTS_REQUEST_KEY);
            refreshPostList(savedPosts != null ? savedPosts : new ArrayList<Post>());
        } else {
            MainPresenter.getInstance().reloadPosts(savedListPostsRequest);
        }

        if(PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setMenuVisibilities();
    }

    @Override
    public void setMenuVisibilities() {
        if(NetworkSessionStore.getUser() != null) {
            navigationView.getMenu().findItem(R.id.nav_newpost).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_signup).setVisible(false);
            fab.setVisibility(View.VISIBLE);
        } else {
            navigationView.getMenu().findItem(R.id.nav_newpost).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_signup).setVisible(true);
            fab.setVisibility(View.GONE);
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
        outState.putSerializable(BUNDLE_LIST_POSTS_REQUEST_KEY, savedListPostsRequest);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        clearFilterMenuItem = menu.findItem(R.id.action_clear_filter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_filter:
                savedListPostsRequest = null;

                clearFilterMenuItem.setVisible(false);
                MainPresenter.getInstance().reloadPosts(savedListPostsRequest);

                return true;
            case R.id.action_filter:
                Intent intent = new Intent(this, FilterActivity.class);
                intent.putExtra(FilterActivity.EXTRA_LIST_POSTS_REQUEST, savedListPostsRequest);
                startActivityForResult(intent, REQUEST_CODE_FILTER);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_signup) {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_newpost) {
            navigateToNewPost();
        } else if (id == R.id.nav_logout) {
            MainPresenter.getInstance().logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navigateToNewPost() {
        Intent intent = new Intent(this, NewPostActivity.class);
        startActivityForResult(intent, REQUEST_CODE_NEW_POST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_CODE_NEW_POST) {
                MainPresenter.getInstance().reloadPosts(savedListPostsRequest);
            } else if(requestCode == REQUEST_CODE_FILTER) {
                savedListPostsRequest = new ListPostsRequest();

                savedListPostsRequest.setTitle(data.getStringExtra(FilterActivity.RESULT_EXTRA_TITLE));
                savedListPostsRequest.setCity(data.getStringExtra(FilterActivity.RESULT_EXTRA_CITY));
                savedListPostsRequest.setCategory((Long)data.getSerializableExtra(FilterActivity.RESULT_EXTRA_CATEGORY));
                savedListPostsRequest.setPriceMin((Integer)data.getSerializableExtra(FilterActivity.RESULT_EXTRA_PRICE_MIN));
                savedListPostsRequest.setPriceMax((Integer)data.getSerializableExtra(FilterActivity.RESULT_EXTRA_PRICE_MAX));
                savedListPostsRequest.setStartDate((Calendar) data.getSerializableExtra(FilterActivity.RESULT_EXTRA_DATE_FROM));
                savedListPostsRequest.setEndDate((Calendar)data.getSerializableExtra(FilterActivity.RESULT_EXTRA_DATE_TO));

                clearFilterMenuItem.setVisible(true);
                MainPresenter.getInstance().reloadPosts(savedListPostsRequest);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void clearPostList() {
        postListAdapter.getPosts().clear();
        postListAdapter.notifyDataSetChanged();
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
