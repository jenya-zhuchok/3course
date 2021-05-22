package nsu.ta.rodchenko;

import java.util.ArrayList;
import java.util.HashSet;

public class RMPAutomate {

    HashSet<Character> enterAlphabet = new HashSet<>();
    HashSet<String> symbolShopAlphabet = new HashSet<>();
    Character startSymbol;

    ArrayList<DeltaFunc> delta = new ArrayList<>();

    private void makeShopSymbols(Grammar ks_grammar){
        ArrayList<Rule> rules = ks_grammar.getRules();
        for(Rule r: rules){
            symbolShopAlphabet.add(r.getLeft().toString());
            symbolShopAlphabet.add(Rule.char2str(r.getRight()));
        }
        symbolShopAlphabet.add("$");
    }

    private void  makeDeltaFuncs(ArrayList<ArrayList<Rule>> unions){

        for(ArrayList<Rule> rules : unions)
            delta.add(new DeltaFunc(rules, DeltaFunc.TYPE.LMP));

        delta.add(new DeltaFunc(DeltaFunc.TYPE.RMP, Parser.parsRight("+")));
        delta.add(new DeltaFunc(DeltaFunc.TYPE.RMP, Parser.parsRight("*")));
        delta.add(new DeltaFunc(DeltaFunc.TYPE.RMP, Parser.parsRight("(")));
        delta.add(new DeltaFunc(DeltaFunc.TYPE.RMP, Parser.parsRight(")")));
        delta.add(DeltaFunc.lastDeltaForRPM(startSymbol));

    }

    public RMPAutomate(Grammar ks_grammar){

        enterAlphabet.addAll(ks_grammar.getTermAlphabet());
        startSymbol = ks_grammar.getStartSymbol();
        makeShopSymbols(ks_grammar);
        makeDeltaFuncs(Rule.unions(ks_grammar.getRules(), DeltaFunc.TYPE.RMP));

    }

    public void print(){
        System.out.println("Right MP Automate:");

        System.out.print("Statements: {");
        for(Character c : DeltaFunc.getAllStatements(delta))
            System.out.print(" "+c+" ");
        System.out.println("}");

        System.out.print("Enter Alphabet: {");
        for(Character c : enterAlphabet)
            System.out.print(" "+c+" ");
        System.out.println("}");

        System.out.print("Symbol Shop Alphabet: {");
        for(String c : symbolShopAlphabet)
            System.out.print(" "+c+" ");
        System.out.println("}");

        System.out.println("Deltas: {");
        for(DeltaFunc d : delta)
            System.out.println(d.toString());
        System.out.println();
        System.out.println("Here {e} is empty symbol");
        System.out.println("}");

        System.out.println("Start statement: q");
        System.out.println("Start symbol: " + startSymbol);
        System.out.println("Shop symbol: $");
        System.out.println("End statements: { r }");
    }
}
