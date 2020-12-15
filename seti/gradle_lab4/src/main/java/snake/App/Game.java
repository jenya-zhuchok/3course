package snake.App;

import java.util.HashMap;

public class Game {

    private Field field;
    private HashMap<User, Snake> users;

    

    Game(){
        field = new Field();
        users = new HashMap<User, Snake>();
    }


    public void addUser(User u){
        Snake s = field.addSnake(u);
        if(s != null)
            users.put(u, s);
    }




}
