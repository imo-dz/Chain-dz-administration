package com.example.chaindzadministration.Models;

import java.io.Serializable;
import java.util.List;

public class ProductLot implements Serializable {
    private String id;
    private String productBarCode;
    private String productLot;
    private String productId;
    private List<Stock>stockList;

    public ProductLot() {
    }

    public ProductLot(String id, String productBarCode, String productLot, String productId, List<Stock> stockList) {
        this.id = id;
        this.productBarCode = productBarCode;
        this.productLot = productLot;
        this.productId = productId;
        this.stockList = stockList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductBarCode() {
        return productBarCode;
    }

    public void setProductBarCode(String productBarCode) {
        this.productBarCode = productBarCode;
    }

    public String getProductLot() {
        return productLot;
    }

    public void setProductLot(String productLot) {
        this.productLot = productLot;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    @Override
    public String toString() {
        return "ProductLot{" +
                "id='" + id + '\'' +
                ", productBarCode='" + productBarCode + '\'' +
                ", productLot='" + productLot + '\'' +
                ", productId='" + productId + '\'' +
                ", stockList=" + stockList +
                '}';
    }
}
