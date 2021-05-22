package nsu.ta.rodchenko;


import java.util.ArrayList;
import java.util.HashSet;

public class Rule {

    private  Character left;
    private  ArrayList<Character> right;


    public Rule(Character l, ArrayList<Character> r){
        left = l;
        right = r;
    }


    public static String char2str(ArrayList<Character> literals){
        StringBuilder str = new StringBuilder();
        for(Character l : literals)
            str.append(l);

        return str.toString();
    }

    public HashSet<Character> getAllTerms(){
        HashSet<Character> terms = new HashSet<>();
        for(Character l : right)
            if(Character.isAlphabetic(l) && Character.isLowerCase(l)) terms.add(l);
        return terms;
    }

    public HashSet<Character> getAllNotTerms(){
        HashSet<Character> terms = new HashSet<>();
        terms.add(left);
        for(Character l : right)
            if(Character.isAlphabetic(l) && Character.isUpperCase(l)) terms.add(l);
        return terms;
    }

    public ArrayList<Character> getRight() {
        return right;
    }

    public Character getLeft() {
        return left;
    }

    public boolean isEqualLeft(Rule r){
        return left == r.getLeft();
    }

    public boolean isEqualRight(Rule r){
        if(right.size() != r.getRight().size()) return false;
        for(int i = 0; i < right.size(); i++)
            if(right.get(i) != r.getRight().get(i)) return false;
        return true;
    }

    public static ArrayList<ArrayList<Rule>> unions(ArrayList<Rule> rules, DeltaFunc.TYPE type){
        ArrayList<ArrayList<Rule>> unions = new ArrayList<>();
            for(Rule r : rules){
                boolean flag = true;
                for(ArrayList<Rule> arr : unions) {
                    if ((type == DeltaFunc.TYPE.LMP && arr.get(0).isEqualLeft(r)) || (type == DeltaFunc.TYPE.RMP && arr.get(0).isEqualRight(r))) {
                            arr.add(r); flag = false; break;
                        }
                }
                if(flag) {
                    unions.add(new ArrayList());
                    unions.get(unions.size() - 1).add(r);
                }
            }
        return unions;
    }

}
