package hu.bme.onlab.ui.newpost;

import java.util.List;

import hu.bme.onlab.model.post.GetCategoriesData;
import hu.bme.onlab.ui.ScreenWithLoader;

public interface NewPostScreen extends ScreenWithLoader {

    void onSendPostSuccess();

    void onSendPostFailure();

    void onGetCategoriesSuccess(List<GetCategoriesData> categories);

    void onGetCategoriesFailure();
}
