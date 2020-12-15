package snake.App;

enum onCell {nothing, snake, food};

public class Cell {
    private int x;
    private int y;

    onCell state = onCell.nothing;

    public int getX() { return x; }
    public int getY() { return y; }

    public onCell whatIs() { return state; }

    public boolean isFree() { return state == onCell.nothing; }
    public boolean isFood() { return state == onCell.food; }
    public boolean isSnake() { return state == onCell.snake; }

    public void free() { state = onCell.nothing; }
    public void food() { state = onCell.food; }
    public void snake() {state = onCell.snake; }

    Cell(){}

    Cell(int x, int y){
        this.x = x;
        this.y = y;
    }
}
