package nsu.ta.rodchenko;

import java.util.ArrayList;

public class Configuration {
    private Character curState = 'q';
    private ArrayList<Character> chain = new ArrayList<>();
    private ArrayList<Character> clip = new ArrayList<>();


    public Configuration(Rule r, DeltaFunc.TYPE type){
        if(type == DeltaFunc.TYPE.LMP)
            clip.add(r.getLeft());
        if(type == DeltaFunc.TYPE.RMP)
            clip.addAll(r.getRight());
    }

    public Configuration(ArrayList<Character> ch, ArrayList<Character> cl){
        if(cl != null) clip.addAll(cl);
        if(ch !=null) chain.addAll(ch);
    }

    public Character getCurState() {
        return curState;
    }

    public String toString(){
        String s1 = "{e}";
        String s2 = "{e}";
        if(!chain.isEmpty()) s1 = Rule.char2str(chain);
        if(!clip.isEmpty()) s2 = Rule.char2str(clip);
        return "("+ curState +", " + s1 +", " + s2 + ")" ;
    }
}
