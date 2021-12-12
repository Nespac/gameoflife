import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseButton
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import kotlin.math.floor

class App : Application() {
    private var STARTED = false

    private var gameLogic = GameLogic(50)

    companion object {
        private const val WIDTH = 700
        private const val HEIGHT = 700
    }

    private lateinit var mainScene: Scene

    private var lastFrameTime: Long = System.nanoTime()

    private val buttonGrid = ButtonGrid

    private val canvas = Canvas(WIDTH.toDouble(), HEIGHT.toDouble())

    override fun start(mainStage: Stage) {
        mainStage.title = "Game of Life"
        val root = VBox()
        mainScene = Scene(root)
        mainStage.scene = mainScene

        root.children.addAll(canvas, buttonGrid.buttonGrid2, buttonGrid.buttonGrid1, buttonGrid.description)
        prepareActionHandlers()

        //starter cells
        gameLogic.currentBoard[20][20] = gameLogic.ALIVE
        gameLogic.currentBoard[20][21] = gameLogic.ALIVE
        gameLogic.currentBoard[20][22] = gameLogic.ALIVE

        gameLogic.currentBoard[30][30] = gameLogic.ALIVE
        gameLogic.currentBoard[31][30] = gameLogic.ALIVE
        gameLogic.currentBoard[30][31] = gameLogic.ALIVE
        gameLogic.currentBoard[31][31] = gameLogic.ALIVE

        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                if(STARTED){
                    buttonGrid.buttonGrid1.isDisable = true
                    gameLogic.step()
                    Thread.sleep(buttonGrid.speedSlider.value.toLong())
                }else{
                    buttonGrid.buttonGrid1.isDisable = false
                }
                tickAndRender(currentNanoTime)
            }
        }.start()

        mainStage.show()
    }

    private fun tickAndRender(currentNanoTime: Long) {

        val elapsedNanos = currentNanoTime - lastFrameTime
        lastFrameTime = currentNanoTime

        // clear canvas
        val graphicsContext = canvas.graphicsContext2D
        graphicsContext.fill = buttonGrid.deadColorPicker.value
        graphicsContext.fillRect(0.0,0.0,canvas.width, canvas.height)
        graphicsContext.fill = buttonGrid.aliveColorPicker.value
        val widthRatio = canvas.width/gameLogic.boardsize
        val heightRatio = canvas.height/gameLogic.boardsize
        (0 until gameLogic.boardsize).forEach { i ->
            (0 until gameLogic.boardsize)
                .asSequence()
                .filter { gameLogic.currentBoard[i][it] == gameLogic.ALIVE }
                .forEach {
                    graphicsContext.fillRect(
                        it.toDouble() * heightRatio,
                        i.toDouble() * widthRatio,
                        heightRatio,
                        widthRatio,
                    )
                }
        }
        graphicsContext.stroke = Color.DARKGRAY
        graphicsContext.lineWidth = 0.5
        (0 .. gameLogic.boardsize).forEach { i ->
            graphicsContext.strokeLine(0.0, i.toDouble()*widthRatio, canvas.width, i.toDouble()*widthRatio)
            graphicsContext.strokeLine(i.toDouble()*heightRatio, 0.0, i.toDouble()*heightRatio, canvas.height)
        }

        // display crude fps counter
        val elapsedMs = elapsedNanos / 1_000_000
        if (elapsedMs != 0L) {
            graphicsContext.fill = Color.WHITE
            graphicsContext.fillText("${1000 / elapsedMs} fps", 10.0, 10.0)
        }
    }


    private fun prepareActionHandlers() {

        canvas.setOnMousePressed(this::handleMouseDraw)
        canvas.setOnMouseDragged(this::handleMouseDraw)

        buttonGrid.redrawButton.setOnAction {
            if (!(buttonGrid.boardSizeBox.text == "" || buttonGrid.boardSizeBox.text == "0"))
                let {
                    buttonGrid.resetButton.isDisable = true
                    this.gameLogic = GameLogic(this.buttonGrid.boardSizeBox.text.toInt())
                }
        }

        buttonGrid.resetButton.setOnAction{
            let { app ->
                app.gameLogic.currentBoard = app.gameLogic.backupBoard.clone()
            }
        }

        buttonGrid.startButton.setOnAction {
            let { app ->
                app.gameLogic.backupBoard = app.gameLogic.currentBoard.clone()
                app.STARTED = true
                buttonGrid.resetButton.isDisable = false
            }
        }

        buttonGrid.stopButton.setOnAction {
            STARTED = false
        }

        buttonGrid.stepButton.setOnAction {
            run {
                gameLogic.step()
            }
        }
    }

    private fun handleMouseDraw(event: javafx.scene.input.MouseEvent) {
        val mouseX = floor(event.x*gameLogic.boardsize/canvas.width).toInt()
        val mouseY = floor(event.y*gameLogic.boardsize/canvas.height).toInt()
        if (event.button==MouseButton.SECONDARY){
            gameLogic.currentBoard[mouseY][mouseX] = gameLogic.DEAD
        } else if (event.button==MouseButton.PRIMARY){
            gameLogic.currentBoard[mouseY][mouseX] = gameLogic.ALIVE
        }
    }
}
