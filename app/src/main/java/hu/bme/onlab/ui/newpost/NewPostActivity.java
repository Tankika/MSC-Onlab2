package hu.bme.onlab.ui.newpost;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import hu.bme.onlab.model.post.GetCategoriesData;
import hu.bme.onlab.model.post.SendPostData;
import hu.bme.onlab.onlab2.R;
import hu.bme.onlab.ui.common.DialogDismissListener;
import hu.bme.onlab.ui.common.CategorySpinnerElement;

public class NewPostActivity extends AppCompatActivity implements NewPostScreen {

    private ProgressDialog progressDialog;
    private SliderLayout sliderShow;

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextPostalCode;
    private EditText editTextPriceMin;
    private EditText editTextPriceMax;
    private Spinner categorySpinner;
    private EditText editTextName;
    private EditText editTextPhone;

    private List<GetCategoriesData> savedCategories;

    private static final int SELECT_PICTURE_REQUEST_CODE = 1;

    private static final String BUNDLE_CATEGORIES_KEY = "BUNDLE_CATEGORIES_KEY";

    private Pattern postalCodePattern = Pattern.compile("^\\d{4}$");
    private Pattern namePattern = Pattern.compile("^[a-zA-Z]+( [a-zA-Z]+){1,}$");
    private Pattern phonePattern = Pattern.compile("^\\+36 (1|\\d{2}) \\d{3} \\d{3,4}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Töltés");
        progressDialog.setMessage("Kérem várjon...");
        progressDialog.setCancelable(false);

