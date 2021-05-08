package nsu.ta.rodchenko;


public abstract class Literal {

    private Character c;
    private int num = -1;

    public Literal (Character c){
        this.c = c;
    }

    @Override
    public String toString() {
        String str = String.valueOf(c);
        if (num >= 0)
            return str + num;

        return str;
    }

    public Character getC() {
        return c;
    }

    public void setC(Character c) {
        this.c = c;
    }

    public void setNum(int num) {
        this.num = num;
    }


    public int getNum() {
        return num;
    }

    public boolean equals(Literal l){
        return (getC().equals(l.getC())) && (getNum() == l.getNum());
    }
}
