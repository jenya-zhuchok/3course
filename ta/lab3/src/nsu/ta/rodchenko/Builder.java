package nsu.ta.rodchenko;

import java.util.ArrayList;

public class Builder {

    Grammar ks_grammar;
    LMPAutomate lmpAutomate;
    RMPAutomate rmpAutomate;


    private void createGrammar(ArrayList<String> strs){
        ArrayList<Rule> rules = new ArrayList<>();
        for (String str : strs)
            rules.addAll(Parser.parserRules(str));
        ks_grammar = new Grammar(rules);
    }


    public Builder(ArrayList<String> str){
        createGrammar(str);
        lmpAutomate = new LMPAutomate(ks_grammar);
        rmpAutomate = new RMPAutomate(ks_grammar);
        print();
    }


    private void print(){
        System.out.println("--------------------------------------------");
        lmpAutomate.print();
        System.out.println("--------------------------------------------");
        rmpAutomate.print();
    }
}
