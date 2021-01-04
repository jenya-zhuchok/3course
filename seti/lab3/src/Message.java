import java.net.InetSocketAddress;
import java.util.UUID;

public class Message {
    public InetSocketAddress inetSocketAddress;
    public UUID guid;
    public byte[] msg;

    Message(InetSocketAddress inetSocketAddress, UUID guid,byte[] msg){
        this.inetSocketAddress = inetSocketAddress;
        this.guid = guid;
        this.msg = msg;
    }
}
