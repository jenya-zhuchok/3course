package Client;

import java.io.*;
        import java.lang.*;
        import java.net.Socket;
        import java.nio.charset.StandardCharsets;

public class Client {
    private static int PORT;
    private static String IP_ADRESS;


    public Client(int port, String adress){
        Client.PORT = port;
        Client.IP_ADRESS = adress;
    }


    public void sendFile(String filePath){
        File file = new File(filePath);
        String fileName = file.getName();
        long fileSize = file.length();
        byte[] arrayNameBytes;
        byte[] arrayFileBytes = new byte[4096];

        try (Socket clientSocket = new Socket(IP_ADRESS, PORT)) {
            DataInputStream readFromSocket = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream writeInSocket = new DataOutputStream(clientSocket.getOutputStream());
            FileInputStream readFromFile = new FileInputStream(filePath);
            arrayNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
            int request = 0;

            writeInSocket.writeInt(arrayNameBytes.length);
            writeInSocket.write(arrayNameBytes);
            writeInSocket.writeLong(fileSize);

            long read = 0;
            while (read < fileSize) {
                int part = readFromFile.read(arrayFileBytes);
                writeInSocket.write(arrayFileBytes, 0, part);
                read += part;
            }

            request = readFromSocket.readInt();
            if (request == 1) System.out.print("Send file successfully!");
            else System.out.print("Don't send file!");

            readFromFile.close();
            writeInSocket.close();
            readFromSocket.close();

        } catch (Exception e){ e.printStackTrace();}

    }

}