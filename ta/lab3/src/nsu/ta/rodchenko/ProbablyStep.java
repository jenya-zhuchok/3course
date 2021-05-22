package nsu.ta.rodchenko;

import nsu.ta.rodchenko.Rule;

import java.util.ArrayList;

public class ProbablyStep {

    private Character state = 'q';
    private ArrayList <Character> literals = new ArrayList<>();


    public ProbablyStep (Character s, ArrayList<Character> characters){
        state = s;
        literals = characters;
    }

    public ProbablyStep (Character s, Character characters){
        state = s;
        literals.add(characters);
    }

    public ProbablyStep (Character s){
        state = s;
    }

    public String toString() {
        if(literals.isEmpty()) return "(" + state +", {e})";
        return "(" + state + ", "+ Rule.char2str(literals) + ")";
    }

    public Character getState() {
        return state;
    }
}
