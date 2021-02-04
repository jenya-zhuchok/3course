import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class Node {
    private InetSocketAddress parent, praParent, isa;
    private ByteBuffer sendBuffer;
    private DatagramChannel dc;
    private HashMap<InetSocketAddress, Long> aliveNeighbours = new HashMap<>();
    private LinkedList<Message> keepNodeMessages = new LinkedList<>();
    private HashSet<UUID> uuids = new HashSet<>(); //для полученных сообщений
    private DatagramPacket dp;
    private ByteBuffer recvBuffer;
    private String name;
    private int percents, port;

    private final byte TEXT_ACK = 3;
    private final byte TEXT_MSG = 2;
    private final byte MSG_HELLO = 0;
    private final byte MSG_ACK = 1;
    //служебное сообщение -> тип, порт(int), размер parent_ipAddress ,parent_ip_address в байтах
    //первый байт:
    //0 - HELLO
    //1 - подтверждение HELLO (ACK)
    //2 - текстовое сообщение от другого узла
    //3 - подтверждение на текстовое сообщение от другого узла

    private void log(InetSocketAddress parent, InetSocketAddress praParent){
        System.out.println("______________________________________");

        if (parent==null){
            System.out.println("MyParent == null");
        }
        else
            System.out.println("MyParent port = "+ parent.getPort() +" MyParent address = " +  parent.getAddress());

        if(praParent==null)
            System.out.println("praParent==null");
        else
            System.out.println("PraParent port = "+ praParent.getPort() +" PraParent address = " + praParent.getAddress());
        System.out.println("______________________________________");
    }

    public Node(String name, int percents, int port, InetSocketAddress parent) throws IOException {
        this.name = name;
        this.percents = percents;
        this.port = port;
        this.parent = parent;

        isa = new InetSocketAddress(port);
        dc = DatagramChannel.open();
        dc.bind(isa);

        sendBuffer = ByteBuffer.allocate(4096);

    }

    public void start() throws IOException  {

        DatagramSocket s = dc.socket();
        s.setSoTimeout(1000);

        byte[] recvBuffer_ = new byte[4096];
        recvBuffer = ByteBuffer.wrap(recvBuffer_);

        byte msgType;
        praParent = null; //предок родителя
        long t1;


        while (true) {

            sendMsgToParent();
            sendAllTextMsgs();
            removeWhoNotRequest();
            t1 = System.currentTimeMillis();

            //пакет -> тип сообщение, UUID, размер сообщения, сообщение

            while (System.currentTimeMillis() < t1 + 2000) {
                try {
                    dp = new DatagramPacket(recvBuffer_, recvBuffer_.length);
                    s.receive(dp);
                    recvBuffer.clear();
                    msgType = recvBuffer.get();
                    switch (msgType){
                        case MSG_HELLO: // получаем информацию от потомка
                            //System.out.println("Received a message from a descendant");
                            //отправляем сообщение с информацией о родителе
                            collectParentData();
                            rememberAliveNeighbour();
                            break;
                        case MSG_ACK: // получаем информацию от родителя про прародителя
                            //System.out.println("Received a message from an ancestor");
                            praParent = getPraParent();
                            //log(parent, praParent);
                            rememberAliveNeighbour();
                            break;
                        case TEXT_MSG: // получаем текстовое сообщение
                            double num = Math.random()*100;
                            if(num > percents){
                                //System.out.println("GET");
                                keepReceivedMsg();
                                msgRequest();
                            }
                            break;
                        case TEXT_ACK: // получаем подтверждение о получении сообщения
                            //System.out.println("OK");
                            removeAcceptedMsg();
                    }
                } catch (SocketTimeoutException e) {
//                     e.printStackTrace();
                }

            }
        }
    }

    private void sendMsgToParent() throws IOException{
        if (parent==null) return;

        sendBuffer.clear();
        sendBuffer.put(MSG_HELLO);
        sendBuffer.flip(); //указатель в конце
        //отправляем сообщение родителю и затем принимаем от него ответ, в это время может прийти сообщ от других узлов
        dc.send(sendBuffer, parent);
    }

    private void sendAllTextMsgs() throws IOException{
        for(Message m : keepNodeMessages)
        {
            sendBuffer.clear();
            sendBuffer.put(TEXT_MSG);
            sendBuffer.putLong(m.guid.getLeastSignificantBits());
            sendBuffer.putLong(m.guid.getMostSignificantBits());
            sendBuffer.putInt(m.msg.length);
            sendBuffer.put(m.msg);
            sendBuffer.flip();
            dc.send(sendBuffer, m.inetSocketAddress);
        }
    }

    private void removeWhoNotRequest(){
        //удаляем соседей, которые давно не отвечали

        ArrayList<InetSocketAddress> inetSocketAddressesDied = new ArrayList<InetSocketAddress>();

        for (Map.Entry<InetSocketAddress, Long> entry : aliveNeighbours.entrySet()) {
            if((System.currentTimeMillis() - entry.getValue()) > 8000) {
                if (entry.getKey().equals(parent)) {
                    parent = praParent;
                    if(praParent!=null)
                        aliveNeighbours.put(praParent, System.currentTimeMillis());
                }

                inetSocketAddressesDied.add(entry.getKey());
            }
        }

        for (InetSocketAddress elem: inetSocketAddressesDied)
            aliveNeighbours.remove(elem);
    }

    private InetSocketAddress getPraParent() throws UnknownHostException {
        int parentPort = recvBuffer.getInt();
        if (parentPort==0) return null;

        int lenght_addr = recvBuffer.getInt();
        byte[] recvAddr = new byte[lenght_addr];
        recvBuffer.get(recvAddr);
        return new InetSocketAddress(InetAddress.getByAddress(recvAddr), parentPort);
    }

    private void msgRequest() throws IOException {
        sendBuffer.clear();
        sendBuffer.put(TEXT_ACK);
        sendBuffer.flip();
        sendResponse();
    }


    private void keepReceivedMsg(){
        int msg_size;
        byte[] recvMsg;
        long part1 = recvBuffer.getLong();
        long part2 = recvBuffer.getLong();
        UUID uuid = new UUID(part1, part2);
        if(!uuids.contains(uuid)){
            uuids.add(uuid);
            msg_size = recvBuffer.getInt();
            recvMsg = new byte[msg_size];
            recvBuffer.get(recvMsg);
            for (ConcurrentMap.Entry<InetSocketAddress, Long> entry : aliveNeighbours.entrySet()) {
                if(!entry.getKey().equals((InetSocketAddress) dp.getSocketAddress()))
                {
                    Message message = new Message((InetSocketAddress)entry.getKey(), uuid, recvMsg);
                    keepNodeMessages.add(message);
                }
            }
            String msg = new String(recvMsg);
            System.out.println(msg);
        }
    }
    private void collectParentData() throws IOException {
        sendBuffer.clear();
        sendBuffer.put(MSG_ACK);

        InetSocketAddress MyParent = parent;

        if (MyParent==null || MyParent.equals(dp.getSocketAddress())){
            sendBuffer.putInt(0);
        } else {
            sendBuffer.putInt(MyParent.getPort());
            sendBuffer.putInt(MyParent.getAddress().getAddress().length);
            sendBuffer.put(MyParent.getAddress().getAddress());
        }
        sendBuffer.flip();
        sendResponse();
        //log(MyParent, praParent);
    }

    private synchronized void removeAcceptedMsg(){
        long part1 = recvBuffer.getLong();
        long part2 = recvBuffer.getLong();
        UUID u = new UUID(part1, part2);
        Iterator<Message> it = keepNodeMessages.iterator();
        while (it.hasNext()) {
            Message message = it.next();
            if (dp.getSocketAddress().equals(message.inetSocketAddress) && u.equals(message.guid)) {
                it.remove();
            }
        }

    }
    private synchronized void rememberAliveNeighbour(){
        aliveNeighbours.put((InetSocketAddress) dp.getSocketAddress(), System.currentTimeMillis());
    }

    private void sendResponse() throws IOException {
        dc.send(sendBuffer, dp.getSocketAddress());
    }
    public void getMsg(String msg){
        for (Map.Entry<InetSocketAddress, Long> entry : aliveNeighbours.entrySet()) {
            UUID uuid = UUID.randomUUID();
            Message message = new Message((InetSocketAddress)entry.getKey(), uuid, msg.getBytes());
            keepNodeMessages.add(message);
        }
    }
}
