package com.example.chaindzadministration.Models;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name;
    private String price;
    private String barCode;
    private String description;
    private String productionId;
    private String imgLink;
    private String companyName;
    private long timeStamp;

    public Product() {
    }


    public Product(String id, String name, String price, String barCode, String description, String productionId, String imgLink, String companyName, long timeStamp) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.barCode = barCode;
        this.description = description;
        this.productionId = productionId;
        this.imgLink = imgLink;
        this.companyName = companyName;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductionId() {
        return productionId;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", barCode='" + barCode + '\'' +
                ", description='" + description + '\'' +
                ", productionId='" + productionId + '\'' +
                ", imgLink='" + imgLink + '\'' +
                ", companyName='" + companyName + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
