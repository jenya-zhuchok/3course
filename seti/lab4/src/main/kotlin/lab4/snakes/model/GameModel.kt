package lab4.snakes.model

import lab4.snakes.SnakesProto
import java.net.InetSocketAddress
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet


class Point(var x : Int, var y : Int) {
    override fun equals(other : Any?) : Boolean {
        val rhs = other as Point?
        return (x==rhs!!.x) && (y==rhs.y)
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}


class GameModel(
    private val mainModel : MainModel,
    val gameConfig : SnakesProto.GameConfig) {

    companion object CellTypes {
        const val CELL_EMPTY: Int = -1
        const val CELL_FOOD: Int = -2
        var nextPlayerId = 1
        var playersNames: java.util.HashMap<Int, String> = java.util.HashMap()
    }

    class PlayerInfo(val addr: InetSocketAddress?, val role: SnakesProto.NodeRole)

    var myPlayerId: Int? = null
    var stateId = 0
    val fieldContents: Array<Array<Int>> = Array(gameConfig.width) { Array(gameConfig.height) { CELL_EMPTY } }
    val foodCells: HashSet<Point> = HashSet()
    val snakes: HashMap<Int, SnakeInfo> = HashMap()
    var players: java.util.HashMap<Int, PlayerInfo> = java.util.HashMap()


    val blessRng: Random = Random()

    @Synchronized
    fun addSnake(playerId: Int): Boolean { //TODO: normal initialization
        for (i in 0 until gameConfig.width) {
            for (j in 0 until gameConfig.height) {
                if (fieldContents[i][j] == CELL_EMPTY) {
                    fieldContents[i][j] = playerId
                    snakes[playerId] = SnakeInfo(this, i, j)
                    return true
                }
            }
        }
        return false
    }

    @Synchronized
    fun addPlayer(addr: InetSocketAddress?, role: SnakesProto.NodeRole, name: String): Int {
        val playerId = nextPlayerId++
        players[playerId] = PlayerInfo(addr, role)
        playersNames[playerId] = name
        return playerId
    }

    @Synchronized
    fun addPlayerWithSnake(addr: InetSocketAddress?, role: SnakesProto.NodeRole, name: String): Int? {
        val playerId = nextPlayerId
        if (!addSnake(playerId)) return null
        addPlayer(addr, role, name)
        return playerId
    }

    @Synchronized
    fun setMyPlayerIdIfNull(playerId: Int) {
        if (myPlayerId == null) {
            myPlayerId = playerId
        }
    }

    @Synchronized
    fun makeStep() {
        for ((_, snakeInfo) in snakes) {
            snakeInfo.move()
        }
        stateId++;
        repaint()
    }

    @Synchronized
    fun repaint() {
        for (i in 0 until gameConfig.width) {
            for (j in 0 until gameConfig.height) {
                fieldContents[i][j] = CELL_EMPTY
            }
        }

        // запоминаем змеек, которые врезались
        val snakes2erase: ArrayList<Int> = ArrayList()
        for ((playerId, snakeInfo) in snakes) {
            val drawnCells: ArrayList<Point> = ArrayList()
            for (it in snakeInfo.cells) {
                if (fieldContents[it.x][it.y] != CELL_EMPTY) {
                    for (it2 in drawnCells) {
                        fieldContents[it2.x][it2.y] = CELL_EMPTY
                    }
                    snakes2erase.add(playerId)
                } else {
                    fieldContents[it.x][it.y] = playerId
                    drawnCells.add(it)
                }
            }
        }

        // убиваем их
        for (snakeId in snakes2erase) {
            destroySnake(snakeId)
        }

        //расставляем еду
        val foodOnFieldExpected: Int = (gameConfig.foodStatic + gameConfig.foodPerPlayer * snakes.size).toInt()
        while (foodCells.size < foodOnFieldExpected) {
            val newPos: Point = Point(blessRng.nextInt(gameConfig.width), blessRng.nextInt(gameConfig.height))
            if (!(foodCells.contains(newPos)) && fieldContents[newPos.x][newPos.y] == CELL_EMPTY) {
                foodCells.add(newPos)
            }
        }

        for (it in foodCells) {
            fieldContents[it.x][it.y] = CELL_FOOD
        }

        mainModel.redrawWindow()
    }

    @Synchronized
    private fun destroySnake(playerId: Int) {
        //if (playerId==myPlayerId) {
        //mainModel.doGameOver(getMyScore())

        //}
        //TODO: act as viewer
        snakes.remove(playerId)
    }

    @Synchronized
    fun rememberSteer(playerId: Int, dir: SnakesProto.Direction) {
        //направдение змеи
        snakes[playerId]?.rememberSteer(dir)
    }

    @Synchronized
    fun rememberMySteer(dir: SnakesProto.Direction) {
        if (myPlayerId != null) {
            rememberSteer(myPlayerId!!, dir)
        }
    }

    @Synchronized
    fun getMyScore(): Int {
        return snakes[myPlayerId]!!.score
    }


    @Synchronized
    fun deserializeState(from: InetSocketAddress, gameState: SnakesProto.GameState) {
        if (gameState.stateOrder <= stateId) return //drop expired state
        foodCells.clear()
        for (it in gameState.foodsList) {
            foodCells.add(Point(it.x, it.y))
        }

        snakes.clear()
        for (it in gameState.snakesList) {
            val snakeId = it.playerId
            val cells = LinkedList<Point>()
            for (it2 in it.pointsList) {
                cells.add(Point(it2.x, it2.y))
            }
            snakes[snakeId] = SnakeInfo(this, cells, it.headDirection, 0)
        }

        players.clear()
        for (it in gameState.players.playersList) {
            val addr = if (it.ipAddress == "") from else InetSocketAddress(it.ipAddress, it.port)
            players[it.id] = PlayerInfo(addr, it.role)
            snakes[it.id]?.score = it.score
            playersNames[it.id] = it.name
        }

        repaint()
    }

    @Synchronized
    fun serializePlayers(): SnakesProto.GamePlayers {
        val plBuilder = SnakesProto.GamePlayers.newBuilder()
        val pBuilder = SnakesProto.GamePlayer.newBuilder()
        for ((playerId, playerInfo) in players) {
            //pBuilder.name = "test"
            pBuilder.name = playersNames[playerId]
            pBuilder.id = playerId
            pBuilder.ipAddress = if (playerInfo.addr == null) "" else playerInfo.addr.address.toString()
            pBuilder.port = if (playerInfo.addr == null) 0 else playerInfo.addr.port
            pBuilder.role = playerInfo.role
            pBuilder.score = 0
            if (snakes[playerId] != null) {
                pBuilder.score = snakes[playerId]!!.score
            }
            plBuilder.addPlayers(pBuilder.build())
        }
        return plBuilder.build()
    }

    @Synchronized
    fun serializeStateMessage(serializedPlayers: SnakesProto.GamePlayers): SnakesProto.GameMessage.Builder {
        val msgBuilder2 = SnakesProto.GameMessage.newBuilder()
        val stateMsgBuilder = SnakesProto.GameMessage.StateMsg.newBuilder()
        val stateBuilder = SnakesProto.GameState.newBuilder()
        stateBuilder.stateOrder = stateId
        for ((snakeId, snakeInfo) in snakes) {
            val snakeBuilder = SnakesProto.GameState.Snake.newBuilder()
            snakeBuilder.playerId = snakeId
            snakeBuilder.state = SnakesProto.GameState.Snake.SnakeState.ALIVE
            snakeBuilder.headDirection = snakeInfo.dir

            for (snakeCell in snakeInfo.cells) {
                val pb = SnakesProto.GameState.Coord.newBuilder()
                pb.x = snakeCell.x
                pb.y = snakeCell.y
                snakeBuilder.addPoints(pb.build())
            }
            stateBuilder.addSnakes(snakeBuilder.build())
        }

        for (foodCell in foodCells) {
            val pb = SnakesProto.GameState.Coord.newBuilder()
            pb.x = foodCell.x
            pb.y = foodCell.y
            stateBuilder.addFoods(pb.build())
        }
        stateBuilder.players = serializedPlayers
        stateBuilder.config = gameConfig
        stateMsgBuilder.state = stateBuilder.build()
        msgBuilder2.state = stateMsgBuilder.build()
        msgBuilder2.msgSeq = 0
        return msgBuilder2
    }

    @Synchronized
    fun _getMyPlayerId(): Int? {
        return myPlayerId
    }
}