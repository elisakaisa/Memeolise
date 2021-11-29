package kth.jjve.memeolise.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameLogic {
    private final List<String> letterList = Arrays.asList("a", "b"); //Todo: decide how many letters we want to have
    private final List<Integer> positionList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    private List<String> usedLetters = new ArrayList<>();
    private List<Integer> usedPositions = new ArrayList<>();

    public void reset(){
        List<String> usedLetters = new ArrayList<>();
        List<Integer> usedPositions = new ArrayList<>();
    }

    public String returnRandomLetter(){
        // Function that takes a random letter from the letter list
        // Returns the letter and appends it to usedLetters
        Random rand = new Random();
        String letter = letterList.get(rand.nextInt(letterList.size()));
//        String letter = "a";
        usedLetters.add(letter);

        return letter;
    }

    public int returnRandomPosition(){
        // Function that takes a random position from the position list
        // Returns the position and appends it to usedPositions
        Random rand = new Random();
        int position = positionList.get(rand.nextInt(positionList.size()));
        usedPositions.add(position);

        return position;
    }

    public boolean checkVisualScored(boolean visualPress, int n, int eventNo){
        //Function to check if the user has scored on the visual
        return (usedPositions.get(eventNo-1).equals(usedPositions.get(eventNo - n -1)) && visualPress);
    }

    public boolean checkAudioScored(int n, int eventNo){
        //Function to check if the user has scored on the audio
        String a = usedLetters.get(eventNo-1);
        String b = usedLetters.get(eventNo - n - 1);
        return (a.equals(b));
    }

    public boolean checkCombinedScored(boolean audioPress, boolean visualPress, int n, int eventNo){
        //Function to check if the user has scored on both audio and visual
        return (usedLetters.get(eventNo-1).equals(usedLetters.get(eventNo - n - 1)) && audioPress &&
                usedPositions.get(eventNo-1).equals(usedPositions.get(eventNo - n -1)) && visualPress);
    }

    public static GameLogic getInstance() {
        if (gameLogic == null){
            gameLogic = new GameLogic();
        }
        return gameLogic;
    }

    private static GameLogic gameLogic=null;

    private GameLogic(){
        reset();
    }

}
