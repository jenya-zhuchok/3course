package App;

public class Cell {
    private int x;
    private int y;

    private boolean free = true;

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isFree() { return free; }

    public void free() { free = true; }
    public void notFree() { free = false; }

    Cell(){}

    Cell(int x, int y){
        this.x = x;
        this.y = y;
    }
}
