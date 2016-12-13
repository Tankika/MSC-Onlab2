package hu.bme.onlab.ui.filter;

import java.util.List;

import hu.bme.onlab.model.post.GetCategoriesData;
import hu.bme.onlab.ui.common.ScreenWithLoader;

public interface FilterScreen extends ScreenWithLoader {
    void onGetCategoriesSuccess(List<GetCategoriesData> categories);

    void onGetCategoriesFailure();
}
