package com.example.TCCT.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ChecklistViewModel extends ViewModel {

    private final MutableLiveData<Integer> topicListen = new MutableLiveData<>();
    private final MutableLiveData<Integer> topicTalk = new MutableLiveData<>();
    private final MutableLiveData<Integer> monthIndex = new MutableLiveData<>();
    private final MutableLiveData<Integer> listenScore = new MutableLiveData<>();
    private final MutableLiveData<Integer> talkScore = new MutableLiveData<>();
    private final MutableLiveData<Integer> listenScoreSum = new MutableLiveData<>();


    public void setTopicListen(Integer item){
        topicListen.setValue(item);
    }
    public void setTopicTalk(Integer item){
        topicTalk.setValue(item);
    }
    public void setMonthIndex(Integer item){
        monthIndex.setValue(item);
    }
    public void setListenScore(Integer item){
        listenScore.setValue(item);
    }
    public void setTalkScore(Integer item){
        talkScore.setValue(item);
    }
    public void setListenScoreSum(Integer item){
        listenScoreSum.setValue(item);
    }

    public LiveData<Integer> getTopicListen() {
        return topicListen;
    }
    public LiveData<Integer> getTopicTalk() {
        return topicTalk;
    }
    public LiveData<Integer> getMonthIndex() {
        return monthIndex;
    }
    public LiveData<Integer> getListenScore() {
        return listenScore;
    }
    public LiveData<Integer> getTalkScore() {
        return talkScore;
    }
    public LiveData<Integer> getListenScoreSum() {
        return listenScoreSum;
    }

}
