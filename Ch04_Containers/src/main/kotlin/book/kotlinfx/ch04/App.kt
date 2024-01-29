package book.kotlinfx.ch04

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.*
import javafx.stage.Stage
import javafx.scene.control.*
import javafx.stage.Screen
import javafx.application.Platform
import javafx.geometry.Rectangle2D
import javafx.scene.text.Text
import javafx.scene.layout.VBox
import javafx.geometry.Insets
import javafx.stage.Modality
import javafx.scene.layout.Priority
import javafx.scene.shape.Circle
import javafx.scene.paint.Color

import book.kotlinfx.util.*
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.FlowPane
import javafx.scene.layout.HBox
import javafx.geometry.NodeOrientation
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Node

fun main(args: Array<String>) {
	Application.launch(App::class.java, *args)
}

class App : Application() {
	var contents = StackPane().apply{id="contents"}
	lateinit var scene1:Scene
 
	override
	fun start(primaryStage: Stage) {
		primaryStage.title = "Containers"

		val root = VBox(
		      MenuBar(
				  Menu("Menu").apply {
				    with(items){
						add(MenuItem("StackPane"){ stackPane() })
						add(MenuItem("VBox / HBox"){ vboxHbox() })
						add(MenuItem("FlowPane"){ flowPane() })
						add(MenuItem("GridPane"){ gridPane() })
						add(MenuItem("TilePane"){ tilePane() })
						add(MenuItem("BorderPane"){ borderPane() })
						add(MenuItem("AnchorPane"){ anchorPane() })
						add(MenuItem("Styling"){ styling() })
					}
				  }
			  ),
			  contents
		    ).apply{ 
				 // make contents height-growable
				 VBox.setVgrow(contents, Priority.ALWAYS)
		    }

		scene1 = Scene(root, 500.0, 350.0).customCSS()
		with(primaryStage) {
			scene = scene1
			show()
		}	
	}
		
	private fun stackPane() {
		with(contents.children){
		    clear()
			add( StackPane(
			  Circle(60.0, Color.LIGHTGREEN),	
			  Text("Some Text"),
			  Text("Bottom Left").apply{ StackPane.setAlignment(this, Pos.BOTTOM_LEFT) },
			  Text("Bottom Right+").apply{
				  StackPane.setAlignment(this, Pos.BOTTOM_RIGHT)
				  StackPane.setMargin(this, Insets(0.0, 10.0, 10.0, 0.0))
			  }
			  //,TextArea().apply { prefRowCount = 1000 }
			).apply{ background = Background(BackgroundFill(Color.BLANCHEDALMOND,null,null)) }  )			
		}
	}
		
	private fun vboxHbox() {
		with(contents.children){
		    clear()
			add( VBox(
			  Text("Some Text"),
			  Circle(60.0, Color.LIGHTGREEN),
			  HBox(
			    Text("Some Text"),
			    VBox(
				  Button("Some Button"),
				  Text("Some Text")
		        ).apply{ background = Background(BackgroundFill(Color.ORANGE,null,null)) }
			  )
			).apply{ background = Background(BackgroundFill(Color.BLANCHEDALMOND,null,null)) } )	
		}
	}

	private fun flowPane() {
		val fp = FlowPane()
		(1..20).forEach { i ->
			fp.children.add(
				Button("${i}").apply{ prefWidth = 40.0 + 50.0 * Math.random()  }
			)
		}
		
		with(contents.children){
		    clear()
			add( VBox(5.0,
				HBox(5.0,
					CheckBox("Right-to-Left").apply{
						setOnAction { 
							if(isSelected) {
								fp.nodeOrientation = NodeOrientation.RIGHT_TO_LEFT
							}else{
								fp.nodeOrientation = NodeOrientation.LEFT_TO_RIGHT
							}
						}
					},
					CheckBox("Vertical").apply{
						setOnAction {
							if(isSelected) {
								fp.orientation = Orientation.VERTICAL
							}else{
								fp.orientation = Orientation.HORIZONTAL
							}
						}
					}
				),
			    fp
			) )	
		}
	}
		
