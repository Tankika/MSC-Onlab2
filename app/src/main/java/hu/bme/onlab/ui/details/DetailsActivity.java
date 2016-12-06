package hu.bme.onlab.ui.details;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import hu.bme.onlab.model.post.GetPostResponse;
import hu.bme.onlab.onlab2.R;

public class DetailsActivity extends AppCompatActivity implements DetailsScreen {

    public static final String INTENT_EXTRA_POST_ID = "INTENT_EXTRA_POST_ID";
    public final static String BUDNLE_POST_KEY = "BUDNLE_POST_KEY";

    private DetailsSectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ProgressDialog progressDialog;

    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.savedInstanceState = savedInstanceState;

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Töltés");
        progressDialog.setMessage("Kérem várjon...");
        progressDialog.setCancelable(false);

    }

    @Override
    public void onGetPostSuccess(GetPostResponse getPostResponse) {
        setupSectionsAdapter(getPostResponse);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(BUDNLE_POST_KEY, mSectionsPagerAdapter.getGetPostResponse());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DetailsPresenter.getInstance().attachScreen(this);

        Intent intent = getIntent();
        GetPostResponse savedPost = savedInstanceState != null ? (GetPostResponse)savedInstanceState.getSerializable(BUDNLE_POST_KEY) : null;
        if(savedPost != null) {
            setupSectionsAdapter(savedPost);
        } else {
            DetailsPresenter.getInstance().getPost(intent.getIntExtra(INTENT_EXTRA_POST_ID, -1));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        DetailsPresenter.getInstance().detachScreen();
    }

    @Override
    public void startLoading() {
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        progressDialog.dismiss();
    }

    private void setupSectionsAdapter(GetPostResponse getPostResponse) {
        mSectionsPagerAdapter = new DetailsSectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.setGetPostResponse(getPostResponse);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
}
