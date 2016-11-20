package hu.bme.onlab.network.post;

import hu.bme.onlab.model.post.GetPostResponse;
import hu.bme.onlab.model.post.ListPostsRequest;
import hu.bme.onlab.model.post.ListPostsResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PostApi {

    @POST("post/listPosts")
    public Call<ListPostsResponse> listPosts(@Body ListPostsRequest listPostsRequest);

    @GET("post/getPost/{id}")
    public Call<GetPostResponse> getPost(@Path("id") int postId);
}
