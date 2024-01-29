package book.kotlinfx.ch05

import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.scene.control.Label
import javafx.scene.control.*
import book.kotlinfx.util.*
import javafx.stage.Stage
import javafx.scene.layout.StackPane
import javafx.scene.shape.Circle
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.ParallelCamera
import javafx.scene.PerspectiveCamera
import javafx.scene.input.Mnemonic
import javafx.application.Platform
import javafx.scene.Cursor
import javafx.collections.ObservableMap
import javafx.collections.ObservableList
import javafx.scene.input.KeyCombination
import javafx.scene.text.Text
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCode
import javafx.event.ActionEvent
import javafx.scene.paint.Paint
import javafx.scene.SnapshotResult
import javafx.scene.image.WritableImage
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import javafx.scene.input.KeyEvent
import javafx.event.Event
import javafx.event.EventHandler
import javafx.scene.paint.Color
import javafx.geometry.Pos
import javafx.geometry.Insets
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.HBox
import javafx.scene.layout.FlowPane
import javafx.scene.layout.BorderPane
import javafx.geometry.NodeOrientation
import javafx.geometry.Orientation
import javafx.scene.layout.GridPane
import kotlin.collections.listOf
import javafx.scene.layout.TilePane
import javafx.scene.layout.Priority
import javafx.scene.layout.AnchorPane
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.value.ObservableValue
import javafx.beans.binding.Bindings
import javafx.beans.property.DoubleProperty
import javafx.scene.canvas.Canvas


class JuliaCanvas {
	var contents = StackPane()
	lateinit var scene1:Scene
	var canvas = Canvas(400.0,400.0)

	fun start() {
		scene1 = Scene(
			VBox(
			  contents
		    ).apply{
				styleClass.add("mainStage")
				// make contents height-growable
				VBox.setVgrow(contents, Priority.ALWAYS)
			},
		    400.0, 500.0)
		
		draw() // fill contents
		
		Stage().apply {
			title = "Julia CANVAS"
			scene = scene1
			show()
		}
	}
	
	private fun draw() {
		val j = Julia(canvas)
		
		val vb = VBox(
			HBox(
				TextField(j.xc.toString()).apply{
					textProperty().addListener{_,_,v ->
						j.xc = v.toDouble()
						j.run1()
					}
				},
				TextField(j.yc.toString()).apply{
					textProperty().addListener{_,_,v ->
						j.yc = v.toDouble()
						j.run1()
					}
				},
				Button("Run"){ j.run1() }
			),
			canvas
		)
  
		with(contents.children){
		    clear(); add(vb)
		}
	}

	
	private fun Scene.customCSS():Scene {
		stylesheets.add("css/styles.css")
		return this
	}
}

class Julia(val canvas:Canvas) {
	companion object {
	    const val W = 400
	    const val H = 400
		const val ESCAPE_MAXITER = 50
		const val ESCAPE_SQUARED = 100_000_000.0
		const val COLORING_SCALE = 20
	}
	
	val pw = canvas.graphicsContext2D.pixelWriter 	
	var xc = 0.0
	var yc = -0.8
		
	fun run1() = Platform.runLater{
		for(pixCtr in 0 .. W*H-1){
			val y = pixCtr / W
			val x = pixCtr - W*y
			calcPoint(x, y)
		}
	}
	
	private fun calcPoint(xi:Int, yi:Int) {
		var x = (xi - W/2.0) / W * 4.0  // complex plane [-2..2]x[-2..2]
		var y = (yi - H/2.0) / H * 4.0
        var i = 0
		while(i < ESCAPE_MAXITER) {
			val xx = x         // this is: z' = z^2 + c
			x = x*x - y*y + xc //
			y = 2*xx*y + yc    //
			if(x*x+y*y > ESCAPE_SQUARED) break
			i++
		}
		val value = (if(i==ESCAPE_MAXITER) {0}else{i*COLORING_SCALE}) % 256
		pw.setArgb(xi,yi, (0xFF000000 + value).toInt())
	}
}
