package hu.bme.onlab.ui.login;

import hu.bme.onlab.ui.common.ScreenWithLoader;

public interface LoginScreen extends ScreenWithLoader {
    void onLoginSuccess();

    void onLoginFail();

    void onLoginError();
}
