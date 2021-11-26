package kth.jjve.memeolise;
/*
This class stores the preferences and is the class
that will be serialised in the serialiser.
 */

import java.io.Serializable;

public class Preferences implements Serializable {
    private final int pTheme;
    private final int pVoice;
    private final boolean pAudio;
    private final boolean pVisual;
    private final int pEventInterval;
    private final int pNumberofEvents;
    private final int pValueofN;

    public Preferences(int theme, int voice, boolean audio, boolean visual,
                       double eventInterval, int numberofEvents, int valueofN){
        pTheme = theme;
        pVoice = voice;
        pAudio = audio;
        pVisual = visual;
        double intermediate = eventInterval * 1000;
        pEventInterval = (int) intermediate;
        pNumberofEvents = numberofEvents;
        pValueofN = valueofN;
    }

    public int getTheme(){return pTheme;}
    public int getVoice(){return pVoice;}
    public boolean getAudio(){return pAudio;}
    public boolean getVisual(){return pVisual;}
    public int getEventInterval(){return pEventInterval;}
    public int getNumberofEvents(){return pNumberofEvents;}
    public int getValueofN(){return pValueofN;}
}
