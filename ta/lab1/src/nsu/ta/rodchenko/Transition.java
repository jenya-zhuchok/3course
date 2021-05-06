package nsu.ta.rodchenko;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Transition {

    private Node from;
    private Node to;
    private Character value;
    private boolean isBack = false;


    public Transition(Character c){
        from = new Node();
        to = new Node();
        value = c;
        link(from, to, value);
    }

    public Transition(Node f, Node t, Character c){
        from = f;
        to = t;
        value = c;
        link(from, to, value);
    }

    public void back(){
        isBack = true;
    }

    private void link (@NotNull Node from, @NotNull Node to, Character value){
        from.addOut(value);
        to.addIn(value);
    }

    static public ArrayList<Node> getStatements(ArrayList <Transition> transitions){

        ArrayList<Node> statements = new ArrayList<>();
        for(Transition t : transitions) {
            if (!statements.contains(t.getFrom()))
                statements.add(t.getFrom());
            if (!statements.contains(t.getTo()))
                statements.add(t.getTo());
        }
        return statements;
    }

    public void connection(Transition t){
        to = Node.unite(this.getTo(), t.getFrom());
    }


    public void fork(Transition t){
        from = Node.unite(this.getFrom(), t.getFrom());
    }

    public void merge(Transition t){
        to = Node.unite(this.getTo(), t.getTo());
    }


    public void setFrom(Node from) {
        this.from = from;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public Character getValue() {
        return value;
    }

    public boolean isLoop() {
        return  isBack;
    }
}
