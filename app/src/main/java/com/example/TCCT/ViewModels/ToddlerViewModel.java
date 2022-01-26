package com.example.TCCT.ViewModels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ToddlerViewModel extends ViewModel {

    private final MutableLiveData<Uri> imgUri = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> formData = new MutableLiveData<>();
    private final MutableLiveData<String> relationship = new MutableLiveData<>();
    private final MutableLiveData<String> time = new MutableLiveData<>();
    private final MutableLiveData<String> devLevel = new MutableLiveData<>();

    public void setImgUri(Uri item){
        imgUri.setValue(item);
    }
    public void setFormData(ArrayList<String> item){
        formData.setValue(item);
    }
    public void setRelationship(String item){
        relationship.setValue(item);
    }
    public void setTime(String item){
        time.setValue(item);
    }
    public void setDevLevel(String item) {
        devLevel.setValue(item);
    }

    public LiveData<Uri> getImgUri() {
        return imgUri;
    }
    public LiveData<ArrayList<String>> getFormData() {
        return formData;
    }
    public LiveData<String> getRelationship() {
        return relationship;
    }
    public LiveData<String> getTime() {
        return time;
    }
    public LiveData<String> getDevLevel() {
        return devLevel;
    }
}
