package kth.jjve.memeolise.game;

import java.io.Serializable;
import java.util.List;

public class ResultStorage implements Serializable {
    private List<Results> sResultList;

    public ResultStorage(List<Results> resultList){
        sResultList = resultList;
    }

    public List<Results> getResultList(){return sResultList;}
}

