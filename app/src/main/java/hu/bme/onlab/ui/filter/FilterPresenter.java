package hu.bme.onlab.ui.filter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.HttpURLConnection;

import hu.bme.onlab.interactor.post.PostInteractor;
import hu.bme.onlab.interactor.post.event.GetCategoriesCallCompletedEvent;
import hu.bme.onlab.ui.common.Presenter;

public class FilterPresenter extends Presenter<FilterScreen> {

    private static FilterPresenter instance;

    private PostInteractor postInteractor;

    private FilterPresenter() {
        postInteractor = new PostInteractor();
    }

    public static FilterPresenter getInstance() {
        if(instance == null) {
            instance = new FilterPresenter();
        }
        return instance;
    }

    @Override
    public void attachScreen(FilterScreen screen) {
        super.attachScreen(screen);
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }

    public void getCategories() {
        screen.startLoading();
        postInteractor.getCategories();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetCategoriesCompleted(GetCategoriesCallCompletedEvent event) {
        if(screen == null) {
            return;
        }

        if(event.getCode() == HttpURLConnection.HTTP_OK) {
            screen.onGetCategoriesSuccess(event.getResponse().getCategories());
        } else {
            screen.onGetCategoriesFailure();
        }
        screen.stopLoading();
    }

}