	private fun gridPane() {
		val gp = GridPane().apply{ style="""
          -fx-border-width: 1em;
          -fx-border-color: #0000;
          -fx-hgap: 0.5em;
          -fx-vgap: 0.5em;
        """ }
		
		with(gp) {
			add( Text("Name (last, first):"),
		      0,0)
		    add( HBox(
				   TextField().apply{id="lastName"},
				   TextField().apply{id="firstName"}
			     ),
		      1,0, 2,1)
		    add( Text("Street:"),
		      0,1)
		    add( HBox(
				   TextField().apply{id="bldg";prefColumnCount=6},
				   TextField().apply{id="street"}
			     ),
		      1,1, 2,1)
		    add( Text("City:"),
		      0,2)
		    add( HBox(
				   TextField().apply{id="city";prefColumnCount=15},
				   TextField().apply{id="state";prefColumnCount=2}
			     ),
		      1,2, 2,1)
		    add( Text("Zip:"),
		      0,3)
		    add( TextField().apply{id="zip";prefColumnCount=8},
		      1,3, 2,1)
			add( Button("SUBMIT").apply{
		        setOnAction{
				   println(listOf("lastName","firstName","bldg","street","city","state","zip")
					   .map{ k -> (scene1.lookup("#${k}") as TextField).text })
				}
			  },
			  0,4)
		}
		
		with(contents.children){
		    clear()
			add(gp)
		}
	}
		
	private fun tilePane() {
		val tp = TilePane().apply{ style="""
          -fx-border-width: 1em;
          -fx-border-color: #0000;
          -fx-hgap: 0.5em;
          -fx-vgap: 0.5em;
        """ }
		
		(1..15).forEach { 
			tp.children.add(
				Circle(20.0 + 20.0 * Math.random())
			)
		}
		
		with(contents.children){
		    clear()
			add(tp)
		}
	}
		
	private fun borderPane() {
		val top = HBox(
			Text("TOP"),
			Button("H-Center"){ BorderPane.setAlignment(this.parent, Pos.TOP_CENTER) }
		).apply { maxWidth = HBox.USE_PREF_SIZE }
		val bottom = Text("BOTTOM")
		val left = Text("LEFT")
		val right = Text("RIGHT")
		val center = Text("CENTER")
		
		val bp = BorderPane(center, top, right, bottom, left)
		
		with(contents.children){
		    clear()
			add(bp)
		}
	}
		
	var leftAnchor:Double? = 40.0
	var topAnchor:Double? = 40.0
	var rightAnchor:Double? = null
	var bottomAnchor:Double? = null
	private fun anchorPane() {
		val ap = AnchorPane()		
		val vb = VBox()
		
		fun doAnchor() {
			AnchorPane.clearConstraints(vb)
			leftAnchor?.run{ AnchorPane.setLeftAnchor(vb, this) }
			rightAnchor?.run{ AnchorPane.setRightAnchor(vb, this) }
			topAnchor?.run{ AnchorPane.setTopAnchor(vb, this) }
			bottomAnchor?.run{ AnchorPane.setBottomAnchor(vb, this) }
		}

		vb.children.addAll(
			ComboBox<String>().apply {
				prefWidth = 100.0
				items.addAll("Left","Right")
				value = "Left"
				setOnAction {
					if(value == "Left") {
						leftAnchor = 40.0
						rightAnchor = null
					}else{
						leftAnchor = null
						rightAnchor = 40.0						
					}
					doAnchor()
				}
			},
			ComboBox<String>().apply {
				prefWidth = 100.0
				items.addAll("Top","Bottom")
				value = "Top"
				setOnAction {
					if(value == "Top") {
						topAnchor = 40.0
						bottomAnchor = null
					}else{
						topAnchor = null
						bottomAnchor = 40.0						
					}
					doAnchor()
				}
			}
		)
		
		doAnchor()
		ap.children.add(vb)
				
		with(contents.children){
		    clear()
			add(ap)
		}
	}

	private fun styling() {
		val top = Text("TOP").apply { BorderPane.setAlignment(this, Pos.TOP_CENTER) }
		val bottom = Text("BOTTOM").apply { BorderPane.setAlignment(this, Pos.TOP_CENTER) }
		val left = Text("LEFT")
		val right = Text("RIGHT")
		val center = VBox(
			HBox( Text("CENTER Row 1") ).hgrow(),
			HBox( Text("CENTER Row 2") ).hgrow(),
			HBox( Text("CENTER Row With Margin") ).hgrow().apply{styleClass.add("pseudo-margin")},
			Region().vgrow().apply{styleClass.add("filler")}
		  ).apply {
			styleClass.add("center")
		  }
		
		val bp = BorderPane(center, top, right, bottom, left).apply{
			stylesheets.add("css/panes1.css")
		}
		
		with(contents.children){
		    clear()
			add(bp)
		}
	}
	
	private fun Node.id(id0:String):Node { id=id0; return this }
	private fun Node.hgrow():Node { HBox.setHgrow(this,Priority.ALWAYS); return this }
	private fun Node.vgrow():Node { VBox.setVgrow(this,Priority.ALWAYS); return this }

	private fun Scene.customCSS():Scene {
		stylesheets.add("css/styles.css")
		return this
	}
}
