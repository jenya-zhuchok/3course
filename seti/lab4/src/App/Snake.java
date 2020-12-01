package App;

import java.util.ArrayList;
import java.util.LinkedList;

enum Direction { up, down, right, left };

public class Snake {

    private LinkedList<Cell> body;
    private Direction look = Direction.right;
    private int score = 1;

    Snake (){
        body = new LinkedList<Cell>();
    }

    Snake(Cell cell)
    {
        body = new LinkedList<Cell>();
        body.push(cell);
    }

    Snake(ArrayList<Cell> cells){
        body = new LinkedList<Cell>();
        for(Cell c : cells)
            body.add(c);
    }

    public void setBody(ArrayList<Cell> cells )  {
        for(Cell c : cells)
            body.add(c);
    }

    public int getScore() { return score; }

    public void turnUp() { look = Direction.up; }
    public void turnDown() { look = Direction.down; }
    public void turnRight() { look = Direction.right; }
    public void turnLeft() { look = Direction.left; }

    public Direction isDirection() {return look; }

    public Cell getHead(){ return body.getFirst(); }

    public int move(Cell cell){

        if(cell.isSnake())
            death();

        if(cell.isFree()){
            body.removeLast().free();
            cell.snake();
            body.push(cell);
        }

        if(cell.isFood()){
            cell.snake();
            body.push(cell);
            score++;
        }

        return body.size();
    }

    public void death() {
        for (Cell cell : body)
            cell.free();
        body.clear(); }

}
