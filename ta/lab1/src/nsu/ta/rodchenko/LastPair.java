package nsu.ta.rodchenko;

public class LastPair {

    private Node head;
    private Node tail;

    public LastPair (){

    }

    public void setHead(Node head) {
        this.head = head;
    }

    public void setTail(Node tail) {
        this.tail = tail;
    }

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }

    public void set(Transition t) {
        setHead(t.getFrom());
        setTail(t.getTo());
    }

    public boolean isEmpty() {
        return (head == null) || (tail == null);
    }

    public  void unite(){
        tail = Node.unite(tail, head);
    }
}

