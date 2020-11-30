package App;

import java.util.ArrayList;
import java.util.Random;

enum Obj { none, }

public class Field {

    private int DEFULT_MAX_USERS = 5;
    private int DEFAULT_FOOD = 2;
    private int DEFAULT_WIDTH = 15;
    private int DEFAULT_HEIGHT = 12;

    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;

    private int maxUsers = DEFULT_MAX_USERS;
    private int foodCount = DEFAULT_FOOD;

    private ArrayList<ArrayList<Cell>> field;
    private ArrayList<Cell> foods;
    private ArrayList<User> users;

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
    }

    Field (int w, int h){
        width = w;
        height = h;

        createField();
    }

    Field (int w, int h, int users, int foods){
        width = w;
        height = h;

        if (users > 0)
            maxUsers = users;
        if (foods > 0)
            foodCount = foods;

        createField();
    }

    private void addFood(){
        Cell cell = findRandomFreeCell();
        foods.add(cell);
    }

    public boolean addUser(User u){
        if (users.size() == maxUsers) return false;
        Cell cell = findRandomFreeCell();
        u.addCell(cell);
        users.add(u);
        return true;
    }

}
