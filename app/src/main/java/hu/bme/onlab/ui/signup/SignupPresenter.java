package hu.bme.onlab.ui.signup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.HttpURLConnection;

import hu.bme.onlab.interactor.user.UserInteractor;
import hu.bme.onlab.interactor.user.event.SignupCompletedEvent;
import hu.bme.onlab.ui.Presenter;

public class SignupPresenter extends Presenter<SignupScreen> {

    private static SignupPresenter instance;

    private UserInteractor userInteractor;

    private SignupPresenter() {
        userInteractor = new UserInteractor();
    }

    public static SignupPresenter getInstance() {
        if(instance == null) {
            instance = new SignupPresenter();
        }
        return instance;
    }

    public void signUp(String email, String password) {
        screen.startLoading();
        userInteractor.signUp(email, password);
    }

    @Override
    public void attachScreen(SignupScreen screen) {
        super.attachScreen(screen);
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutCompleted(SignupCompletedEvent event) {
        if(event.getCode() == HttpURLConnection.HTTP_OK) {
            screen.onSignupSuccess();
        }
        screen.stopLoading();
    }
}
