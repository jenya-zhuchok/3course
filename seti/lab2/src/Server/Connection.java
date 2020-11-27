package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection implements Runnable{

    private Socket clientSocket;
    private FileOutputStream fileOut;
    private DataOutputStream writeInSocket;
    private DataInputStream readFromSocket;


    public Connection(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        writeInSocket = new DataOutputStream(clientSocket.getOutputStream()); //канал записи в сокет
        readFromSocket = new DataInputStream(clientSocket.getInputStream()); //канал чтения из сокета
    }

    private String getFileName() throws IOException {
        int nameSize = readFromSocket.readInt();
        byte[] arrayNameBytes = new byte[nameSize];
        readFromSocket.readFully(arrayNameBytes, 0, nameSize);
        return new String(arrayNameBytes, StandardCharsets.UTF_8);
    }


    private double spead(long size, long time){ return size/(time*1000); }

    private long processing(String fileName, long fileSize) throws IOException {
        byte[] arrayFileBytes = new byte[4096];
        long t1, t2, diftime;
        long read = 0;
        int part, difpart = 0;
        t1 = System.currentTimeMillis();

        while (read < fileSize) {
            part = readFromSocket.read(arrayFileBytes);
            difpart += part;
            t2 = System.currentTimeMillis();
            diftime = t2 - t1;
            if (diftime >= 3000) {
                System.out.println(fileName + ": Instant speed = " + spead(difpart, diftime) + "Mb/sec");
                t1 = t2;
                difpart = 0;
            }
            fileOut.write(arrayFileBytes, 0, part);
            read += part;
        }
        return read;
    }


    @Override
    public void run(){
        long fileSize, t1, t2 = 0;
        String fileName;

        //start new thread with accepted socket
        System.out.print("Have connection!\n");

        try {
            fileName = getFileName();
            fileSize = readFromSocket.readLong();

            fileOut = new FileOutputStream("D:/repos/git/3_corse/3course/seti/lab2/uploads/" + fileName);

            t1 = System.currentTimeMillis();
            long read = processing(fileName, fileSize);
            t2 = System.currentTimeMillis();

            System.out.println(fileName + ": Speed per session = " + spead(read,t2 - t1 ) + " Mb/sec");
            if(read == fileSize) System.out.println(fileName + ": Size of received file is equal size of sended file");
            else System.out.println(fileName + ": Size of received file is not equal size of sended file");

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            try {
                fileOut.close();
                writeInSocket.writeInt(1);
                writeInSocket.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                readFromSocket.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}