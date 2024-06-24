package com.example.chaindzadministration.Models;

import java.io.Serializable;

public class Stock implements Serializable {
    private String id;
    private String stockMId;
    private String name;
    private long inTimeStamp;
    private long outTimeStamp;
    private boolean isOut;
    private String address;

    public Stock() {
    }

    public Stock(String id, String stockMId, String name, long inTimeStamp, long outTimeStamp, boolean isOut, String address) {
        this.id = id;
        this.stockMId = stockMId;
        this.name = name;
        this.inTimeStamp = inTimeStamp;
        this.outTimeStamp = outTimeStamp;
        this.isOut = isOut;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStockMId() {
        return stockMId;
    }

    public void setStockMId(String stockMId) {
        this.stockMId = stockMId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getInTimeStamp() {
        return inTimeStamp;
    }

    public void setInTimeStamp(long inTimeStamp) {
        this.inTimeStamp = inTimeStamp;
    }

    public long getOutTimeStamp() {
        return outTimeStamp;
    }

    public void setOutTimeStamp(long outTimeStamp) {
        this.outTimeStamp = outTimeStamp;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id='" + id + '\'' +
                ", stockMId='" + stockMId + '\'' +
                ", name='" + name + '\'' +
                ", inTimeStamp=" + inTimeStamp +
                ", outTimeStamp=" + outTimeStamp +
                ", isOut=" + isOut +
                ", address='" + address + '\'' +
                '}';
    }
}
