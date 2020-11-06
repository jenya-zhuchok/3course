import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.util.*;

public class RunDetected {

  private MulticastSocket socket;
  private Map<String, Long> list;
  private long lastPrint;
  private int updatePrint = 4000;
  private int limitTime = 5000;

  private String receive(){

      byte[] message = new byte[255];
      DatagramPacket packet = new DatagramPacket(message, message.length);

      try {
          socket.receive(packet);
      } catch (IOException e) {
          return null;
      }

      ByteBuffer bb = ByteBuffer.wrap(message);
      long firstLong = bb.getLong();
      long secondLong = bb.getLong();
      UUID uuid = new UUID(firstLong, secondLong);

      //System.out.println(uuid);

      String retRecv = "ip: " + packet.getAddress().toString() + "\nid: " + uuid + "\n";// re
      return retRecv;
  }

  private void print() {

      long now = System.currentTimeMillis();

      for (Map.Entry<String, Long> entry : list.entrySet())
          if(now - entry.getValue() > limitTime)
              list.remove(entry.getKey());

      System.out.println("Detected " + list.size() + "copies:");

      for (Map.Entry<String, Long> entry : list.entrySet()){
          System.out.println(entry.getKey());
      }

      System.out.println();

      lastPrint = System.currentTimeMillis();
  }


  RunDetected(String adr, int port) throws IOException {

      socket = new MulticastSocket(port);
      InetAddress group = InetAddress.getByName(adr);
      socket.joinGroup(group);

      TimerTask sendMassege = new SenderTimer(socket);
      Timer timer = new Timer(true);
      timer.scheduleAtFixedRate(sendMassege, 0, 3 * 1000);

      list = new HashMap<String, Long>();


  }

  public void run(){

      System.out.println("Start:");
      System.out.println();
      lastPrint = System.currentTimeMillis();

      while(true){

          String str = receive();
          if(str != null)
              list.put(str,System.currentTimeMillis());

          if (System.currentTimeMillis() - lastPrint > updatePrint)
              print();
      }
  }
}

