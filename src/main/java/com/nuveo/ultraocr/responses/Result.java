package com.nuveo.ultraocr.responses;

import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("Document")
    private Object document;

    @SerializedName("Quantity")
    private int quantity;

    @SerializedName("Time")
    private String time;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Object getDocument() {
        return document;
    }

    public void setDocument(Object document) {
        this.document = document;
    }
}