package hu.bme.onlab.interactor.post.event;

import hu.bme.onlab.interactor.ApiCallCompletedEvent;

public class SendPostCallCompletedEvent extends ApiCallCompletedEvent<Void> {

    public SendPostCallCompletedEvent(int code, Void response) {
        super(code, response);
    }

    public SendPostCallCompletedEvent(Throwable throwable) {
        super(throwable);
    }
}
