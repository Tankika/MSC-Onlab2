package hu.bme.onlab.model.post;

import java.util.ArrayList;
import java.util.List;

public class SendPostData {
    private String title;
    private String description;
    private String postalCode;
    private String priceMin;
    private String priceMax;
    private String category;
    private String name;
    private String phone;
    private List<ImageData> imageDataList;

    {
        imageDataList = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(String priceMin) {
        this.priceMin = priceMin;
    }

    public String getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(String priceMax) {
        this.priceMax = priceMax;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<ImageData> getImageDataList() {
        return imageDataList;
    }

    public void setImageDataList(List<ImageData> imageDataList) {
        this.imageDataList = imageDataList;
    }
}
