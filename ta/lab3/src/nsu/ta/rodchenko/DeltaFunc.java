package nsu.ta.rodchenko;

import java.util.ArrayList;
import java.util.HashSet;

public class DeltaFunc {

    private Configuration config;
    private  ArrayList<ProbablyStep> right = new ArrayList<>();

    public enum TYPE {LMP, RMP};

    private DeltaFunc(Configuration c, ProbablyStep step){
        config = c;
        right.add(step);
    }

    public  DeltaFunc(TYPE type, ArrayList<Character> characters){
        if(type == TYPE.LMP){
            config = new Configuration(characters, characters);
            right.add(new ProbablyStep(config.getCurState()));
        }
        if(type == TYPE.RMP){
            config = new Configuration(characters, null);
            right.add(new ProbablyStep(config.getCurState(), characters));
        }
    }

    public static DeltaFunc lastDeltaForRPM(Character startSymb){
        return new DeltaFunc(new Configuration(null, Parser.parsRight("$" + startSymb)), new ProbablyStep('r'));
    }
    public DeltaFunc(ArrayList<Rule> rules, TYPE type){
        config = new Configuration(rules.get(0), type);

        for(Rule r: rules) {
            if (type == TYPE.LMP)
                right.add(new ProbablyStep(config.getCurState(), r.getRight()));
            if (type == TYPE.RMP)
                right.add(new ProbablyStep(config.getCurState(), r.getLeft()));
        }
    }

    public String toString(){
        StringBuilder mainStr = new StringBuilder("Δ" + config.toString() + " = { ");
        ArrayList<String> strs= new  ArrayList<>();
        for(ProbablyStep s : right)
            mainStr.append(s.toString() + " ");
        mainStr.append("} ");
        return mainStr.toString();
    }


    static public HashSet<Character> getAllStatements(ArrayList<DeltaFunc> deltas){
        HashSet<Character> chars = new HashSet<>();
        for (DeltaFunc d: deltas){
            chars.add(d.config.getCurState());
            for(ProbablyStep s: d.right)
                chars.add(s.getState());
        }
        return chars;
    }

}
