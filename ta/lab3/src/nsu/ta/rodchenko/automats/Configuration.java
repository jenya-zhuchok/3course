package nsu.ta.rodchenko.automats;

import nsu.ta.rodchenko.literals.Literal;

import java.util.ArrayList;

public class Configuration {
    private Statement curState;
    private ArrayList<Literal> chain;
    private ArrayList<Literal> clip = new ArrayList<>();

    public Configuration(Statement s, ArrayList<Literal> chain, Literal symbol){
        curState = s;
        this.chain = chain;
        clip.add(symbol);
    }
}
