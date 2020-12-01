package App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Field {

    private int DEFULT_MAX_USERS = 5;
    private int DEFAULT_FOOD = 2;
    private int DEFAULT_WIDTH = 15;
    private int DEFAULT_HEIGHT = 12;

    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;

    private int maxUsers = DEFULT_MAX_USERS;
    private int maxFood = DEFAULT_FOOD;
    private int foodCount = 0;

    private ArrayList<ArrayList<Cell>> field;
    private HashMap<User, Snake> users;

    private void createField(){

        field = new ArrayList<>();
        for(int i = 0; i < width; i++)
        {
            field.add(new ArrayList<>());
            for(int j = 0; j < height; j++)
                field.get(i).add(new Cell(i, j));
        }
    }
    private Cell getCell(int x, int y){ return field.get(x).get(y); }
    private Cell findRandomFreeCell(){
        Random r = new Random();
        while (true){
            int w = r.nextInt(width);
            int h = r.nextInt(height);
            if (getCell(w, h).isFree()) return getCell(w, h);
        }
    }

    Field (){
        createField();
        addFood();
    }

    Field (int w, int h){
        width = w;
        height = h;

        createField();
        addFood();
    }

    Field (int w, int h, int snakes, int foods){
        width = w;
        height = h;

        if (snakes > 0)
            maxUsers = snakes;
        if (foods > 0)
            maxFood = foods;

        createField();
        addFood();
    }

    private void addFood(){
        while (foodCount < maxFood){
            Cell cell = findRandomFreeCell();
            cell.food();
        }
    }


    //TODO:
    // добавить возможность создавать змеек с более чем одной клеткой
    public Snake addUser(User u){

        if (users.size() >= maxUsers)
            return null;

        Cell cell = findRandomFreeCell();
        Snake s = new Snake(cell);
        users.put(u, s);

        return s;
    }


    public void step() {
        for(Map.Entry<User, Snake> entry : users.entrySet()){

            Snake  s = entry.getValue();
            Cell head = s.getHead();
            Cell newHead = null;
            
            if(s.isDirection() == Direction.up)
                newHead = getCell(head.getX(), (head.getY() + 1) % height);

            if(s.isDirection() == Direction.down)
                newHead = getCell(head.getX(), (head.getY() + height - 1) % height);


            if(s.isDirection() == Direction.right)
                newHead = getCell((head.getX() + 1) % width, head.getY());

            if(s.isDirection() == Direction.left)
                newHead = getCell((head.getX() + width - 1) % width, head.getY());

            s.move(newHead);
        }

    }

}
