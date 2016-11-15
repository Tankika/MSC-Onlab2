package hu.bme.onlab.interactor.post;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

import hu.bme.onlab.interactor.post.event.ListPostsCallCompletedEvent;
import hu.bme.onlab.model.post.ListPostsRequest;
import hu.bme.onlab.model.post.ListPostsResponse;
import hu.bme.onlab.network.RetrofitFactory;
import hu.bme.onlab.network.post.PostApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostInteractor {

    private PostApi postApi;

    public PostInteractor() {
        postApi = RetrofitFactory.createRetrofit("post/").create(PostApi.class);
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
}
