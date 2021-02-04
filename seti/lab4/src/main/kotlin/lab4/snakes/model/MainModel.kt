package lab4.snakes.model

import lab4.snakes.controller.GameViewController

import lab4.snakes.SnakesProto
import java.lang.Thread.sleep
import java.net.InetAddress
import java.net.InetSocketAddress
import kotlin.concurrent.thread

class MainModel(private val gvc : GameViewController,
            val masterAddr : InetSocketAddress?,
            val gameConfig : SnakesProto.GameConfig,
            bindPort  : Int?) {
    companion object {
        private val multicastSocketAddress = InetSocketAddress(InetAddress.getByName("224.0.0.7"), 9000)
    }

    var gameTickThread : Thread? = null
    var msgSenderReceiver : MsgSenderReceiver = MsgSenderReceiver(bindPort, this)
    var gameModel : GameModel = GameModel(this, gameConfig)
    var playerName : String = "test"

    fun redrawWindow() {
        gvc.redrawWindow()
    }


    fun start() {
        if (masterAddr==null) {
            val playerId = gameModel.addPlayerWithSnake(null, SnakesProto.NodeRole.MASTER, playerName) ?: 1
            gameModel.setMyPlayerIdIfNull(playerId)

            gameTickThread = thread{
                while (true) {
                    announceGameAndSendStatePlayers()
                    sleep(gameConfig.stateDelayMs.toLong())
                    gameModel.makeStep()
                }
            }
        } else {
            val msgBuilder = SnakesProto.GameMessage.newBuilder()
                val joinMsgBuilder = SnakesProto.GameMessage.JoinMsg.newBuilder()
                joinMsgBuilder.name = "test"
            msgBuilder.join = joinMsgBuilder.build()
            msgSenderReceiver.send(masterAddr, msgBuilder)
        }
    }

    fun doGameOver(score : Int) {
        gvc.gameOver(score)
    }

    private fun announceGameAndSendStatePlayers() {
        val playersSerialized = gameModel.serializePlayers()
        val msgBuilder = SnakesProto.GameMessage.newBuilder()
            val anBuilder = SnakesProto.GameMessage.AnnouncementMsg.newBuilder()
            anBuilder.config = gameConfig
            anBuilder.players = playersSerialized
            anBuilder.canJoin = true
            msgBuilder.announcement = anBuilder.build()
        msgSenderReceiver.send(multicastSocketAddress, msgBuilder)

        val stateMsg = gameModel.serializeStateMessage(playersSerialized)
        for ((playerId, playerInfo) in gameModel.players) {
            if (playerInfo.addr!=null) {
                msgSenderReceiver.send(playerInfo.addr, stateMsg)
            }
        }
    }

    fun msgReceivedCallback(from : InetSocketAddress, msg : SnakesProto.GameMessage, ackMsg : SnakesProto.GameMessage.Builder) {
        var name : String = "player" + GameModel.nextPlayerId
        //var name : String? = msg.announcement.players.playersList[0].name
        //if(name == null) name = "player" + GameModel.nextPlayerId
        if (msg.hasJoin()) {
            val newSnakeId = gameModel.addPlayerWithSnake(from, SnakesProto.NodeRole.NORMAL,name)
            if (newSnakeId!=null) {
                ackMsg.receiverId = newSnakeId
            }
        }
        if (msg.hasAck()) {
            gameModel.setMyPlayerIdIfNull(msg.receiverId)
        }

        if (msg.hasState()) {
            gameModel.deserializeState(from, msg.state.state)
        }

        if (msg.hasSteer()) {
            gameModel.rememberSteer(msg.senderId, msg.steer.direction)
        }
    }

    fun destroy() {
        gameTickThread?.interrupt()
        msgSenderReceiver.stop()
    }
}