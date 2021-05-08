package nsu.ta.rodchenko;

import java.util.ArrayList;
import java.util.HashSet;

public class Grammar {

    ArrayList<Rule> rules = new ArrayList<>();
    HashSet<Character> alphabet = new HashSet<>();


    public Grammar(){}

    public Grammar(ArrayList<Rule> rules){
        this.rules = rules;
        lookAlphabet();
    }

    public void addRule(Rule r){
        rules.add(r);
        lookAlphabet();
    }

    public void printRules(){
        for(Rule r: rules)
            r.print();
    }

    public void printAlphabet(){
        for(Character c: alphabet)
            System.out.print(c + " ");
        System.out.println();
    }

    public void lookAlphabet(){
        for(Rule r: rules)
            alphabet.addAll(r.getAllTerms());
    }


    public HashSet<Character> getAlphabet() {
        return alphabet;
    }

    public boolean isEmptyLanguage(){
        return alphabet.size() <= 0;
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }
}
