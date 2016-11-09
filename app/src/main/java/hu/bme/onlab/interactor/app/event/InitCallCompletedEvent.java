package hu.bme.onlab.interactor.app.event;

import hu.bme.onlab.interactor.ApiCallCompletedEvent;

public class InitCallCompletedEvent extends ApiCallCompletedEvent<Void> {

    public InitCallCompletedEvent(Throwable throwable) {
        super(throwable);
    }

    public InitCallCompletedEvent(int code, Void response) {
        super(code, response);
    }
}
