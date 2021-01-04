import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException{

        InetSocketAddress parentInetSocAddr = null;
        if(args.length == 5){
            parentInetSocAddr = new InetSocketAddress(InetAddress.getByName(args[3]), Integer.parseInt(args[4]));
        }

        MyThread myThread = new MyThread(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), parentInetSocAddr);
        Thread thread = new Thread(myThread);
        thread.start();
        Scanner in = new Scanner(System.in);

        while(true){
            String msg = in.nextLine();
            /*try{
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }*/
            myThread.getNode().getMsg(args[0].toString() + ": " + msg);
        }
    }
}
