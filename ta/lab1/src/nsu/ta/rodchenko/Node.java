package nsu.ta.rodchenko;

import java.util.ArrayList;
import java.util.UUID;

public class Node {

    private UUID id;
    private ArrayList<Character> in;
    private ArrayList<Character> out;

    private void init(){
        id = UUID.randomUUID();
        this.in = new ArrayList<>();
        this.out = new ArrayList<>();
    }

    public Node(){
        init();
    }

    public Node(Character in, Character out){
        init();
        addIn(in);
        addOut(out);
    }

    public boolean equals(Node node) {
        if(node == null) return false;
        return this.id ==  node.getId();
    }
    public void addIn(Character c){
        if (c != null)
            in.add(c);
    }

    public void addOut(Character c){
        if (c != null)
            out.add(c);
    }
    
    static public Node unite (Node a, Node b){

        if(a == null && b == null)
            return new Node();

        if(a == b)
            return  a;


        if(a != null && b == null)
            return a;

        if(a == null)
            return b;

        for (Character c : a.getIn())
            b.addIn(c);
        for (Character c : a.getOut())
            b.addOut(c);

        return b;
    }

    public UUID getId() {
        return id;
    }

    public ArrayList<Character> getIn() {
        return in;
    }

    public ArrayList<Character> getOut() {
        return out;
    }
}