        sliderShow = (SliderLayout) findViewById(R.id.new_post_slider);
        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        final LinearLayout descriptionSection = (LinearLayout)findViewById(R.id.description_section);
        TextView descriptionSectionHeader = (TextView)findViewById(R.id.description_section_header);
        descriptionSectionHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSectionVisibility(descriptionSection);
            }
        });

        final LinearLayout detailsSection = (LinearLayout)findViewById(R.id.details_section);
        TextView detailsSectionHeader = (TextView)findViewById(R.id.details_section_header);
        detailsSectionHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSectionVisibility(detailsSection);
            }
        });

        final LinearLayout advertiserSection = (LinearLayout)findViewById(R.id.advertiser_section);
        TextView advertiserSectionHeader = (TextView)findViewById(R.id.advertiser_section_header);
        advertiserSectionHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSectionVisibility(advertiserSection);
            }
        });

        editTextTitle = (EditText) findViewById(R.id.title);
        editTextDescription = (EditText) findViewById(R.id.description);
        editTextPostalCode = (EditText) findViewById(R.id.postal_code);
        editTextPriceMin = (EditText) findViewById(R.id.price_min);
        editTextPriceMax = (EditText) findViewById(R.id.price_max);
        categorySpinner = (Spinner)  findViewById(R.id.category);
        editTextName = (EditText) findViewById(R.id.name);
        editTextPhone = (EditText) findViewById(R.id.phone);

        NewPostPresenter.getInstance().attachScreen(this);

        if(savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_CATEGORIES_KEY)) {
            savedCategories = (ArrayList<GetCategoriesData>)savedInstanceState.getSerializable(BUNDLE_CATEGORIES_KEY);
            setupCategorySpinner(savedCategories);
        } else {
            NewPostPresenter.getInstance().getCategories();
        }
    }

    private void toggleSectionVisibility(View section) {
        if(section.getVisibility() == View.VISIBLE) {
            section.setVisibility(View.GONE);
        } else {
            section.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NewPostPresenter.getInstance().detachScreen();
    }

    private void addPicture() {
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);
        pickIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        pickIntent.addCategory(Intent.CATEGORY_OPENABLE);

        File photoFile;
        try {
            photoFile = NewPostPresenter.getInstance().createImageFile(this);
        } catch (IOException ex) {
            Crashlytics.logException(ex);
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

    private void attemptSubmit() {
        editTextTitle.setError(null);
        editTextDescription.setError(null);
        editTextPostalCode.setError(null);
        editTextPriceMin.setError(null);
        editTextPriceMax.setError(null);
        editTextName.setError(null);
        editTextPhone.setError(null);

        // hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();
        if (focusedView == null) {
            focusedView = new View(this);
        }
        imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);

        boolean cancel = false;
        View focusView = null;

        if(!validateTitleField()) {
            focusView = editTextTitle;
            cancel = true;
        } else if(!validateDescriptionField()) {
            focusView = editTextDescription;
            cancel = true;
        } else if(!validatePostalCodeField()) {
            focusView = editTextPostalCode;
            cancel = true;
        } else if(!validatePriceField()) {
            focusView = editTextPriceMin;
            cancel = true;
        } else if(!validateNameField()) {
            focusView = editTextName;
            cancel = true;
        } else if(!validatePhoneField()) {
            focusView = editTextPhone;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            SendPostData sendPostData = new SendPostData();

            String title = editTextTitle.getText().toString();
            String description = editTextDescription.getText().toString();
            String postalCode = editTextPostalCode.getText().toString();
            String priceMin = editTextPriceMin.getText().toString();
            String priceMax = editTextPriceMax.getText().toString();
            String category = ((CategorySpinnerElement)categorySpinner.getSelectedItem()).getGetCategoriesData().getId().toString();
            String name = editTextName.getText().toString();
            String phone = editTextPhone.getText().toString();

            sendPostData.setTitle(title);
            sendPostData.setDescription(description);
            sendPostData.setPostalCode(postalCode);
            sendPostData.setPriceMin(priceMin);
            sendPostData.setPriceMax(priceMax);
            sendPostData.setCategory(category);
            sendPostData.setName(name);
            sendPostData.setPhone(phone);

            NewPostPresenter.getInstance().sendPost(sendPostData, this);
        }
    }

    private boolean validateTitleField() {
        String title = editTextTitle.getText().toString();

        if(TextUtils.isEmpty(title)) {
            editTextTitle.setError(getString(R.string.error_field_required));
            return false;
        } else if(title.length() < 12) {
            editTextTitle.setError(getString(R.string.error_field_minlength, 12));
            return false;
        } else if(100 < title.length()) {
            editTextTitle.setError(getString(R.string.error_field_maxlength, 100));
            return false;
        }

        return true;
    }

    private boolean validateDescriptionField() {
        String description = editTextDescription.getText().toString();

        if(TextUtils.isEmpty(description)) {
            editTextDescription.setError(getString(R.string.error_field_required));
            return false;
        } else if(description.length() < 20) {
            editTextDescription.setError(getString(R.string.error_field_minlength, 20));
            return false;
        } else if(4000 < description.length()) {
            editTextDescription.setError(getString(R.string.error_field_maxlength, 4000));
            return false;
        }

        return true;
    }

    private boolean validatePostalCodeField() {
        String postalCode = editTextPostalCode.getText().toString();

        if(TextUtils.isEmpty(postalCode)) {
            editTextPostalCode.setError(getString(R.string.error_field_required));
            return false;
        } else if(!postalCodePattern.matcher(postalCode).matches()) {
            editTextPostalCode.setError(getString(R.string.error_field_pattern));
            return false;
        }

        return true;
    }

    private boolean validatePriceField() {
        String priceMinString = editTextPriceMin.getText().toString();
        String priceMaxString = editTextPriceMax.getText().toString();

        Integer priceMin = null;
        Integer priceMax = null;
        try {
            if(!TextUtils.isEmpty(priceMinString)) {
                priceMin = Integer.parseInt(priceMinString);
            }
        } catch(NumberFormatException e) {
            editTextPriceMin.setError(getString(R.string.error_field_priceformat));
            return false;
        }
        try {
            if(!TextUtils.isEmpty(priceMaxString)) {
                priceMax = Integer.parseInt(priceMaxString);
            }
        } catch(NumberFormatException e) {
            editTextPriceMax.setError(getString(R.string.error_field_priceformat));
            return false;
        }

        if(priceMin != null) {
            if(priceMin < 0) {
                editTextPriceMin.setError(getString(R.string.error_field_minValue, 0));
                return false;
            } else if(999999999 < priceMin) {
                editTextPriceMin.setError(getString(R.string.error_field_maxValue, 999999999));
                return false;
            }
        }

        if(priceMax != null) {
            if(priceMax < 0) {
                editTextPriceMax.setError(getString(R.string.error_field_minValue, 0));
                return false;
            } else if(999999999 < priceMax) {
                editTextPriceMax.setError(getString(R.string.error_field_maxValue, 999999999));
                return false;
            }
        }

        if(priceMin != null && priceMax != null) {
            if(priceMin > priceMax) {
                editTextPriceMin.setError(getString(R.string.error_field_price_min_more_than_max));
                return false;
            }
        }

        return true;
    }

    private boolean validateNameField() {
        String name = editTextName.getText().toString();

        if(TextUtils.isEmpty(name)) {
            editTextName.setError(getString(R.string.error_field_required));
            return false;
        } else if(!namePattern.matcher(name).matches()) {
            editTextName.setError(getString(R.string.error_field_pattern));
            return false;
        }

        return true;
    }

    private boolean validatePhoneField() {
        String phone = editTextPhone.getText().toString();

        if(TextUtils.isEmpty(phone)) {
            editTextPhone.setError(getString(R.string.error_field_required));
            return false;
        } else if(!phonePattern.matcher(phone).matches()) {
            editTextPhone.setError(getString(R.string.error_field_pattern));
            return false;
        }

        return true;
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
    public boolean onSupportNavigateUp () {
        onBackPressed();
        return true;
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
            case R.id.action_attach:
                addPicture();
                return true;
            case R.id.action_send:
                attemptSubmit();
                return true;
            case R.id.action_clear_pictures:
                clearPictures();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearPictures() {
        NewPostPresenter.getInstance().getPhotoUriSet().clear();
        updateSlider();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(BUNDLE_CATEGORIES_KEY, new ArrayList<>(savedCategories));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void startLoading() {
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void onSendPostSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onSendPostFailure() {
        new AlertDialog.Builder(this)
            .setTitle("Hiba!")
            .setMessage("Ismeretlen hiba a hírdetés feladásakor")
            .setNeutralButton("Ok", new DialogDismissListener());
    }

    @Override
    public void onGetCategoriesSuccess(List<GetCategoriesData> categories) {
        savedCategories = categories;
        setupCategorySpinner(categories);
    }

    private void setupCategorySpinner(List<GetCategoriesData> categories) {
        ArrayAdapter<CategorySpinnerElement> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (GetCategoriesData getCategoriesData : categories) {
            adapter.add(new CategorySpinnerElement(getCategoriesData));
        }

        categorySpinner.setAdapter(adapter);
    }

    @Override
    public void onGetCategoriesFailure() {
        new AlertDialog.Builder(this)
                .setTitle("Hiba!")
                .setMessage("Ismeretlen hiba az oldal betöltésekor!")
                .setNeutralButton("Ok", new DialogDismissListener());
    }

}
