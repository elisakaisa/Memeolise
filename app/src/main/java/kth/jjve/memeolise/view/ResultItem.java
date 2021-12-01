package kth.jjve.memeolise.view;

import kth.jjve.memeolise.game.ResultStorage;

public class ResultItem {
    private final String rName;
    private final int rScore;
    private final int rMaxscore;

    public ResultItem(String name, int score, int maxscore){
        rName = name;
        rScore = score;
        rMaxscore = maxscore;
    }

    public String getRName(){return rName;}

    public int getRScore(){return rScore;}

    public int getrMaxscore(){return rMaxscore;}

}
