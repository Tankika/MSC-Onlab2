package hu.bme.onlab.ui.signup;

import org.greenrobot.eventbus.EventBus;

import hu.bme.onlab.interactor.user.UserInteractor;
import hu.bme.onlab.ui.Presenter;
import hu.bme.onlab.ui.login.LoginPresenter;
import hu.bme.onlab.ui.login.LoginScreen;

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
}
