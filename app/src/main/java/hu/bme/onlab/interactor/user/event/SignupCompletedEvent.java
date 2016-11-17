package hu.bme.onlab.interactor.user.event;

import hu.bme.onlab.interactor.ApiCallCompletedEvent;

public class SignupCompletedEvent extends ApiCallCompletedEvent<Void> {

    public SignupCompletedEvent(int code, Void response) {
        super(code, response);
    }

    public SignupCompletedEvent(Throwable throwable) {
        super(throwable);
    }
}
