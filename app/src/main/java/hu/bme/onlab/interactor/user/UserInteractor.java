package hu.bme.onlab.interactor.user;

import android.util.Base64;

import org.greenrobot.eventbus.EventBus;

import hu.bme.onlab.model.user.SignupRequest;
import hu.bme.onlab.model.user.User;
import hu.bme.onlab.network.RetrofitFactory;
import hu.bme.onlab.network.user.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInteractor {

    private UserApi userApi;

    public UserInteractor() {
        userApi = RetrofitFactory.createRetrofit("user/").create(UserApi.class);
    }

    public void login(String email, String password) {
        String authorization = "Basic " + Base64.encodeToString((email + ":" + password).getBytes(), Base64.NO_WRAP);

        userApi.getUser(authorization).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                LoginCompletedEvent event = new LoginCompletedEvent(response.code(), response.body());
                EventBus.getDefault().post(event);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                LoginCompletedEvent event = new LoginCompletedEvent(t);
                EventBus.getDefault().post(event);
            }
        });
    }

    public void signUp(String email, String password) {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(email);
        signupRequest.setPassword(password);

        userApi.signUp(signupRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
