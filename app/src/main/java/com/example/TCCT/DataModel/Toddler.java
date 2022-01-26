package com.example.TCCT.DataModel;

import java.util.ArrayList;

public class Toddler {

    String imgUri;
    ArrayList<String> basicData, detailData;

    public Toddler() {
    }

    public Toddler(String imgUri, ArrayList<String> basicData, ArrayList<String> detailData) {
        this.imgUri = imgUri;
        this.basicData = basicData;
        this.detailData = detailData;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public ArrayList<String> getBasicData() {
        return basicData;
    }

    public void setBasicData(ArrayList<String> basicData) {
        this.basicData = basicData;
    }

    public ArrayList<String> getDetailData() {
        return detailData;
    }

    public void setDetailData(ArrayList<String> detailData) {
        this.detailData = detailData;
    }
}
