package hu.bme.onlab.interactor.app;

import org.greenrobot.eventbus.EventBus;

import hu.bme.onlab.interactor.app.event.InitCallCompletedEvent;
import hu.bme.onlab.network.RetrofitFactory;
import hu.bme.onlab.network.app.AppApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppInteractor {

    AppApi appApi;

    public AppInteractor() {
        appApi = RetrofitFactory.createRetrofit("").create(AppApi.class);
    }

    public void init() {
        appApi.init().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                InitCallCompletedEvent event = new InitCallCompletedEvent(response.code(), response.body());
                EventBus.getDefault().post(event);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                InitCallCompletedEvent event = new InitCallCompletedEvent(t);
                EventBus.getDefault().post(event);
            }
        });
    }
}
