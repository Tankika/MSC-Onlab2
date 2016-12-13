package hu.bme.onlab.ui.common;

import hu.bme.onlab.model.post.GetCategoriesData;

public class CategorySpinnerElement {
    private GetCategoriesData getCategoriesData;

    public CategorySpinnerElement(GetCategoriesData getCategoriesData) {
        this.getCategoriesData = getCategoriesData;
    }

    public GetCategoriesData getGetCategoriesData() {
        return getCategoriesData;
    }

    @Override
    public String toString() {
        return getCategoriesData.getName();
    }
}
