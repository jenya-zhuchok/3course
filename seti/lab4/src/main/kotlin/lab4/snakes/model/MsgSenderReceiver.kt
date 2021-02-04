package lab4.snakes.model

import lab4.snakes.SnakesProto
import java.lang.Thread.sleep
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.thread

class MsgSenderReceiver(bindPort : Int?, val mainModel : MainModel) {
    companion object {
        val sleepDelay : Long = 10
    }
    private var curSeq : Long = 1
    class QueueMsg(val to : InetSocketAddress, val msg : SnakesProto.GameMessage, val isSeq : Boolean)

    val socket = if (bindPort==null) DatagramSocket() else DatagramSocket(bindPort)
    val msgQueue = ConcurrentLinkedQueue<QueueMsg>()

    val sendThread = thread{
        while (!Thread.interrupted()) {
            val msg = msgQueue.poll()
            if (msg==null) {
                sleep(sleepDelay)
            } else {
                val msgBytes = msg.msg.toByteArray()
                socket.send(DatagramPacket(msgBytes, msgBytes.size, msg.to))
            }
        }
    }

    val receiveThread = thread{
        val maxUdpSize = 65000
        val buf = ByteArray(maxUdpSize)
        val dp = DatagramPacket(buf, buf.size)
        while (!Thread.interrupted()) {
            socket.receive(dp)
            val prefBuf = buf.copyOf(dp.length)
            val msg = SnakesProto.GameMessage.parseFrom(prefBuf)
            val sender = dp.socketAddress as InetSocketAddress

            val ackMsg = SnakesProto.GameMessage.newBuilder()
            ackMsg.ack = SnakesProto.GameMessage.AckMsg.newBuilder().build()
            ackMsg.msgSeq = msg.msgSeq
            mainModel.msgReceivedCallback(sender, msg, ackMsg)

            send(sender, ackMsg, false)
        }
    }

    fun send(to : InetSocketAddress, msgBuilder : SnakesProto.GameMessage.Builder, isSeq: Boolean = true) {
        val playerId = mainModel.gameModel._getMyPlayerId()
        if (playerId!=null) {
            msgBuilder.senderId = playerId
        }
        msgQueue.add(QueueMsg(to, if (isSeq) msgBuilder.setMsgSeq(curSeq++).build() else msgBuilder.build(),isSeq))
    }

    fun stop() {
        sendThread.interrupt()
        receiveThread.interrupt()
    }
}