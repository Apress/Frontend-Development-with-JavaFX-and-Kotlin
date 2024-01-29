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

class ControlPanes {
	var contents = StackPane()
	lateinit var scene1:Scene
	
	fun start() {
		scene1 = Scene(
			VBox(
				ToolBar(
					Button("Scrollpanes") { scrollPane()},
					Button("Accordion") { accordion()},
					Button("Tabs") { tabs()},
					Button("Split Panes") { splitPanes()}
				),
			    contents
		    ).apply{
				styleClass.add("mainStage")
				// make contents height-growable
				VBox.setVgrow(contents, Priority.ALWAYS)
			},
		    400.0, 400.0)
		.apply {
			cursor = Cursor.HAND
		}
		
		Stage().apply {
			title = "Panes"
			scene = scene1
			show()
		}
	}
	
	private fun tabs() {
		val tabs = TabPane(
			"Pane 1" to VBox(
				Text("I am pane #1")
			),
			"Pane 2" to VBox(
				Text("I am pane #2")
			),
			"Pane 3" to VBox(
				Text("I am pane #3")
			)
		).apply{
			// tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
		}
  
		with(contents.children){
		    clear()
			add(
		        VBox(tabs)
			)
		}
	}

	private fun splitPanes() {
        val p1 = BorderPane( Text("I'm pane 1") )
        val p2 = BorderPane( Text("I'm pane 2") )		
		
		with(contents.children){
		    clear()
			add(
		        SplitPane(p1, p2).apply{
					orientation = Orientation.HORIZONTAL
				}
			)
		}
	}

	private fun accordion() {
		val acc1 = Accordion(
			TitledPane("Pane 1",VBox(
				Text("I am pane #1")
			)),
			TitledPane("Pane 2",VBox(
				Text("I am pane #2")
			)),
			TitledPane("Pane 3",VBox(
				Text("I am pane #3")
			))
		)
		val acc2 = Accordion(
			"Pane 1" to VBox(
				Text("I am pane #1")
			),
			"Pane 2" to VBox(
				Text("I am pane #2")
			),
			"Pane 3" to VBox(
				Text("I am pane #3")
			)
		)
  
		with(contents.children){
		    clear()
			add(
		        VBox(acc1, VSTRUT(20), acc2)
			)
		}
	}

	private fun scrollPane() {
		val vb = VBox()
		
		(1..100).forEach{ i ->
			vb.children.add(Text("I'm Text #${i}"))
		}
		
		val vValProp = SimpleDoubleProperty(0.0)
		val vValStr:ObservableValue<String> = bindingNumberToString(vValProp) 
				
		val sp = ScrollPane( vb ).apply{
			isPannable = true
			isFitToWidth = true
            isFitToHeight = true
			vvalueProperty().bindBidirectional(vValProp)
		}
  
		with(contents.children){
		    clear()
			add(
				VBox( 5.0,
					HBox( 5.0, Button("Hide scrollbars"){ sp.vbarPolicy = ScrollPane.ScrollBarPolicy.NEVER },
					      Button("Show scrollbars"){ sp.vbarPolicy = ScrollPane.ScrollBarPolicy.AS_NEEDED } ),
					HBox( 5.0, Button("Scroll to 0.5"){ vValProp.set(0.5) },
						  Text(vValStr) ),
					Button("Add change listener"){
						sp.vvalueProperty().addListener{ observable, /*oldVal*/_, /*newVal*/_ ->
							when(observable) {
								is DoubleProperty -> println(observable.value) 
							} }
					},
			        sp
				)
			)
		}
	}
		
	private fun Scene.customCSS():Scene {
		stylesheets.add("css/styles.css")
		return this
	}
}