package com.example.TCCT.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MilestoneDetailViewModel extends ViewModel {

    private final MutableLiveData<Integer> milestoneIndex = new MutableLiveData<>();

    public void setMilestoneIndex(Integer item){
        milestoneIndex.setValue(item);
    }
    public LiveData<Integer> getMilestoneIndex() {
        return milestoneIndex;
    }
}
