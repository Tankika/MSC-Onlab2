package hu.bme.onlab.ui.main;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.HttpURLConnection;

import hu.bme.onlab.interactor.app.AppInteractor;
import hu.bme.onlab.interactor.app.event.InitCallCompletedEvent;
import hu.bme.onlab.interactor.post.PostInteractor;
import hu.bme.onlab.interactor.post.event.ListPostsCallCompletedEvent;
import hu.bme.onlab.interactor.user.UserInteractor;
import hu.bme.onlab.interactor.user.event.LogoutCompletedEvent;
import hu.bme.onlab.model.post.ListPostsResponse;
import hu.bme.onlab.network.NetworkSessionStore;
import hu.bme.onlab.ui.Presenter;

public class MainPresenter extends Presenter<MainScreen> {

    private static MainPresenter instance;

    private AppInteractor appInteractor;
    private PostInteractor postInteractor;
    private UserInteractor userInteractor;

    private int page = 0;
    private int pageSize = 5;

    private MainPresenter() {
        appInteractor = new AppInteractor();
        postInteractor = new PostInteractor();
        userInteractor = new UserInteractor();
    }

    public static MainPresenter getInstance() {
        if(instance == null) {
            instance = new MainPresenter();
        }
        return instance;
    }

    @Override
    public void attachScreen(MainScreen screen) {
        super.attachScreen(screen);
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void init() {
        screen.startLoading();
        appInteractor.init();
    }

    public void reloadPosts() {
        screen.startLoading();
        screen.clearPostList();
        page = 1;
        postInteractor.listPosts(page, pageSize);
    }

    public void loadPosts() {
        screen.startLoading();
        page++;
        postInteractor.listPosts(page, pageSize);
    }

    public void logout() {
        screen.startLoading();
        userInteractor.logout();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInitCompleted(InitCallCompletedEvent event) {
        if(event.getCode() == HttpURLConnection.HTTP_OK) {
            loadPosts();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onListPostsCompleted(ListPostsCallCompletedEvent event) {
        ListPostsResponse response = event.getResponse();

        if(response != null) {
            screen.refreshPostList(response.getPosts());
        }
        screen.stopLoading();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutCompleted(LogoutCompletedEvent event) {
        NetworkSessionStore.setUser(null);
        screen.setMenuVisibilities();
        screen.stopLoading();
    }

}
