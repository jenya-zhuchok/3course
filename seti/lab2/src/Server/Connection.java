package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection implements Runnable{
    private Socket clientSocket;
    private long numReceivedBytes;

    public Connection(Socket clientSocket){
        this.clientSocket = clientSocket;
    }
    private FileOutputStream fileOut;
    private DataOutputStream writeInSocket;
    private DataInputStream readFromSocket;

    @Override public void run(){
        try {

            int nameSize, part;
            long fileSize, t1, t2, t3 = 0;
            float dif;
            String fileName;
            byte[] arrayNameBytes;
            byte[] arrayFileBytes = new byte[4096];

            //start new thread with accepted socket
            System.out.print("Have connection!\n");

            writeInSocket = new DataOutputStream(clientSocket.getOutputStream()); //канал записи в сокет
            readFromSocket = new DataInputStream(clientSocket.getInputStream()); //канал чтения из сокета

            nameSize = readFromSocket.readInt();
            arrayNameBytes = new byte[nameSize];
            readFromSocket.readFully(arrayNameBytes, 0, nameSize);
            fileName = new String(arrayNameBytes, StandardCharsets.UTF_8);
            fileOut = new FileOutputStream("D:/repos/git/3_corse/3course/seti/lab2/" + fileName);
            fileSize = readFromSocket.readLong();
            long read = 0;
            t1 = System.currentTimeMillis();
            t2 = t1;

            while (read < fileSize) {
                part = readFromSocket.read(arrayFileBytes);
                t3 = System.currentTimeMillis();
                dif = t3 - t2;
                if (dif >= 3000) {
                    System.out.printf("Instant speed = %f Mb/sec\n",part/(dif*1000));
                    t2 = t3;
                }
                fileOut.write(arrayFileBytes, 0, part);
                read += part;
            }

            dif = t3 - t1;
            //if (dif < 3000) {
            //  System.out.printf("Instant speed = %f Mb/sec\n",read/(dif*1000));
            //}
            System.out.printf("Speed per session = %f Mb/sec\n", read/(dif*1000));
            numReceivedBytes = read;
            if(read == fileSize) System.out.print("Size of received file is equal size of sended file\n");
            else System.out.print("Size of received file is not equal size of sended file\n");

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