package hu.bme.onlab.model.post;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "")
public class GetCategoriesData implements Serializable {
    @SerializedName("id")
    private Long id;
    @SerializedName("name")
    private String name;

    @ApiModelProperty(value = "")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty(value = "")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetCategoriesData)) return false;

        GetCategoriesData that = (GetCategoriesData) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GetCategoriesData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
