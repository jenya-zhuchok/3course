package nsu.ta.rodchenko;

public class NotTerm extends Literal {

    public NotTerm(char c) {
        super(Character.toUpperCase(c));
    }

    public NotTerm(char c, int n){
        super(Character.toUpperCase(c));
        setNum(n);
    }
}
