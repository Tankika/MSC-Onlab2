package hu.bme.onlab.interactor.post.event;

import hu.bme.onlab.interactor.ApiCallCompletedEvent;
import hu.bme.onlab.model.post.GetCategoriesResponse;
import hu.bme.onlab.model.post.GetPostResponse;

public class GetCategoriesCallCompletedEvent extends ApiCallCompletedEvent<GetCategoriesResponse> {

    public GetCategoriesCallCompletedEvent(int code, GetCategoriesResponse response) {
        super(code, response);
    }

    public GetCategoriesCallCompletedEvent(Throwable throwable) {
        super(throwable);
    }
}
