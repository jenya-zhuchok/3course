package App;

import javax.swing.plaf.PanelUI;
import java.util.LinkedList;

enum Direction { up, down, right, left };

public class User {

    private String name;
    private LinkedList<Cell> body;
    private Direction look = Direction.right;

    User (String n){
        name = n;
        body = new LinkedList<Cell>();
    }

    public void  addCell(Cell cell){ body.add(cell); }

    public void turnUp() { look = Direction.up; }
    public void turnDown() { look = Direction.down; }
    public void turnRight() { look = Direction.right; }
    public void turnLeft() { look = Direction.left; }

    public Direction isDirection() {return look; }

    public Cell getHead(){return }


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
