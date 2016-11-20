package hu.bme.onlab.ui.details;

import hu.bme.onlab.model.post.GetPostResponse;
import hu.bme.onlab.ui.ScreenWithLoader;

public interface DetailsScreen extends ScreenWithLoader {
    public void onGetPostSuccess(GetPostResponse getPostResponse);
}
