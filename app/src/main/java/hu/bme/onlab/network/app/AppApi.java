package hu.bme.onlab.network.app;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AppApi {

    @GET("/")
    public Call<Void> init();
}
