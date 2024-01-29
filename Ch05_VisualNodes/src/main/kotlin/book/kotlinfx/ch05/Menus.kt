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
import javafx.scene.control.ButtonBar.ButtonData
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.image.ImageView
import javafx.scene.image.Image

class Menus {
	lateinit var stage:Stage
	lateinit var scene1:Scene
	
	fun start() {
		val txt = Text()
		val mb = makeMenu(txt)
		
		val editWithContextMenu = TextField().apply {
			text = "Click Mouse-Right for context menu"
			contextMenu = makeContextMenu(this) }
		
		scene1 = Scene(
			VBox(
				mb,
				VBox(
					txt,
					editWithContextMenu
				).apply{ padding = Insets(10.0)}
		    ),
		    400.0, 400.0).customCSS()
				
		stage = Stage().apply {
			title = "Menus"
			scene = scene1
			show()
		}
	}
			
	private fun Scene.customCSS():Scene {
		stylesheets.add("css/styles.css")
		return this
	}
	
	val monitorChanges = SimpleBooleanProperty()
	private fun makeMenu(status:Text) = MenuBar(
			Menu("File", null,
				MenuItem("Open"){ status.text = "Open clicked" },
				MenuItem("Save"){ status.text = "Save clicked" },
				SeparatorMenuItem(),
				MenuItem("Quit"){ stage.close() }
			),
			Menu("Edit", null,
				MenuItem("Find"){ status.text = "Find clicked" },
				MenuItem("Replace"){ status.text = "Replace clicked" },
				Menu("Statistics",null,
				    MenuItem("Words"){ status.text = "Words clicked" },
				    MenuItem("Characters"){ status.text = "Characters clicked" }
				),
				CheckMenuItem("Monitor Changes").apply{
					selectedProperty().bindBidirectional(monitorChanges) }
			),
			Menu("Frog", ImageView(Image("images/frog.jpg",20.0,20.0,false,true)))
		)
	
	fun makeContextMenu(edit:TextField) = ContextMenu(
			MenuItem("Clear"){ edit.text = "" },
			MenuItem("Hello"){ edit.text = "Hello" }
		)
}