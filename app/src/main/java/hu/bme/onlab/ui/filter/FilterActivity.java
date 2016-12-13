package hu.bme.onlab.ui.filter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

import hu.bme.onlab.model.post.GetCategoriesData;
import hu.bme.onlab.model.post.ListPostsRequest;
import hu.bme.onlab.onlab2.R;
import hu.bme.onlab.ui.common.DialogDismissListener;
import hu.bme.onlab.ui.common.CategorySpinnerElement;

public class FilterActivity extends AppCompatActivity implements FilterScreen {

    private ProgressDialog progressDialog;

    private EditText titleEditText;
    private EditText cityEditText;
    private Spinner categorySpinner;
    private EditText priceMinEditText;
    private EditText priceMaxEditText;
    private EditText dateFromEditText;
    private EditText dateToEditText;

    private List<GetCategoriesData> savedCategories;

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.");

    private static final String BUNDLE_CATEGORIES_KEY = "BUNDLE_CATEGORIES_KEY";

    public static final String EXTRA_LIST_POSTS_REQUEST = "EXTRA_LIST_POSTS_REQUEST";

    public static final String RESULT_EXTRA_TITLE = "RESULT_EXTRA_TITLE";
    public static final String RESULT_EXTRA_CITY = "RESULT_EXTRA_CITY";
    public static final String RESULT_EXTRA_CATEGORY = "RESULT_EXTRA_CATEGORY";
    public static final String RESULT_EXTRA_PRICE_MIN = "RESULT_EXTRA_PRICE_MIN";
    public static final String RESULT_EXTRA_PRICE_MAX = "RESULT_EXTRA_PRICE_MAX";
    public static final String RESULT_EXTRA_DATE_FROM = "RESULT_EXTRA_DATE_FROM";
    public static final String RESULT_EXTRA_DATE_TO = "RESULT_EXTRA_DATE_TO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Töltés");
        progressDialog.setMessage("Kérem várjon...");
        progressDialog.setCancelable(false);

        ListPostsRequest listPostsRequest = (ListPostsRequest)getIntent().getSerializableExtra(EXTRA_LIST_POSTS_REQUEST);
        listPostsRequest = listPostsRequest != null ? listPostsRequest : new ListPostsRequest();

        titleEditText = (EditText) findViewById(R.id.title);
        titleEditText.setText(listPostsRequest.getTitle());

        cityEditText = (EditText) findViewById(R.id.city);
        cityEditText.setText(listPostsRequest.getCity());

        categorySpinner = (Spinner) findViewById(R.id.category);

        priceMinEditText = (EditText) findViewById(R.id.price_min);
        priceMinEditText.setText(listPostsRequest.getPriceMin() != null ? listPostsRequest.getPriceMin().toString() : null);
        priceMaxEditText = (EditText) findViewById(R.id.price_max);
        priceMaxEditText.setText(listPostsRequest.getPriceMax() != null ? listPostsRequest.getPriceMax().toString() : null);

        dateFromEditText = (EditText) findViewById(R.id.date_from);
        dateFromEditText.setText(listPostsRequest.getStartDate() != null ? dateFormat.format(listPostsRequest.getStartDate().getTime()) : null);
        dateFromEditText.setOnClickListener(new DateFieldOnClickListener());

        dateToEditText = (EditText) findViewById(R.id.date_to);
        dateToEditText .setText(listPostsRequest.getEndDate() != null ? dateFormat.format(listPostsRequest.getEndDate().getTime()) : null);
        dateToEditText.setOnClickListener(new DateFieldOnClickListener());

        FilterPresenter.getInstance().attachScreen(this);

        if(savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_CATEGORIES_KEY)) {
            savedCategories = (ArrayList<GetCategoriesData>)savedInstanceState.getSerializable(BUNDLE_CATEGORIES_KEY);
            setupCategorySpinner(savedCategories);
        } else {
            FilterPresenter.getInstance().getCategories();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FilterPresenter.getInstance().detachScreen();
    }

    @Override
    public boolean onSupportNavigateUp () {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                onActionDoneClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onActionDoneClicked() {
        Intent data = new Intent();

        String title = titleEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String priceMin = priceMinEditText.getText().toString();
        String priceMax = priceMaxEditText.getText().toString();
        String dateFrom = dateFromEditText.getText().toString();
        String dateTo = dateToEditText.getText().toString();

        if(!"".equals(title)) {
            data.putExtra(RESULT_EXTRA_TITLE, title);
        }
        if(!"".equals(city)) {
            data.putExtra(RESULT_EXTRA_CITY, city);
        }

        data.putExtra(RESULT_EXTRA_CATEGORY, ((CategorySpinnerElement)categorySpinner.getSelectedItem()).getGetCategoriesData().getId());

        if(!"".equals(priceMin)) {
            data.putExtra(RESULT_EXTRA_PRICE_MIN, (Serializable) Integer.parseInt(priceMin));
        }
        if(!"".equals(priceMax)) {
            data.putExtra(RESULT_EXTRA_PRICE_MAX, (Serializable)Integer.parseInt(priceMax));
        }

        if(!"".equals(dateFrom)) {
            try {
                Date date = dateFormat.parse(dateFrom);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                data.putExtra(RESULT_EXTRA_DATE_FROM, calendar);
            } catch (ParseException e) {
                Crashlytics.logException(e);
            }
        }
        if(!"".equals(dateFrom)) {
            try {
                Date date = dateFormat.parse(dateTo);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                data.putExtra(RESULT_EXTRA_DATE_TO, calendar);
            } catch (ParseException e) {
                Crashlytics.logException(e);
            }
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(BUNDLE_CATEGORIES_KEY, new ArrayList<>(savedCategories));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onGetCategoriesSuccess(List<GetCategoriesData> categories) {
        savedCategories = categories;
        setupCategorySpinner(categories);
    }

    private void setupCategorySpinner(List<GetCategoriesData> categories) {
        ListPostsRequest listPostsRequest = (ListPostsRequest)getIntent().getSerializableExtra(EXTRA_LIST_POSTS_REQUEST);

        ArrayAdapter<CategorySpinnerElement> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        GetCategoriesData blankGetCategoriesData = new GetCategoriesData();
        blankGetCategoriesData.setName("Válassz kategóriát...");
        adapter.add(new CategorySpinnerElement(blankGetCategoriesData));

        Integer selected = null;
        for (GetCategoriesData getCategoriesData : categories) {
            if(listPostsRequest != null && getCategoriesData.getId().equals(listPostsRequest.getCategory())) {
                selected = adapter.getCount();
            }
            adapter.add(new CategorySpinnerElement(getCategoriesData));
        }

        categorySpinner.setAdapter(adapter);
        if(selected != null) {
            categorySpinner.setSelection(selected);
        }
    }

    @Override
    public void onGetCategoriesFailure() {
        new AlertDialog.Builder(this)
                .setTitle("Hiba!")
                .setMessage("Ismeretlen hiba az oldal betöltésekor!")
                .setNeutralButton("Ok", new DialogDismissListener());
    }

    @Override
    public void startLoading() {
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        progressDialog.dismiss();
    }

    public void onDatePicked(int viewId, int year, int month, int day) {
        EditText dateField = (EditText) findViewById(viewId);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        dateField.setText(dateFormat.format(calendar.getTime()));
    }

    private class DateFieldOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            DialogFragment newFragment = new DatePickerFragment();
            Bundle args = new Bundle();
            args.putInt(DatePickerFragment.ARG_VIEW_ID, v.getId());
            newFragment.setArguments(args);
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }
    }
}
