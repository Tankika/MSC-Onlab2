package hu.bme.onlab.network;

import hu.bme.onlab.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserApi {

    @GET("user")
    public Call<User> getUser(@Header("Authorization") String authorization);
}
