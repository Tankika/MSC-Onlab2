package hu.bme.onlab.model.post;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "")
public class GetCategoriesResponse {
    @SerializedName("categories")
    private List<GetCategoriesData> categories;

    {
        categories = new ArrayList<>();
    }

    @ApiModelProperty(value = "")
    public List<GetCategoriesData> getCategories() {
        return categories;
    }
    public void setCategories(List<GetCategoriesData> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetCategoriesResponse)) return false;

        GetCategoriesResponse that = (GetCategoriesResponse) o;

        return categories != null ? categories.equals(that.categories) : that.categories == null;

    }

    @Override
    public int hashCode() {
        return categories != null ? categories.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "GetCategoriesResponse{" +
                "categories=" + categories +
                '}';
    }
}
