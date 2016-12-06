package hu.bme.onlab.model.post;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "")
public class GetPostResponse implements Serializable {

    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("priceMin")
    private Integer priceMin;
    @SerializedName("priceMax")
    private Integer priceMax;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("creationDateTime")
    private BigDecimal creationDateTime = null;
    @SerializedName("categoryName")
    private String categoryName;
    @SerializedName("postalCode")
    private String postalCode;
    @SerializedName("city")
    private String city;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("imageIds")
    private List<Long> imageIds;

    @ApiModelProperty(value = "")
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @ApiModelProperty(value = "")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @ApiModelProperty(value = "")
    public Integer getPriceMin() {
        return priceMin;
    }
    public void setPriceMin(Integer priceMin) {
        this.priceMin = priceMin;
    }

    @ApiModelProperty(value = "")
    public Integer getPriceMax() {
        return priceMax;
    }
    public void setPriceMax(Integer priceMax) {
        this.priceMax = priceMax;
    }

    @ApiModelProperty(value = "")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @ApiModelProperty(value = "")
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @ApiModelProperty(value = "")
    public BigDecimal getCreationDateTime() {
        return creationDateTime;
    }
    public void setCreationDateTime(BigDecimal creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    @ApiModelProperty(value = "")
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @ApiModelProperty(value = "")
    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @ApiModelProperty(value = "")
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    @ApiModelProperty(value = "")
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @ApiModelProperty(value = "")
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @ApiModelProperty(value = "")
    public List<Long> getImageIds() {
        return imageIds;
    }
    public void setImageIds(List<Long> imageIds) {
        this.imageIds = imageIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetPostResponse)) return false;

        GetPostResponse that = (GetPostResponse) o;

        if (Double.compare(that.latitude, latitude) != 0) return false;
        if (Double.compare(that.longitude, longitude) != 0) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (priceMin != null ? !priceMin.equals(that.priceMin) : that.priceMin != null)
            return false;
        if (priceMax != null ? !priceMax.equals(that.priceMax) : that.priceMax != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (creationDateTime != null ? !creationDateTime.equals(that.creationDateTime) : that.creationDateTime != null)
            return false;
        if (categoryName != null ? !categoryName.equals(that.categoryName) : that.categoryName != null)
            return false;
        if (postalCode != null ? !postalCode.equals(that.postalCode) : that.postalCode != null)
            return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        return imageIds != null ? imageIds.equals(that.imageIds) : that.imageIds == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (priceMin != null ? priceMin.hashCode() : 0);
        result = 31 * result + (priceMax != null ? priceMax.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (creationDateTime != null ? creationDateTime.hashCode() : 0);
        result = 31 * result + (categoryName != null ? categoryName.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (imageIds != null ? imageIds.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GetPostResponse{\n" +
                "title='" + title + '\'' + "\n" +
                ", description='" + description + '\'' + "\n" +
                ", priceMin=" + priceMin + "\n" +
                ", priceMax=" + priceMax + "\n" +
                ", name='" + name + '\'' + "\n" +
                ", phone='" + phone + '\'' + "\n" +
                ", creationDateTime=" + creationDateTime + "\n" +
                ", categoryName='" + categoryName + '\'' + "\n" +
                ", postalCode='" + postalCode + '\'' + "\n" +
                ", city='" + city + '\'' + "\n" +
                ", latitude=" + latitude + "\n" +
                ", longitude=" + longitude + "\n" +
                ", imageIds=" + imageIds + "\n" +
                "}\n";
    }
}
