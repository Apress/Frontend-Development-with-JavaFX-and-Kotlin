package book.kotlinfx.ch07

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.effect.*
import javafx.scene.layout.StackPane
import javafx.stage.Stage

import book.kotlinfx.util.*
import javafx.scene.Node
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.paint.Stop
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.CycleMethod
import javafx.scene.text.Text
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.Group
import javafx.scene.layout.VBox
import javafx.scene.layout.Pane
import javafx.scene.layout.GridPane
import javafx.scene.control.Slider
import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.shape.Circle
import javafx.animation.KeyValue
import javafx.animation.KeyFrame
import javafx.util.Duration
import javafx.animation.Timeline
import javafx.animation.Interpolator
import javafx.animation.FadeTransition
import javafx.animation.TranslateTransition

fun main(args:Array<String>) {
	Application.launch(App::class.java, *args)
}
 
class App : Application() {
	lateinit var scene:Scene
	
	override fun start(primaryStage:Stage) {
		primaryStage.title = "Effects And Animation"
		val root = StackPane()
		scene = Scene(root, 600.0, 600.0)
		root.children.add(
		    TabPane("Effects" to Effects, "Animation" to Animations(scene.width))
		)
		with(primaryStage){
		    scene = this@App.scene
		    show()
		}
	}
	
	private val String.t get() = Text(this)
	private fun Interpolator(curvex:(d:Double)->Double) =
		object : Interpolator(){ override fun curve(v:Double) = curvex(v) }

}
