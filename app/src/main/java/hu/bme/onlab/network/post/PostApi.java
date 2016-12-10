package hu.bme.onlab.network.post;

import java.util.List;

import hu.bme.onlab.model.post.GetCategoriesResponse;
import hu.bme.onlab.model.post.GetPostResponse;
import hu.bme.onlab.model.post.ListPostsRequest;
import hu.bme.onlab.model.post.ListPostsResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface PostApi {

    @POST("post/listPosts")
    public Call<ListPostsResponse> listPosts(@Body ListPostsRequest listPostsRequest);

    @GET("post/getPost/{id}")
    public Call<GetPostResponse> getPost(@Path("id") int postId);

    @GET("post/getCategories")
    public Call<GetCategoriesResponse> getCategories();

    @Multipart
    @POST("post/sendPost")
    public Call<Void> sendPost(@Part("post[title]") RequestBody title,
                               @Part("post[description]") RequestBody description,
                               @Part("post[postalCode]") RequestBody postalCode,
                               @Part("post[priceMin]") RequestBody priceMin,
                               @Part("post[priceMax]") RequestBody priceMax,
                               @Part("post[category]") RequestBody category,
                               @Part("post[name]") RequestBody name,
                               @Part("post[phone]") RequestBody phone,
                               @Part List<MultipartBody.Part> images);
}
