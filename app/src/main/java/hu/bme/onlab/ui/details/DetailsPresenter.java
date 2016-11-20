package hu.bme.onlab.ui.details;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.HttpURLConnection;

import hu.bme.onlab.interactor.post.PostInteractor;
import hu.bme.onlab.interactor.post.event.GetPostCallCompletedEvent;
import hu.bme.onlab.ui.Presenter;

public class DetailsPresenter extends Presenter<DetailsScreen> {

    private static DetailsPresenter instance;

    private PostInteractor postInteractor;

    private DetailsPresenter() {
        postInteractor = new PostInteractor();
    }

    public static DetailsPresenter getInstance() {
        if(instance == null) {
            instance = new DetailsPresenter();
        }
        return instance;
    }

    public void getPost(int postId) {
        screen.startLoading();
        postInteractor.getPost(postId);
    }

    @Override
    public void attachScreen(DetailsScreen screen) {
        super.attachScreen(screen);
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutCompleted(GetPostCallCompletedEvent event) {
        if(event.getCode() == HttpURLConnection.HTTP_OK) {
            screen.onGetPostSuccess(event.getResponse());
        }
        screen.stopLoading();
    }
}
