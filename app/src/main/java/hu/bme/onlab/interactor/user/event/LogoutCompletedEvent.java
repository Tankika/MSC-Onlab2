package hu.bme.onlab.interactor.user.event;

import hu.bme.onlab.interactor.ApiCallCompletedEvent;

public class LogoutCompletedEvent extends ApiCallCompletedEvent<Void> {
    public LogoutCompletedEvent(int code, Void response) {
        super(code, response);
    }

    public LogoutCompletedEvent(Throwable throwable) {
        super(throwable);
    }
}
