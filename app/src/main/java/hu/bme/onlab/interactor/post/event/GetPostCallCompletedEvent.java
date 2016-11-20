package hu.bme.onlab.interactor.post.event;

import hu.bme.onlab.interactor.ApiCallCompletedEvent;
import hu.bme.onlab.model.post.GetPostResponse;
import hu.bme.onlab.model.post.ListPostsResponse;

public class GetPostCallCompletedEvent extends ApiCallCompletedEvent<GetPostResponse> {

    public GetPostCallCompletedEvent(int code, GetPostResponse response) {
        super(code, response);
    }

    public GetPostCallCompletedEvent(Throwable throwable) {
        super(throwable);
    }
}
