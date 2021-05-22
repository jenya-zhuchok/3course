package nsu.ta.rodchenko;

import java.util.ArrayList;
import java.util.HashSet;

public class Grammar {

    ArrayList<Rule> rules = new ArrayList<>();
    HashSet<Character> termAlphabet = new HashSet<>();
    HashSet<Character> notTermAlphabet = new HashSet<>();
    Character startSymbol;

    public Grammar(ArrayList<Rule> rules){
        this.rules = rules;
        lookAlphabet();
        startSymbol = rules.get(0).getLeft(); // небезопасно
    }

    public void lookAlphabet(){
        for(Rule r: rules)
            termAlphabet.addAll(r.getAllTerms());
        for(Rule r: rules)
            notTermAlphabet.addAll(r.getAllNotTerms());

    }

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public HashSet<Character> getTermAlphabet() {
        return termAlphabet;
    }

    public Character getStartSymbol() {
        return startSymbol;
    }
}
