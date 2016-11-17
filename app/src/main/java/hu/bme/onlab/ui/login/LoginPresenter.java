package hu.bme.onlab.ui.login;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.HttpURLConnection;

import hu.bme.onlab.interactor.user.event.LoginCompletedEvent;
import hu.bme.onlab.interactor.user.UserInteractor;
import hu.bme.onlab.network.NetworkSessionStore;
import hu.bme.onlab.ui.Presenter;

public class LoginPresenter extends Presenter<LoginScreen> {

    private static LoginPresenter instance;

    private UserInteractor userInteractor;

    private LoginPresenter() {
        userInteractor = new UserInteractor();
    }

    public static LoginPresenter getInstance() {
        if(instance == null) {
            instance = new LoginPresenter();
        }
        return instance;
    }

    @Override
    public void attachScreen(LoginScreen screen) {
        super.attachScreen(screen);
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }

    public void loginUser(String email, String password) {
        screen.startLoading();
        userInteractor.login(email, password);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginCompleted(LoginCompletedEvent event) {
        if(event.getCode() == HttpURLConnection.HTTP_OK) {
            NetworkSessionStore.setUser(event.getResponse());
            screen.stopLoading();
            screen.onLoginSuccess();
        }
    }

}
