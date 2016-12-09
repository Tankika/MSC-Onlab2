package hu.bme.onlab.ui.newpost;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.io.File;
import java.io.IOException;

import hu.bme.onlab.onlab2.R;

public class NewPostActivity extends AppCompatActivity implements NewPostScreen {

    private SliderLayout sliderShow;

    private static final int SELECT_PICTURE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button addPictureButton = (Button)findViewById(R.id.newpost_add_picture);
        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPicture(v);
            }
        });

        Button clearPicturesButton = (Button)findViewById(R.id.newpost_clear_pictures);
        clearPicturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPostPresenter.getInstance().getPhotoUriSet().clear();
                updateSlider();
            }
        });

        Button submitButton = (Button)findViewById(R.id.newpost_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSubmit(v);
            }
        });

        sliderShow = (SliderLayout) findViewById(R.id.new_post_slider);
        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
    }

    @Override
    protected void onStart() {
        super.onStart();
        NewPostPresenter.getInstance().attachScreen(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        NewPostPresenter.getInstance().detachScreen();
    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    private void addPicture(View v) {
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);
        pickIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        pickIntent.addCategory(Intent.CATEGORY_OPENABLE);

        File photoFile = null;
        try {
            photoFile = NewPostPresenter.getInstance().createImageFile(this);
        } catch (IOException ex) {
            Crashlytics.logException(ex);
            // TODO notify of error
            return;
        }
        if (photoFile != null) {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoURI = FileProvider.getUriForFile(this,
                    "hu.bme.onlab.onlab2.fileprovider",
                    photoFile);

            NewPostPresenter.getInstance().setTempPhotoUri(photoURI);
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            String pickTitle = "Adj hozzá képet...";
            Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { takePhotoIntent } );

            startActivityForResult(chooserIntent, SELECT_PICTURE_REQUEST_CODE);
        }
    }

    private void attemptSubmit(View v) {
        NewPostPresenter.getInstance().sendPost(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            if(data == null) {
                NewPostPresenter.getInstance().saveTempPhotoUri();
            } else {
                NewPostPresenter.getInstance().getPhotoUriSet().add(data.getData());
            }
            updateSlider();
        }
    }

    @Override
    public void onBackPressed() {
        NewPostPresenter.getInstance().getPhotoUriSet().clear();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void updateSlider() {
        sliderShow.removeAllSliders();
        if(0 < NewPostPresenter.getInstance().getPhotoUriSet().size()) {
            sliderShow.setVisibility(View.VISIBLE);

            for (Uri uri : NewPostPresenter.getInstance().getPhotoUriSet()) {
                DefaultSliderView slideView = new DefaultSliderView(this);
                slideView.image(uri.toString());
                sliderShow.addSlider(slideView);
            }
        } else {
            sliderShow.setVisibility(View.GONE);
        }
    }
}
