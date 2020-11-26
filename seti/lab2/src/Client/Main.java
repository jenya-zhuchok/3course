package Client;

public class Main {

    public static void main(String[] args) {
        Client c = new Client(Integer.parseInt(args[0]), args[1]);
        c.sendFile(args[2]);
    }
}