package App;

import java.util.ArrayList;

enum Direction { up, down, right, left };

public class User {

    private String name;
    private ArrayList<Cell> body;
    private Direction look = Direction.right;

    User (String n){
        name = n;
    }

    public void  addCell(Cell cell){ body.add(cell); }

    public void turnUp() { look = Direction.up; }
    public void turnDown() { look = Direction.down; }
    public void turnRight() { look = Direction.right; }
    public void turnLeft() { look = Direction.left; }

    public void death() { body.clear(); }

}
