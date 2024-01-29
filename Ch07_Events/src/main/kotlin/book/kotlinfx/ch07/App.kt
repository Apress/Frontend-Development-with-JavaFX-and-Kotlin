package book.kotlinfx.ch07

import javafx.application.Application
import javafx.stage.Stage

import book.kotlinfx.util.*
import javafx.scene.layout.*
import javafx.scene.*
import javafx.scene.shape.Rectangle
import javafx.scene.paint.Color
import javafx.event.EventType
import javafx.scene.input.MouseEvent
import javafx.event.EventHandler
import javafx.scene.text.Text
import javafx.scene.control.TextArea
import javafx.event.Event
import javafx.scene.control.TextField
import javafx.scene.input.Dragboard
import javafx.scene.input.TransferMode
import javafx.scene.image.Image
import javafx.scene.input.ClipboardContent

fun main(args:Array<String>) {
	Application.launch(App::class.java, *args)
}
 
class App : Application() {
	override
	fun start(primaryStage:Stage) {
		primaryStage.title = "Events"
		
		val root = StackPane().apply {
		    children.add(
				TabPane("Simple Events" to events(),
					"Drag'n'Drop" to dragAndDrop(),
				    "Drag'n'Drop Full Gesture" to dragAndDropFullGesture())
			)
		}
		
		with(primaryStage){
		    scene = Scene(root, 600.0, 400.0)
		    show()
		}
	}
	
	fun events():Node {		
		val status1 = TextArea().apply {
			prefRowCount = 7
			prefColumnCount = 60
			isWrapText = true
			isEditable = false
		}
		val status2 = TextArea().apply {
			prefRowCount = 2
			prefColumnCount = 60
			isWrapText = true
			isEditable = false
		}
		
		val rect1 = Rectangle(100.0,100.0,Color.BLUE).apply{
			addEventHandler(MouseEvent.MOUSE_CLICKED, { mouseEvent ->
				status1.text = "Rect: " + mouseEvent.toString()
			})
		}
		val rect2 = Rectangle(100.0,100.0,Color.PINK).apply{
			onMouseEntered = EH{ mouseEvent:MouseEvent ->
				status1.text = "Rect: " + mouseEvent.toString()
			}
		}
		val rect3x = VBox(
			Rectangle(100.0,100.0,Color.AQUA).apply{
			    onMouseClicked = EH{ mouseEvent:MouseEvent ->
				    status2.text = "Rect3x: " + mouseEvent.toString()
				    // never runs!
			    }
		}).apply{
			addEventFilter(MouseEvent.MOUSE_CLICKED, { mouseEvent ->
				status1.text = "Rect3x-parent: " + mouseEvent.toString()
				mouseEvent.consume()
			})
		}
		
		val someParent = HBox(5.0, rect1, rect2, rect3x).apply{
			onMouseClicked = EH{ mouseEvent:MouseEvent ->
				status2.text = "HBox: " + mouseEvent.toString()
			}
		}
		
		return VBox(
			someParent,
			status1,
			status2
	    )
	}
	
