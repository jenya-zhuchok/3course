package nsu.ta.rodchenko;

import nsu.ta.rodchenko.literals.Literal;
import nsu.ta.rodchenko.literals.Term;

import java.util.ArrayList;
import java.util.HashSet;

public class Rule {

    private  ArrayList<Literal> left;
    private  ArrayList<Literal> right;


    public Rule(ArrayList<Literal> l, ArrayList<Literal> r){
        left = l;
        right = r;
    }


    private Rule(String first, String second) {
        left = Parser.parsLiterals(first);
        right = Parser.parsLiterals(second);
    }


    public static String lit2str(ArrayList<Literal> literals){
        StringBuilder str = new StringBuilder();
        for(Literal l : literals)
            str.append(l.toString());

        return str.toString();
    }


    public void addLiteralToEndRight(ArrayList<Literal> l){
        right.addAll(l);
    }

    public void addLiteralToEndRight(Literal l){
        right.add(l);
    }

    public void addLiteralToTopRight(Literal l){
        right.add(0, l);
    }


    public int getRightSize(){
        return right.size();
    }



    public HashSet<Character> getAllTerms(){
        HashSet<Character> terms = new HashSet<>();
        for(Literal l : right)
            if( l instanceof Term) terms.add(l.getC());
        return terms;
    }


    public void print(){
        System.out.print(lit2str(left));
        System.out.print( " -> ");
        System.out.println(lit2str(right));
    }

    public ArrayList<Literal> getRight() {
        return right;
    }

    public ArrayList<Literal> getLeft() {
        return left;
    }

    public void setRight(ArrayList<Literal> right) {
        this.right = right;
    }

    public boolean isRightEquals(ArrayList<Literal> tmp) {
        if(tmp.size() != right.size()) return false;
        for(int i = 0; i < tmp.size(); i++){
            if(!tmp.get(i).equals(right.get(i))) return false;
        }
        return true;
    }
}
