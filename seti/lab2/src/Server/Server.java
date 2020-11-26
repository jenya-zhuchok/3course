package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    private static int PORT;

    public Server(int port_num) {
        PORT = port_num;
        int i = 0;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                i++;
                System.out.printf("Thread%d\n", i);
                final Socket clientSocket = serverSocket.accept();
                Connection connection = new Connection(clientSocket);
                Thread thread = new Thread(connection);
                thread.start();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        };
    }


}