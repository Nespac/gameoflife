import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ColorPicker
import javafx.scene.control.Slider
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.text.Text

object ButtonGrid {
    val buttonGrid1 = GridPane()
    val buttonGrid2 = GridPane()
    val stepButton = Button("one step")
    val startButton = Button("start")
    val stopButton = Button("stop")
    val resetButton = Button("reset")
    val boardSizeBox = NumberTextField
    val redrawButton = Button("set size ->")
    val description = Text("Hint: \nClick or drag on the board with the LEFT MOUSE BUTTON to produce new cells!\nClick or drag on the board with the RIGHT MOUSE BUTTON to delete existing cells!")
    val deadColorDesc = Text("dead color")
    val deadColorPicker = ColorPicker()
    val aliveColorDesc = Text("alive color")
    val aliveColorPicker = ColorPicker()
    val speedSlider = Slider(10.0, 500.0, 150.0)
    val sliderDesc = Text("adjust step delay -> - +")

    init{
        buttonGrid1.add(stepButton, 0, 0)
        buttonGrid1.add(boardSizeBox, 5, 0)
        buttonGrid1.add(redrawButton, 4, 0)
        buttonGrid1.add(deadColorDesc, 1, 0)
        buttonGrid1.add(deadColorPicker, 1, 1)
        buttonGrid1.add(aliveColorDesc, 2, 0)
        buttonGrid1.add(aliveColorPicker, 2, 1)
        buttonGrid1.add(sliderDesc, 4,1)
        buttonGrid1.add(speedSlider, 5,1)
        buttonGrid2.add(startButton, 0, 0)
        buttonGrid2.add(stopButton, 1, 0)
        buttonGrid2.add(resetButton, 2, 0)

        buttonGrid1.apply {
            padding = Insets(10.0,0.0,10.0,0.0)
            alignment = Pos.CENTER
            hgap = 10.0
            vgap = 5.0
        }
        buttonGrid2.apply {
            padding = Insets(10.0,0.0,10.0,0.0)
            alignment = Pos.CENTER
            hgap = 10.0
            vgap = 5.0
        }
        boardSizeBox.apply {
            text = "50"
            maxWidth = 50.0
        }
        aliveColorPicker.apply {
            value = Color.BLACK
            maxWidth = 70.0
        }
        deadColorPicker.apply {
            value = Color.GRAY
            maxWidth = 70.0
        }
        description.apply {
            style = "-fx-font-weight: bold"
        }
        stepButton.apply {
            minWidth = 70.0
        }
        startButton.apply {
            minWidth = 70.0
        }
        stopButton.apply {
            minWidth = 70.0
        }
        resetButton.apply {
            minWidth = 70.0
        }
        redrawButton.apply {
            minWidth = 50.0
        }
    }

    object NumberTextField : TextField(){
        override fun replaceText(start: Int, end: Int, text: String?) {
            if (validate(text)){
                super.replaceText(start, end, text)
            }
        }

        override fun replaceSelection(text: String?) {
            if(validate(text)){
                super.replaceSelection(text)
            }
        }

        private fun validate(text:String?):Boolean{
            return text!!.matches(Regex("[0-9]*"))
        }
    }
}
