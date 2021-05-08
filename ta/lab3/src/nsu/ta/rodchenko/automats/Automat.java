package nsu.ta.rodchenko.automats;

import nsu.ta.rodchenko.literals.Literal;

import java.util.ArrayList;
import java.util.HashSet;

public class Automat {

    private ArrayList<Statement> statements = new ArrayList<>(); //may be hash?
    private HashSet<Literal> alphabet = new HashSet<>();
    private HashSet<Literal> endAlphabet = new HashSet<>();
    private Statement startStatement;
    private Literal startSymbol;
    private ArrayList<Statement> endStatements = new ArrayList<>();


}
