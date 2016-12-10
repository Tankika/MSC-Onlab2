package hu.bme.onlab.ui.newpost;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

import com.crashlytics.android.Crashlytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hu.bme.onlab.interactor.post.PostInteractor;
import hu.bme.onlab.interactor.post.event.GetCategoriesCallCompletedEvent;
import hu.bme.onlab.interactor.post.event.SendPostCallCompletedEvent;
import hu.bme.onlab.model.post.GetCategoriesData;
import hu.bme.onlab.model.post.ImageData;
import hu.bme.onlab.model.post.SendPostData;
import hu.bme.onlab.network.NetworkSessionStore;
import hu.bme.onlab.ui.Presenter;

public class NewPostPresenter extends Presenter<NewPostScreen> {

    private static NewPostPresenter instance;

    private PostInteractor postInteractor;

    private Uri tempPhotoUri;
    final private Set<Uri> photoUriSet;

    private NewPostPresenter() {
        postInteractor = new PostInteractor();
        photoUriSet = new HashSet<>();
    }

    public static NewPostPresenter getInstance() {
        if(instance == null) {
            instance = new NewPostPresenter();
        }
        return instance;
    }

    @Override
    public void attachScreen(NewPostScreen screen) {
        super.attachScreen(screen);
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }

    public void setTempPhotoUri(Uri tempPhotoUri) {
        this.tempPhotoUri = tempPhotoUri;
    }

    public Set<Uri> getPhotoUriSet() {
        return photoUriSet;
    }

    public void saveTempPhotoUri() {
        if(tempPhotoUri == null) {
            throw new IllegalStateException("Please set the temp photo uri before trying to save it");
        }

        photoUriSet.add(tempPhotoUri);
        tempPhotoUri = null;
    }

    public void getCategories() {
        screen.startLoading();
        postInteractor.getCategories();
    }

    public void sendPost(SendPostData sendPostData, Context context) {
        screen.startLoading();

        for (Uri uri : photoUriSet) {
            ImageData imageData = null;

            try {
                if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                    imageData = getImageDataForContentScheme(context, uri);
                } else if(ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
                    imageData = getImageDataForFileScheme(context, uri);
                } else {
                    throw new Exception("Invalid scheme: " + uri.getScheme());
                }

                sendPostData.getImageDataList().add(imageData);
            } catch (Exception e) {
                Crashlytics.logException(e);
                continue;
            }
        }
        photoUriSet.clear();

        postInteractor.sendPost(sendPostData);
    }

    private ImageData getImageDataForContentScheme(Context context, Uri uri) throws Exception {
        ImageData imageData = new ImageData();

        // Set filename
        Cursor cursor = null;
        String[] proj = { OpenableColumns.DISPLAY_NAME };
        try {
            cursor = context.getContentResolver().query(uri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            imageData.setName(cursor.getString(column_index));
        } finally {
            if(cursor != null) cursor.close();
        }

        // Set mimetype
        imageData.setMimeType(context.getContentResolver().getType(uri));

        // Set content
        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);

            imageData.setContent(getBytes(is));
        } finally {
            if(is == null) is.close();
        }

        return imageData;
    }

    private ImageData getImageDataForFileScheme(Context context, Uri uri) throws Exception {
        ImageData imageData = new ImageData();

        File imageFile = new File(uri.getPath());

        // Set filename
        imageData.setName(imageFile.getName());

        // Set mimetype
        String imageExtension = MimeTypeMap.getFileExtensionFromUrl(imageFile.getAbsolutePath().replace(" ", "_"));
        imageData.setMimeType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(imageExtension));

        // Set content
        FileInputStream is = null;
        try {
            is = new FileInputStream(imageFile);

            imageData.setContent(getBytes(is));
        } finally {
            if(is != null) is.close();
        }

        return imageData;
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendPostCompleted(SendPostCallCompletedEvent event) {
        if(event.getCode() == HttpURLConnection.HTTP_OK) {
            screen.onSendPostSuccess();
        } else {
            screen.onSendPostFailure();
        }
        screen.stopLoading();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetCategoriesCompleted(GetCategoriesCallCompletedEvent event) {
        if(event.getCode() == HttpURLConnection.HTTP_OK) {
            screen.onGetCategoriesSuccess(event.getResponse().getCategories());
        } else {
            screen.onGetCategoriesFailure();
        }
        screen.stopLoading();
    }
}
