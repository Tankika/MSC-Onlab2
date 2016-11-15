package hu.bme.onlab.interactor.post.event;

import hu.bme.onlab.interactor.ApiCallCompletedEvent;
import hu.bme.onlab.model.post.ListPostsResponse;

public class ListPostsCallCompletedEvent extends ApiCallCompletedEvent<ListPostsResponse> {

    public ListPostsCallCompletedEvent(int code, ListPostsResponse response) {
        super(code, response);
    }

    public ListPostsCallCompletedEvent(Throwable throwable) {
        super(throwable);
    }
}
