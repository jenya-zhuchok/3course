package nsu.ta.rodchenko;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;

public class Automat {

    private String reg_exp;
    private HashSet<Character> alphabet;
    private ArrayList<Node> statements;
    private ArrayList<Transition> transitions;
    private LastPair lastPair;

    public Automat(String string) {
        alphabet = new HashSet<>();
        statements = new ArrayList<>();
        transitions = new ArrayList<>();
        lastPair = new LastPair();

        if (string.isEmpty())
            return;

        reg_exp = string;
        Parsing();
    }//ok

    private int lastTransitionIndex(){
        return transitions.size() - 1;
    } //ok

    private void refreshStatement(){
        statements.clear();
        statements = Transition.getStatements(transitions);
    }//ok


    private  ArrayList<ArrayList<ArrayList<Transition>>> remember(Node from, Node to){
        ArrayList<ArrayList<ArrayList<Transition>>> rem = new ArrayList<ArrayList<ArrayList<Transition>>> ();
        ArrayList<ArrayList<Transition>> remHead= Utilit.remember(from, transitions);
        ArrayList<ArrayList<Transition>> remTail= Utilit.remember(to, transitions);
        rem.add(remHead);
        rem.add(remTail);
        return rem;
    }//ok

    private  ArrayList<ArrayList<ArrayList<Transition>>> remember(Node from, Node to, ArrayList<Transition> transitions){
        ArrayList<ArrayList<ArrayList<Transition>>> rem = new ArrayList<ArrayList<ArrayList<Transition>>> ();
        ArrayList<ArrayList<Transition>> remHead= Utilit.remember(from, transitions);
        ArrayList<ArrayList<Transition>> remTail= Utilit.remember(to, transitions);
        rem.add(remHead);
        rem.add(remTail);
        return rem;
    }//ok

    private void forget(ArrayList<ArrayList<ArrayList<Transition>>> rem, Node newFrom, Node newTo){
        Utilit.fogot(newFrom, rem.remove(0));
        Utilit.fogot(newTo, rem.remove(0));
    }//ok

    private void concat(Transition t){
        if(transitions.size() > 0){
            if(!transitions.get(lastTransitionIndex()).isLoop())
                transitions.get(lastTransitionIndex()).connection(t);
            else {
                t.setFrom(transitions.get(lastTransitionIndex()).getTo());
                transitions.add(t);
                lastPair.set(t);
                refreshStatement();
                return;
            }
        }
        lastPair.setHead(t.getFrom());
        transitions.add(t);
        refreshStatement();
    }

    private void concat(ArrayList<Transition> t){
        if(transitions.size() == 0){
            transitions = t;
            return;
        }

        if(t.size() == 0) return;

        ArrayList<ArrayList<ArrayList<Transition>>> rem =
                remember(transitions.get(lastTransitionIndex()).getTo(),t.get(0).getFrom());
        ArrayList<ArrayList<ArrayList<Transition>>> rem2 =
                remember(transitions.get(lastTransitionIndex()).getTo(),t.get(0).getFrom(), t);

        int position = lastTransitionIndex();
        transitions.get(position).connection(t.get(0));
        transitions.addAll(t);

        forget(rem, transitions.get(position).getTo(),transitions.get(position+ 1).getFrom());
        forget(rem2, transitions.get(position).getTo(),transitions.get(position+ 1).getFrom());

        refreshStatement();
    }

    private Automat fragment(int from, int to){

        if( to - 1 <= from) return null; //bad

        char[] dst = new char[to - from - 1];
        reg_exp.getChars(from + 1, to, dst, 0);
        return new Automat(String.valueOf(dst));
    }// ok

    private void wasGetPlusSymbol(@NotNull Automat a){ //(a+b)*
        alphabet.addAll(a.alphabet);
        transitions.addAll(a.transitions);

        lastPair.setHead(transitions.get(0).getFrom());
        ArrayList<ArrayList<ArrayList<Transition>>> remS = remember(lastPair.getHead(), a.lastPair.getHead());
        ArrayList<ArrayList<ArrayList<Transition>>> remE = remember(lastPair.getTail(), a.lastPair.getTail());

        lastPair.setHead(Node.unite(lastPair.getHead(), a.lastPair.getHead()));
        lastPair.setTail(Node.unite(lastPair.getTail(), a.lastPair.getTail()));

        forget(remS, lastPair.getHead(), lastPair.getHead());
        forget(remE, lastPair.getTail(), lastPair.getTail());

        refreshStatement();
    }

    private void wasGetRepeatSymbol(){
        ArrayList<ArrayList<ArrayList<Transition>>> rem = remember(lastPair.getHead(), lastPair.getTail());
        lastPair.unite();
        transitions.get(lastTransitionIndex()).back();
        forget(rem, lastPair.getHead(), lastPair.getTail());
        refreshStatement();
    } //ok

    private void wasGetLiteral(Character c){
        alphabet.add(c); //ok
        Transition t = new Transition(c);
        lastPair.set(t);
        concat(t);
        refreshStatement();
    }

    private void   wasGetSubExp(@NotNull Automat subExpr){
        alphabet.addAll(subExpr.alphabet);
        lastPair = subExpr.lastPair;
        concat(subExpr.transitions);
        refreshStatement();
    }



    private void  Parsing(){
        int len = reg_exp.length();
        for(int i = 0; i < len; i++){
            char[] dst;
            switch (reg_exp.charAt(i)){
                case '*':
                    wasGetRepeatSymbol();
                    break;

                case '+':
                    wasGetPlusSymbol(fragment(i, len));
                    i = len;
                    break;

                case '(':
                    int j = reg_exp.lastIndexOf(')');

                    if(j-i == 1) {
                        i = j;
                        continue;
                    }

                    wasGetSubExp(fragment(i, j));
                    i = j;
                    break;
                default:
                    wasGetLiteral(reg_exp.charAt(i));

            }
        }
        lastPair.setHead(transitions.get(0).getFrom()); //bad

    }


    public ArrayList<Node> getStatements() {
        return statements;
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public HashSet<Character> getAlphabet() {
        return alphabet;
    }


}
