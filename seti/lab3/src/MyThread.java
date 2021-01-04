import java.net.InetSocketAddress;

public class MyThread implements Runnable {
    private Node node;

    @Override
    public void run(){
        try {
            node.start();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    MyThread(String name, int percents, int port, InetSocketAddress parentIneSocAddr){
        node = new Node(name, percents,  port, parentIneSocAddr);
    }

    public Node getNode(){
        return node;
    }
}
