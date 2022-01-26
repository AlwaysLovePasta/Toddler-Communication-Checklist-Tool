package com.example.TCCT.DataModel;

import java.util.ArrayList;

public class Score {

    ArrayList<Integer> scoreSet;

    public Score() {
    }

    public Score(ArrayList<Integer> scoreSet) {
        this.scoreSet = scoreSet;
    }

    public ArrayList<Integer> getScoreSet() {
        return scoreSet;
    }

    public void setScoreSet(ArrayList<Integer> scoreSet) {
        this.scoreSet = scoreSet;
    }
}
