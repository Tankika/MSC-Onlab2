package hu.bme.onlab.interactor.user;

import hu.bme.onlab.interactor.ApiCallCompletedEvent;
import hu.bme.onlab.model.user.User;

public class LoginCompletedEvent extends ApiCallCompletedEvent<User> {

    public LoginCompletedEvent(int code, User response) {
        super(code, response);
    }

    public LoginCompletedEvent(Throwable throwable) {
        super(throwable);
    }
}
