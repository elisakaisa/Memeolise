package kth.jjve.memeolise.game;
/*
Jitse van Esch & Elisa Perini
2.12.21
 */

import java.io.Serializable;

public class Results implements Serializable {
    private String rName;
    private int rScore;
    private int rMaxscore;

    public void setResultName(String name){
        rName = name;
    }

    public void setScore(int score){
        rScore = score;
    }

    public void setMaxscore(int maxscore){
        rMaxscore = maxscore;
    }

    public String getResultName(){ return rName;}

    public int getScore(){return rScore;}

    public int getMaxscore(){return rMaxscore;}
}