	fun dragAndDrop():Node {
		// simple dragging --------------------------------------------------------
		val p1 = Pane()
		val dragDelta = object{ var x=0.0; var y=0.0 }
		val tf1 = TextField("Source Node").apply{
              onMousePressed = EH{
			    e -> println("Source: pressed")
                dragDelta.x = layoutX - e.sceneX
                dragDelta.y = layoutY - e.sceneY
                cursor = Cursor.MOVE
			  }
              onMouseDragged = EH{
			      e -> println("Source: dragged")
                  layoutX = e.sceneX + dragDelta.x
                  layoutY = e.sceneY + dragDelta.y
			  }
              onDragDetected = EH{
			      e -> println("Source: dragged detected")
			  }
              onMouseReleased = EH{
			    e -> println("Source: released")
                cursor = Cursor.HAND
			  }				
			}
		val tf2 = TextField("Target node").apply{
              onMouseDragEntered = EH{ e -> println("Target: drag entered") }
              onMouseDragOver = EH{ e -> println("Target: drag over") }
              onMouseDragReleased = EH{ e -> println("Target: drag released") }
              onMouseDragExited = EH{ e -> println("Target: drag exited") }			
			}
		tf1.layoutX = 0.0
		tf1.layoutY = 0.0
		tf2.layoutX = 200.0
		tf2.layoutY = 0.0
		p1.children.addAll(tf2, tf1)
		// ----------------------------------------------------------------------
		
		// full dragging --------------------------------------------------------
		val p2 = Pane()
		//val dragDelta = object{ var x=0.0; var y=0.0 }
		val tf3 = TextField("Source Node").apply{
              onMousePressed = EH{
			    e -> println("Source: pressed")
                dragDelta.x = layoutX - e.sceneX
                dragDelta.y = layoutY - e.sceneY
				// Make sure the node is not picked
                setMouseTransparent(true)
                cursor = Cursor.MOVE
			  }
              onMouseDragged = EH{
			      e -> println("Source: dragged")
                  layoutX = e.sceneX + dragDelta.x
                  layoutY = e.sceneY + dragDelta.y
			  }
              onDragDetected = EH{
			      e -> println("Source: dragged detected")
				  startFullDrag()
			  }
              onMouseReleased = EH{
			    e -> println("Source: released")
				// Make sure the node is picked
                setMouseTransparent(false)
                cursor = Cursor.HAND
			  }				
			}
		val a4 = VBox( Text("Target node"),
			Rectangle(200.0,50.0,Color.LIGHTSALMON).apply{
              onMouseDragEntered = EH{ e -> println("Target: drag entered") }
              onMouseDragOver = EH{ e ->
				  println("Target: drag over")
				  fill = Color.LIGHTSALMON.darker()
			  }
              onMouseDragReleased = EH{ e ->
				  println("Target: drag released")
				  fill = Color.LIGHTGREEN
			  }
              onMouseDragExited = EH{ e -> println("Target: drag exited") }			
			} )
		tf3.layoutX = 0.0
		tf3.layoutY = 0.0
		a4.layoutX = 200.0
		a4.layoutY = 0.0
		p2.children.addAll(a4, tf3)
		// ----------------------------------------------------------------------

		return VBox(10.0, p1, p2)
	}		

	fun dragAndDropFullGesture():Node {
		val tf1 = TextField().apply{
			promptText = "Enter something, then drag"
            onDragDetected = EH{
			      e -> println("Source: dragged detected")				
				  if(text.isNotBlank()) {
					  startDragAndDrop(*TransferMode.COPY_OR_MOVE).run {
						  // from now on, no more mouse events, instead
						  // drag events. "this" = Dragboard
						  setDragView(Image("/dnd.png",false),0.0,0.0)
                          setContent(ClipboardContent().apply{ putString(text.trim()) })						  
					  }
                      cursor = Cursor.MOVE
				  }
                  e.consume()
			  }
            onDragDone = EH{
			      e -> println("Source: drag done")
				  when(e.transferMode) {
                    TransferMode.MOVE -> {
                      text = "" }
					else -> {}
                  }				
                  cursor = Cursor.TEXT
			  }
		}
		    
		val tf2 = TextField().apply{
			promptText = "Drop here"
			onDragOver = EH{ e -> 
				// If drag board has a string, let the event know that the target accepts
				// copy and move transfer modes
				e.dragboard.run {
				  if(hasString()) {
				    e.acceptTransferModes(*TransferMode.COPY_OR_MOVE)
				  }
				}
				e.consume()
            }
			onDragDropped = EH{ e -> 
                // Transfer the data to the target
				val txt = e.dragboard.let{ it.getString() }
				if(txt != null) text = txt
				e.setDropCompleted(txt != null) // successful?
				e.consume()
			}
		}
		val textFieldExample = HBox(10.0, tf1, tf2)
		
		return VBox(10.0, textFieldExample)
	}
	
	 
}
