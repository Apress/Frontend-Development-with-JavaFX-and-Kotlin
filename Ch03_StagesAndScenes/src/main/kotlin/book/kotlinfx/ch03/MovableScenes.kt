package book.kotlinfx.ch03

import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.scene.control.Label
import javafx.scene.control.*
import book.kotlinfx.util.*
import javafx.stage.Stage

class MovableScenes(parentx:Double, parenty:Double) {
	lateinit var stage1:Stage
	lateinit var stage2:Stage
	var scene1:Scene
	var scene2:Scene
	
	init {
		scene1 = Scene(VBox(
			Label("I'm a scene"),
		    Button("Move Scene"){
				val tmp = stage2.scene
				stage2.scene = stage1.scene
				stage1.scene = tmp
			}
		), 200.0, 150.0).customCSS()

		scene2 = Scene(VBox(), 200.0, 150.0)
		
		stage1 = Stage().apply {
			title = "Stage 1"
			scene = scene1
			x = parentx-100
			y = parenty+100
			show()
		}

		stage2 = Stage().apply {
			title = "Stage 2"
			scene = scene2
			x = parentx+100
			y = parenty+100
			show()
		}
	}
	
	private fun Scene.customCSS():Scene {
		stylesheets.add("css/styles.css")
		return this
	}
}