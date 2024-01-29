package book.kotlinfx.ch01

import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.stage.Stage

import book.kotlinfx.util.* // from Ch00_Util

fun main(args:Array<String>) {
	Application.launch(HelloWorld::class.java, *args)
}
 
class HelloWorld : Application() {
	override
	fun start(primaryStage:Stage) {
		primaryStage.title = "Hello World!"

//		val btn = Button().apply { 
//		    text = "Say 'Hello World'"
//		    setOnAction { _ ->
//		        println("Hello World!")
//		    }
//		}
//		val btn = Button("Say 'Hello World'").apply { 
//		    setOnAction { _ ->
//		        println("Hello World!")
//		    }
//		}
		val btn = Button("Say 'Hello World'"){
		    println("Hello World!")
		}
		
		val root = StackPane().apply {
		    children.add(btn)
		}
		
		with(primaryStage){
		    scene = Scene(root, 300.0, 250.0)
		    show()
		}
	}
}
