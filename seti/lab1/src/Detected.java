import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Detected {

    private MulticastSocket socket;
    private InetAddress ADDR;
    private int PORT;


    private int TIMEOUT = 2000;
    private int UPDATE_TIMEOUT = 5000;
    private long lastSendtime = 0;
    private long lastRecvtime = 0;

    private UUID uuid;
    HashMap<UUID, Long> receivedUUID;


    Detected(String addr, int port) throws IOException {

        ADDR = InetAddress.getByName(addr);
        PORT = port;

        socket = new MulticastSocket(PORT);
        socket.joinGroup(ADDR);
        socket.setSoTimeout(TIMEOUT);

        uuid = UUID.randomUUID();
        receivedUUID = new HashMap<>();

        run();
    }

    private void send(){

        byte[] message = uuid.toString().getBytes();


        DatagramPacket packet = new DatagramPacket(message, message.length, ADDR, PORT);
        try {
            socket.send(packet);
            System.out.println("Sending UUID is successfully");
            lastSendtime = System.currentTimeMillis();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private UUID receive() throws IOException{

        int length = uuid.toString().length();
        byte[] message = new byte[length];

        DatagramPacket packet = new DatagramPacket(message, length);
        try {
            socket.receive(packet);
            System.out.println("Receiving UUID is successfully");
        } catch (SocketTimeoutException e) {
            System.out.println("Socket timeout");
            return null;
        }
        lastRecvtime = System.currentTimeMillis();
        return UUID.nameUUIDFromBytes(message);
    }


    private void printReceivedUUID(){
        long timeNow = System.currentTimeMillis();

        ArrayList<UUID> removeList = new ArrayList<UUID>();

        for (HashMap.Entry<UUID, Long> elem : receivedUUID.entrySet())
            if (timeNow - elem.getValue() > UPDATE_TIMEOUT )
                removeList.add(elem.getKey());

         for (UUID id : removeList)
             receivedUUID.remove(id);

        System.out.println("-------------------------------------------");
        System.out.println("Detected: \n");

        for (HashMap.Entry<UUID, Long> elem : receivedUUID.entrySet())
            System.out.println(elem.getKey());

        System.out.println("\n-------------------------------------------");
    }


    private void run() throws IOException {


        while(true){
            if(System.currentTimeMillis() - lastSendtime > TIMEOUT)     send();

            UUID id =  receive();
            if(id != null)  receivedUUID.put(id, lastRecvtime);

            printReceivedUUID();
        }
    }
}
