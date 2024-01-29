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

class DialogButtons {
	lateinit var stage:Stage
	lateinit var scene1:Scene
	
	fun start() {
		val bb = ButtonBar().apply{ buttons.addAll(
			Button("Yes"){ println("YES pressed") }.apply{ ButtonBar.setButtonData(this, ButtonData.YES) },
			Button("No"){ println("NO pressed") }.apply{ ButtonBar.setButtonData(this, ButtonData.NO) },
			Button("Close"){ stage.close() }.apply{ ButtonBar.setButtonData(this, ButtonData.CANCEL_CLOSE) }
		) }
			
		scene1 = Scene(
			VBox(
			  Text("""
This is a dialog. We want to show the
Operating System ordering dialog-related
buttons. 
              """),
				bb
		    ).apply{ styleClass.add("dialog-contents") },
		    400.0, 400.0).customCSS()
		
		
		stage = Stage().apply {
			title = "Dialog Buttons"
			scene = scene1
			show()
		}
	}
			
	private fun Scene.customCSS():Scene {
		stylesheets.add("css/styles.css")
		return this
	}
}