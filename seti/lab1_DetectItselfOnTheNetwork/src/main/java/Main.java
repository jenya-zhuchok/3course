import java.io.IOException;


public class Main {

    public static void main(String args[]) throws IOException {
        String address = "251.0.0.4";
        int port = 4565;

        RunDetected run;
        try {
            run = new RunDetected(address, port);
        } catch (IOException e) {
            return;
        }
        run.run();
    }
}