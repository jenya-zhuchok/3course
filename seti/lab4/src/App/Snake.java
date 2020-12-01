package App;

import java.util.ArrayList;
import java.util.LinkedList;

enum Direction { up, down, right, left };

public class Snake {

    private LinkedList<Cell> body;
    private Direction look = Direction.right;

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


    public void turnUp() { look = Direction.up; }
    public void turnDown() { look = Direction.down; }
    public void turnRight() { look = Direction.right; }
    public void turnLeft() { look = Direction.left; }

    public Direction isDirection() {return look; }

    public Cell getHead(){ return body.getFirst(); }

    public boolean move(Cell cell){

        if(cell.isSnake()) {
            death();
            return false;
        }

        if(cell.isFree()){
            body.removeLast().free();
            cell.snake();
            body.push(cell);
        }

        if(cell.isFood()){
            cell.snake();
            body.push(cell);
        }

        return true;
    }


    public void death() { body.clear(); }

}
