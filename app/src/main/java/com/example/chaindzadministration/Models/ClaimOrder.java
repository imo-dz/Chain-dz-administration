package com.example.chaindzadministration.Models;

import java.io.Serializable;

public class ClaimOrder implements Serializable {
    private String id;
    private String clientId;
    private String barCodeProduct;
    private String lot;
    private String productId;
    private String ProductionManagerId;
    private String clientName;
    private String clientPhone;
    private String productName;
    private String productionId;
    private String productImgLink;
    private long timeStamp;

    public ClaimOrder() {
    }

    public ClaimOrder(String id, String clientId, String barCodeProduct, String lot, String productId, String productionManagerId, String clientName, String clientPhone, String productName, String productionId, String productImgLink, long timeStamp) {
        this.id = id;
        this.clientId = clientId;
        this.barCodeProduct = barCodeProduct;
        this.lot = lot;
        this.productId = productId;
        ProductionManagerId = productionManagerId;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.productName = productName;
        this.productionId = productionId;
        this.productImgLink = productImgLink;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getBarCodeProduct() {
        return barCodeProduct;
    }

    public void setBarCodeProduct(String barCodeProduct) {
        this.barCodeProduct = barCodeProduct;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductionManagerId() {
        return ProductionManagerId;
    }

    public void setProductionManagerId(String productionManagerId) {
        ProductionManagerId = productionManagerId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductionId() {
        return productionId;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

    public String getProductImgLink() {
        return productImgLink;
    }

    public void setProductImgLink(String productImgLink) {
        this.productImgLink = productImgLink;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "ClaimOrder{" +
                "id='" + id + '\'' +
                ", clientId='" + clientId + '\'' +
                ", barCodeProduct='" + barCodeProduct + '\'' +
                ", lot='" + lot + '\'' +
                ", productId='" + productId + '\'' +
                ", ProductionManagerId='" + ProductionManagerId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientPhone='" + clientPhone + '\'' +
                ", productName='" + productName + '\'' +
                ", productionId='" + productionId + '\'' +
                ", productImgLink='" + productImgLink + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
