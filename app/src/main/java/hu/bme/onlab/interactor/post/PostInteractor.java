package hu.bme.onlab.interactor.post;

import android.net.Uri;
import android.webkit.MimeTypeMap;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import hu.bme.onlab.interactor.post.event.GetPostCallCompletedEvent;
import hu.bme.onlab.interactor.post.event.ListPostsCallCompletedEvent;
import hu.bme.onlab.model.post.GetPostResponse;
import hu.bme.onlab.model.post.ImageData;
import hu.bme.onlab.model.post.ListPostsRequest;
import hu.bme.onlab.model.post.ListPostsResponse;
import hu.bme.onlab.model.post.SendPostData;
import hu.bme.onlab.network.CustomRetrofitFactory;
import hu.bme.onlab.network.post.PostApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostInteractor {

    private PostApi postApi;

    public PostInteractor() {
        postApi = CustomRetrofitFactory.createRetrofit().create(PostApi.class);
    }

    public void listPosts(int page, int pageSize) {
        ListPostsRequest listPostsRequest = new ListPostsRequest();
        listPostsRequest.setPage(BigDecimal.valueOf(page));
        listPostsRequest.setPageSize(BigDecimal.valueOf(pageSize));

        postApi.listPosts(listPostsRequest).enqueue(new Callback<ListPostsResponse>() {
            @Override
            public void onResponse(Call<ListPostsResponse> call, Response<ListPostsResponse> response) {
                ListPostsCallCompletedEvent event = new ListPostsCallCompletedEvent(response.code(), response.body());
                EventBus.getDefault().post(event);
            }

            @Override
            public void onFailure(Call<ListPostsResponse> call, Throwable t) {
                ListPostsCallCompletedEvent event = new ListPostsCallCompletedEvent(t);
                EventBus.getDefault().post(event);
            }
        });
    }

    public void getPost(int postId) {
        postApi.getPost(postId).enqueue(new Callback<GetPostResponse>() {
            @Override
            public void onResponse(Call<GetPostResponse> call, Response<GetPostResponse> response) {
                GetPostCallCompletedEvent event = new GetPostCallCompletedEvent(response.code(), response.body());
                EventBus.getDefault().post(event);
            }

            @Override
            public void onFailure(Call<GetPostResponse> call, Throwable t) {
                GetPostCallCompletedEvent event = new GetPostCallCompletedEvent(t);
                EventBus.getDefault().post(event);
            }
        });
    }

    public void sendPost(SendPostData sendPostData) {
        List<MultipartBody.Part> images = new ArrayList<>();
        for (int i = 0; i < sendPostData.getImageDataList().size(); i++) {
            ImageData imageData = sendPostData.getImageDataList().get(i);

            MediaType mediaType = MediaType.parse(imageData.getMimeType());
            RequestBody imageRequestBody = RequestBody.create(mediaType, imageData.getContent());

            MultipartBody.Part requestPart = MultipartBody.Part.createFormData("file[" + i + "]", imageData.getName(), imageRequestBody);

            images.add(requestPart);
        }

        Call<Void> call = postApi.sendPost(
            RequestBody.create(MediaType.parse("multipart/form-data"), sendPostData.getTitle()),
            RequestBody.create(MediaType.parse("multipart/form-data"), sendPostData.getDescription()),
            RequestBody.create(MediaType.parse("multipart/form-data"), sendPostData.getPostalCode()),
            RequestBody.create(MediaType.parse("multipart/form-data"), sendPostData.getPriceMin()),
            RequestBody.create(MediaType.parse("multipart/form-data"), sendPostData.getPriceMax()),
            RequestBody.create(MediaType.parse("multipart/form-data"), sendPostData.getCategory()),
            RequestBody.create(MediaType.parse("multipart/form-data"), sendPostData.getName()),
            RequestBody.create(MediaType.parse("multipart/form-data"), sendPostData.getPhone()),
            images
        );

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("");
            }
        });
    }
}
