package hu.bme.onlab.network.user;

import hu.bme.onlab.model.post.ListPostsRequest;
import hu.bme.onlab.model.user.SignupRequest;
import hu.bme.onlab.model.user.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserApi {

    @GET("user/user")
    public Call<User> getUser(@Header("Authorization") String authorization);

    @POST("user/signup")
    public Call<Void> signUp(@Body SignupRequest signupRequest);

    @POST("/logout")
    public Call<Void> logout();
}
