package hu.bme.onlab.interactor.main;

import android.util.Base64;

import hu.bme.onlab.model.User;
import hu.bme.onlab.network.NetworkConfig;
import hu.bme.onlab.network.RetrofitFactory;
import hu.bme.onlab.network.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserInteractor {

    private UserApi userApi;

    public UserInteractor() {
        userApi = RetrofitFactory.createRetrofit("user/").create(UserApi.class);
    }

    public void getUser() {
        String authorization = "Basic " + Base64.encodeToString("user@test.hu:password".getBytes(), Base64.NO_WRAP);
        userApi.getUser(authorization).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
