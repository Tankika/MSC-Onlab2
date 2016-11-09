package hu.bme.onlab.network.app;

import hu.bme.onlab.model.ListPostsRequest;
import hu.bme.onlab.model.ListPostsResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AppApi {

    @GET("/")
    public Call<Void> init();
}
