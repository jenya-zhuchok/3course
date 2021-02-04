package lab4.snakes.view

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.input.KeyEvent
import javafx.stage.Stage
import lab4.snakes.controller.DestroyableController
import lab4.snakes.controller.GameViewController
import java.io.IOException

var appInstance : SnakeApp? = null
class SnakeApp : Application() {
    var primaryStage : Stage? = null
    var fxmlloader : FXMLLoader? = null
    enum class Layouts {
        MAIN, NEW_GAME, GAME
    }
    override fun start(primaryStage: Stage?) {
        appInstance = this
        this.primaryStage = primaryStage
        this.primaryStage!!.title = "Змейка"

        try {
            showLayout(Layouts.MAIN)
        } catch (e : IOException) {e.printStackTrace()}
    }

    fun showLayout(layout : Layouts) {
        fxmlloader?.getController<DestroyableController>()?.destroy()
        var path = ""
        if (layout==Layouts.MAIN) {
            path =  "/../../resources/main/main_layout.fxml"
        }
        if (layout==Layouts.NEW_GAME) {
            path =  "/../../resources/main/new_game_layout.fxml"
        }
        if (layout==Layouts.GAME) {
            path =  "/../../resources/main/game_layout.fxml"
        }
        fxmlloader = FXMLLoader()
        fxmlloader!!.location = SnakeApp::class.java.getResource(path)
        this.primaryStage!!.scene = Scene(fxmlloader!!.load<Parent?>())
        if (layout==Layouts.GAME) {
            this.primaryStage!!.scene.addEventFilter(KeyEvent.KEY_PRESSED) {
                (fxmlloader!!.getController() as GameViewController?)!!.onKeyPressed(it)
            }
        }
        this.primaryStage!!.show()
    }
}