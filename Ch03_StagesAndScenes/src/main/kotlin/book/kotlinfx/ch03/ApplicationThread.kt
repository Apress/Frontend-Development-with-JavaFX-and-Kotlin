package book.kotlinfx.ch03

import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.scene.control.Label
import javafx.scene.control.*
import book.kotlinfx.util.*
import javafx.stage.Stage
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import javafx.scene.shape.Circle
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.application.Platform

class ApplicationThread {
	var contents = VBox()
	
	fun start() {
		val scene1 = Scene(
			VBox(
			  Button("Won't work"){
				  Thread{
				    contents.children.add(Label("Added"))  
				  }.start()
			  },
			  Button("Works"){
				  Thread{
				      Platform.runLater{
				          contents.children.add(Label("Added"))
				      }
				  }.start()
			  },
			  contents
		    ).apply{ styleClass.add("mainStage") },
		400.0, 400.0).customCSS()
		
		Stage().apply {
			title = "Application Thread"
			scene = scene1
			show()
		}
	}
		
	private fun Scene.customCSS():Scene {
		stylesheets.add("css/styles.css")
		return this
	}
}