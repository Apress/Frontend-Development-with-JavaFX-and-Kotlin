package book.kotlinfx.ch03

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import javafx.scene.control.*
import javafx.stage.Screen
import javafx.application.Platform
import javafx.geometry.Rectangle2D

import book.kotlinfx.util.*
import javafx.scene.layout.VBox
import javafx.geometry.Insets
import javafx.stage.Modality

fun main(args: Array<String>) {
	Application.launch(App::class.java, *args)
}

class App : Application() {
	
	override
	fun init(){
		val args: Application.Parameters = parameters
		println("Named args: "+args.named)
		println("Unnamed args: "+args.unnamed)
		// ...
	}
	
	override
	fun start(primaryStage: Stage) {
		primaryStage.title = "Stages And Scenes"

		val  screenList = Screen.getScreens()
		println("# of Screens: ${screenList.size}")
		screenList.forEach{ scrn ->
			printInfo(scrn)
		}
		
		val root = VBox(
			Button("Open secondary owned stage"){
		        secondaryOwnedStage(primaryStage)				
			},
			Button("Open secondary modal stage"){
		        secondaryModalStage(primaryStage)				
			},
			Button("Open secondary top-level stage"){
		        secondaryTopStage()				
			},
			Button("Movable Scenes"){
		        MovableScenes(primaryStage.x, primaryStage.y)				
			},
			Button("Scene Properties"){
		        SceneProperties().start()		
			},
			Button("Application Thread"){
		        ApplicationThread().start()		
			}
		).apply{ styleClass.add("mainStage")  }

		with(primaryStage) {
			scene = Scene(root, 400.0, 350.0).customCSS()
			show()
		}	
	}
	
	private fun printInfo( s:Screen ) {
        println("""
            |DPI:  ${s.dpi}
            |Screen Bounds: ${info(s.bounds)}
            |Screen Visual Bounds: ${info(s.visualBounds)}
            |Output Scale X: ${s.outputScaleX} 
            |Output Scale Y: ${s.outputScaleY} 
        """.trimMargin())
    }
		
    private fun info( r:Rectangle2D ):String {
        return String.format("minX=%.2f, minY=%.2f, width=%.2f, height=%.2f",
            r.minX, r.minY, r.width, r.height)
    }
	
	private fun secondaryOwnedStage(primaryStage:Stage) {
		Stage().apply {
			title = "Secondary Owned Stage"
			initOwner(primaryStage)
			scene = Scene(VBox(
			    Label("I'm an owned secondary stage"),
		        Label("You cannot put me behind my owner"),
		        Label("I'm not modal, though")
		    ), 300.0, 250.0).customCSS()
			show()
		}
	}
	
	private fun secondaryModalStage(primaryStage:Stage) {
		Stage().apply {
			title = "Secondary Modal Stage"
			initOwner(primaryStage)
			initModality(Modality.WINDOW_MODAL)
			scene = Scene(VBox(
			    Label("I'm a modal secondary stage"),
		        Label("You cannot put me behind my owner")
		    ), 300.0, 250.0).customCSS()
			show()
		}
	}

	private fun secondaryTopStage() {
		Stage().apply {
			title = "Secondary Top-Level Stage"
			scene = Scene(VBox(
			    Label("I'm a top-level secondary stage"),
			    Label("I'll stay, even if you close the primary stage")
		    ), 350.0, 250.0).customCSS()
			show()
		}
	}
	
	private fun Scene.customCSS():Scene {
		stylesheets.add("css/styles.css")
		return this
	}
}
