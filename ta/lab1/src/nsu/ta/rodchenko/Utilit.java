package nsu.ta.rodchenko;

import java.util.ArrayList;

public class Utilit {

    static public ArrayList<ArrayList<Transition>> remember(Node n, ArrayList<Transition> transitions){

        ArrayList<ArrayList<Transition>> rem = new ArrayList<>();

        ArrayList<Transition> rememberFrom = new ArrayList<>();
        for(Transition t: transitions)
            if(t.getFrom()==n) rememberFrom.add(t);

        ArrayList<Transition> rememberTo = new ArrayList<>();
        for(Transition t: transitions)
            if(t.getTo()==n) rememberTo.add(t);

        rem.add(rememberFrom);
        rem.add(rememberTo);

        return rem;
    }

    static public void fogot(Node n, ArrayList<ArrayList<Transition>> rem){
        ArrayList<Transition> rememberFrom = rem.remove(0);
        ArrayList<Transition> rememberTo = rem.remove(0);

        for(Transition t: rememberTo) t.setTo(n);
        for(Transition t: rememberFrom) t.setFrom(n);
    }


    static public void printResult(FiniteAutomaton fa){
        System.out.print("Σ = { ");
        for(Character c: fa.getAlphabet())
            System.out.print(c + " ");
        System.out.println("}");

        //----------------------------------------------------------//
        ArrayList<Node> statements = fa.getStatements();
        System.out.print("Q = { ");
        int i = 0;
        for(Node n: statements) {
            System.out.print("q" + i + " ");
            i++;
        }
        System.out.println("}");
        //----------------------------------------------------------//

        System.out.println("δ = {");
        //int i = 0;
        for(Transition t: fa.getTransitions()) {
            int from = statements.indexOf(t.getFrom());
            int to = statements.indexOf(t.getTo());
            Character value = t.getValue();
            System.out.println("("+value + ", "+"q"+from+ ")-> q"+to);
        }
        System.out.println("}");

    }

    static public void printResult(Automat fa){
        System.out.print("Σ = { ");
        for(Character c: fa.getAlphabet())
            System.out.print(c + " ");
        System.out.println("}");

        //----------------------------------------------------------//
        ArrayList<Node> statements = fa.getStatements();
        System.out.print("Q = { ");
        int i = 0;
        for(Node n: statements) {
            System.out.print("q" + i + " ");
            i++;
        }
        System.out.println("}");
        //----------------------------------------------------------//

        System.out.println("δ = {");
        //int i = 0;
        for(Transition t: fa.getTransitions()) {
            int from = statements.indexOf(t.getFrom());
            int to = statements.indexOf(t.getTo());
            Character value = t.getValue();
            System.out.println("("+value + ", "+"q"+from+ ")-> q"+to);
        }
        System.out.println("}");

    }
}
