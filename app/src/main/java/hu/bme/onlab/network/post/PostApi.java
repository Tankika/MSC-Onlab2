package hu.bme.onlab.network.post;

import hu.bme.onlab.model.post.ListPostsRequest;
import hu.bme.onlab.model.post.ListPostsResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PostApi {

    @POST("listPosts")
    public Call<ListPostsResponse> listPosts(@Body ListPostsRequest listPostsRequest);
}
