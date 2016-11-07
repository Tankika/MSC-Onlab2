package hu.bme.onlab.ui.main;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import hu.bme.onlab.interactor.post.PostInteractor;
import hu.bme.onlab.interactor.post.event.ListPostsCallCompletedEvent;
import hu.bme.onlab.ui.Presenter;

public class MainPresenter extends Presenter<MainScreen> {

    private static MainPresenter instance;

    private PostInteractor postInteractor;

    private int page = 1;
    private int pageSize = 5;

    private MainPresenter() {
        postInteractor = new PostInteractor();
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

    public void listPosts() {
        postInteractor.listPosts(page, pageSize);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onListPostsCompleted(ListPostsCallCompletedEvent event) {
        screen.refreshPostList(event.getResponse().getPosts());
    }


}
