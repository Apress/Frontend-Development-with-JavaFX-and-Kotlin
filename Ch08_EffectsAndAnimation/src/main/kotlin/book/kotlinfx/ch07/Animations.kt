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
import javafx.geometry.Insets

class Animations(val width1:Double) : VBox() {
	init {
		padding = Insets(10.0)
		
		// ----------------------------------------------------------------
		//     Scrolling Text
		// ----------------------------------------------------------------
        val scrollingText = Pane().apply{
			val msg = Text("JavaFX animation at work!").apply{
	          textOrigin = VPos.TOP
	          font = Font.font(24.0)			
			}
	 
	        val sceneWidth = width1
	        val msgWidth = msg.layoutBounds.width
	
	        // Initial and final key frames
	        val initKeyValue = KeyValue(msg.translateXProperty(), sceneWidth)
	        val initFrame = KeyFrame(Duration.ZERO, initKeyValue)
	        val endKeyValue = KeyValue(msg.translateXProperty(), -1.0 * msgWidth)
	        val endFrame = KeyFrame(Duration.seconds(3.0), endKeyValue)
			
	        Timeline(initFrame, endFrame).apply{
				cycleCount = Timeline.INDEFINITE // run forever 
			}.also{
				userData = it // make accessible from outside
			}
			
			children.add(msg)
		}
		val scrollingTextCtrl = HBox(10.0,
			Button("Pause"){ (scrollingText.userData as Timeline).pause() },
			Button("Play"){ (scrollingText.userData as Timeline).play() },
			Slider(0.5,2.0,1.0).apply{
				(scrollingText.userData as Timeline)
				.rateProperty().bind(valueProperty()) }
		)

		// ----------------------------------------------------------------
		//     Scrolling Text with Custom Interpolator
		// ----------------------------------------------------------------
        val interpolators = Pane().apply{
			val msg = Text("SQRT interpolator").apply{
	          textOrigin = VPos.TOP
	          font = Font.font(24.0)			
			}
	 
	        val sceneWidth = width1
	        val msgWidth = msg.layoutBounds.width
	
	        val kv0 = KeyValue(msg.translateXProperty(), sceneWidth)
	        val fr0 = KeyFrame(Duration.ZERO, kv0)
	        val kv1 = KeyValue(msg.translateXProperty(), -1.0 * msgWidth, Interpolator{ Math.sqrt(it) })
	        val fr1 = KeyFrame(Duration.seconds(3.0), kv1)
			
	        Timeline(fr0, fr1).apply{
				cycleCount = Timeline.INDEFINITE // run forever 
			}.also{
				userData = it // make accessible from outside
			}
			
			children.add(msg)
		}
		val interpolatorsCtrl = HBox(10.0,
			Button("Pause"){ (interpolators.userData as Timeline).pause() },
			Button("Play"){ (interpolators.userData as Timeline).play() },
			Slider(0.5,2.0,1.0).apply{
				(interpolators.userData as Timeline)
				.rateProperty().bind(valueProperty()) }
		)

		// ----------------------------------------------------------------
		//     Transition: Fading
		// ----------------------------------------------------------------
        val transiFading = HBox(5.0).apply{		
			val rect = Rectangle(200.0, 50.0, Color.RED)
			val fadeInOut = FadeTransition(Duration.seconds(2.0), rect)
			val fin = Button("Fade in"){ with(fadeInOut){ cycleCount = 1; fromValue = 0.0; toValue = 1.0; play() } }
			val fout = Button("Fade out"){ with(fadeInOut){ cycleCount = 1; fromValue = 1.0; toValue = 0.0; play() } }
			val cycle = Button("Cycle"){ with(fadeInOut){
				fromValue = 1.0; toValue = 0.0; cycleCount = 4; isAutoReverse = true; play() } }
			children.addAll(fin, fout, cycle, rect)
		}

		// ----------------------------------------------------------------
		//     Transition: Translation
		// ----------------------------------------------------------------
        val transiTranslation = HBox(5.0).apply{		
			val rect = Rectangle(50.0, 50.0, Color.RED)
			val pane = Pane(rect).apply{
				prefWidth = 200.0; minWidth = 200.0
				prefHeight = 50.0; minHeight = 50.0
			}
			val transl = TranslateTransition(Duration.seconds(2.0), rect)
			val shftr = Button("Shift R"){ with(transl){ cycleCount = 1; fromX = 0.0; toX = 150.0; play() } }
			val shftl = Button("Shift L"){ with(transl){ cycleCount = 1; fromX = 150.0; toX = 0.0; play() } }
			val cycle = Button("Cycle"){ with(transl){
				fromX = 0.0; toX = 150.0; cycleCount = 4; isAutoReverse = true; play() } }
			children.addAll(shftr, shftl, cycle, pane)
		}
		
		// ----------------------------------------------------------------
		// ----------------------------------------------------------------
        children.add(ScrollPane( VBox(15.0,
			scrollingText, scrollingTextCtrl, VSTRUT(5),
		    interpolators, interpolatorsCtrl, VSTRUT(5),
			transiFading,
			transiTranslation
		) ).apply{style = "-fx-background-color:transparent;"} )
	}

	private val String.t get() = Text(this)
	private fun GridPane.getAt(column:Int, row:Int):Node? =
		children.find{GridPane.getColumnIndex(it) == column && GridPane.getRowIndex(it) == row }
	private fun GridPane.atTop(n:Node?){ GridPane.setFillHeight(n,false) 
			                            GridPane.setValignment(n,VPos.TOP) }
	private fun Interpolator(curvex:(d:Double)->Double) =
		object : Interpolator(){ override fun curve(v:Double) = curvex(v) }
	
}