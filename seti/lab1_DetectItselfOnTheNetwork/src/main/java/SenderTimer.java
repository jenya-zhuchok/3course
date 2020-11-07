import java.io.IOException;
import java.net.DatagramPacket;

import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.TimerTask;
import java.util.UUID;

public class SenderTimer extends TimerTask {

        private MulticastSocket socket;
        private byte[] buffer;
        private DatagramPacket packet;
        private int senderror = 0;

        public SenderTimer(MulticastSocket s){
            socket = s;

            UUID uuid = UUID.randomUUID();
            System.out.println(uuid);
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
            buffer = bb.array();

            packet = new DatagramPacket(buffer, buffer.length, socket.getInetAddress(), socket.getPort());
        }

        @Override
        public void run() {
            System.out.println("Try send a message at:" + new Date());
            try {
                socket.send(packet);
                senderror = 0;
            } catch (IOException e) {
                senderror++;
                System.out.println("Massage was nos send");
                if(senderror >= 3)
                    System.out.println("May be the program is broken, reboot it");
            }
        }
}
