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
import javafx.embed.swing.SwingFXUtils
import java.io.File
import javafx.scene.input.KeyEvent
import javafx.event.Event
import javafx.event.EventHandler

class SceneProperties {
	var contents = StackPane()
	lateinit var scene1:Scene
	
	fun start() {
		scene1 = Scene(
			VBox(
		      MenuBar(
				  Menu("_Menu").apply {
				    with(items){
						add(MenuItem("Position & Size"){ positionAndSize() })
				        add(MenuItem("Camera"){ camera() })
				        add(MenuItem("Mnemonic & Accelerators"){ mnemonic() })
				        add(MenuItem("Auto M_nemonic"){ println("Auto Mnemonic") })
				        add(MenuItem("Focus"){ focus() })
				        add(MenuItem("Node Lookup"){ nodeLookup() })
				        add(MenuItem("Scene Snapshot"){ snapshot() })
				        add(MenuItem("Key & Mouse"){ keyAndMouse() })
					}
				  }
			  ),
			  contents
		    ).apply{ styleClass.add("mainStage") },
		400.0, 400.0).customCSS().apply {
			cursor = Cursor.HAND
		}
		
		Stage().apply {
			title = "Scene Properties"
			scene = scene1
			show()
		}
	}
	
	private fun positionAndSize() {
		with(contents.children){
		    clear()
			add( VBox(
			  Label("Circle radius = 0.25 * Scene Width"),
			  Circle().apply{ radiusProperty().bind(scene1.widthProperty().divide(4.0))  }	
			))
		}
	}

	val cam = ParallelCamera()
	private fun camera() {
		scene1.camera = cam
		with(contents.children){
		    clear()
			add( VBox(
			    Label(scene1.camera?.toString()?:"Camera is null"),
				Button("Shift Camera"){
					cam.translateX = 20.0
				},
				Button("Wobble Camera"){
					cam.translateX = 0.0
					Thread {
						(0..100).forEach{ i ->
							val ang = 5*2*Math.PI * i/100
    						cam.scaleX = 1.0 + 0.2 - 0.2 * Math.sin(ang)							
							Thread.sleep(50)
						}
						cam.scaleX = 1.0
					}.start()
				}
			))
		}
	}

	private fun mnemonic() {
		val node = TextField("Press Q+Alt")
		val mnemonicKeyCombo:KeyCodeCombination =
			KeyCodeCombination(KeyCode.Q, KeyCombination.ALT_DOWN)
        val myMnemonic:Mnemonic = Mnemonic(node, mnemonicKeyCombo)
		scene1.addEventHandler(ActionEvent.ACTION) {
			actionEvent:ActionEvent ->
			println(actionEvent.toString())
		}
        scene1.addMnemonic(myMnemonic)
				
		with(contents.children){
		    clear()
			add( VBox(
				Button("Print Current Mnemonics"){
				  //val mn:ObservableMap<KeyCombination,​ObservableList<Mnemonic>> =
				  val mn = scene1.mnemonics
					mn.forEach {keyComb, list ->
						println("${keyComb} -> ${list}")
					}
				},
				node
			))
		}
		
		scene1.accelerators[KeyCodeCombination(KeyCode.R, KeyCombination.ALT_DOWN)] =
			Runnable{ println("Accelerator ALT-R pressed") }
	}

	private fun focus() {
		val info = Text()
		with(contents.children){
		    clear()
			add( VBox(
			  TextField("Text 1"),
			  TextField("Text 2"),
			  Button("Who has focus?"){
				  info.text = scene1.focusOwner.toString()  
			  },
			  Button("Request focus for text 2"){
				  parent.childrenUnmodifiable[1].requestFocus()
			  },
			  info,
			  Text("You can also press ALT+F too see who has focus")
			))
		}
		scene1.accelerators[KeyCodeCombination(KeyCode.F, KeyCombination.ALT_DOWN)] =
			Runnable { info.text = scene1.focusOwner.toString() }
	}

	private fun nodeLookup() {
		with(contents.children){
		    clear()
			add( VBox(
			  Text("Some Text").apply{ id = "text1" },
			  Button("Update text by node ID lookup"){
				  (scene1.lookup("#text1") as Text).text = "Changed!"
			  }
			))
		}
	}

	private fun snapshot() {
		scene1.setOnMouseClicked { me ->
			println(me.toString())
		}
		
		with(contents.children){
		    clear()
			add( VBox(
			  Text("Some Text").apply {
				  setOnMouseClicked { me ->
			          println(me.toString())
		          }
			  },
			  Button("Some Button"),
			  Circle(20.0, Paint.valueOf("#f80")),
			  Button("Take Snapshot"){
				  val wi = WritableImage(Math.ceil(scene1.width).toInt(),
					  Math.ceil(scene1.height).toInt())
				  scene1.snapshot({ ssr:SnapshotResult ->
					  val bufferedImage:BufferedImage =
						  SwingFXUtils.fromFXImage(ssr.image, null)
                      ImageIO.write(bufferedImage, "png", File("Snapshot." + System.currentTimeMillis() + ".png" ))
					  null
				  }, wi)
			  },
			  Button("Take Snapshot v2"){
				  val wi = WritableImage(Math.ceil(scene1.width).toInt(),
					  Math.ceil(scene1.height).toInt())
				  val tm0 = System.currentTimeMillis()
				  val wi2 = scene1.snapshot(wi)
				  println("Progress: "+wi2.progress+
						  " ms: "+(System.currentTimeMillis()-tm0))
				  val bufferedImage:BufferedImage =
						  SwingFXUtils.fromFXImage(wi2, null)
                  ImageIO.write(bufferedImage, "png", File("Snapshot." + System.currentTimeMillis() + ".png" ))
			  }
			))
		}
	}
	
	private fun keyAndMouse() {
		scene1.setOnMouseClicked { me ->
			println(me.toString())
		}
		scene1.setOnKeyTyped { ke ->
			println(ke.toString())					  
		}
		
		with(contents.children){
		    clear()
			add( VBox(
			  Text("Some Text").apply {
				  setOnMouseClicked { me ->
			          println(me.toString())
		          }
			  }
			))
		}
	}
		
	private fun Scene.customCSS():Scene {
		stylesheets.add("css/styles.css")
		return this
	}
}