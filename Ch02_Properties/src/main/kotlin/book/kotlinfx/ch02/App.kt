package book.kotlinfx.ch02

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage

import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.scene.layout.HBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.beans.property.SimpleStringProperty

import book.kotlinfx.util.* // from Ch00_Util
import javafx.scene.layout.GridPane
import javafx.geometry.Insets
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleIntegerProperty
import javafx.util.StringConverter
import java.text.DecimalFormat
import javafx.scene.control.Slider
import javafx.scene.shape.Circle
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.beans.value.ObservableValue
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.control.TabPane
import javafx.scene.control.Tab
import javafx.scene.Node
import javafx.collections.FXCollections
import javafx.scene.control.ListView
import java.util.HashSet
import javafx.collections.ListChangeListener
import javafx.collections.SetChangeListener
import javafx.collections.MapChangeListener
import javafx.beans.InvalidationListener
import javafx.beans.Observable

fun main(args:Array<String>) {
	Application.launch(App::class.java, *args)
}
 
class App : Application() {
	override
	fun start(primaryStage:Stage) {
		primaryStage.title = "Properties"
		
	    val tabPane = TabPane(
            Tab("Properties", properties()),
            Tab("Collections", collections())
		)

		with(primaryStage){
		    scene = Scene(tabPane, 300.0, 400.0)
		    show()
		}
	}
		
	private fun properties():Node {
		// Old-fashioned data binding
		val t1 = TextField("").apply{
		    textProperty().addListener { _, oldValue, newValue -> 
                println("textfield changed from " + oldValue + " to " + newValue)
				// inform model...
		} }
		val b1 = Button("Set 42"){
			t1.textProperty().value = "42"
		}

		// Property based data binding
		val p2 = SimpleStringProperty("")
		val t2 = TextField("").apply{
		    textProperty().bindBidirectional(p2) }
		val b2 = Button("Set 42"){
			p2.value = "42"
		}

		// One TextField inside binding expression
		val t3 = TextField()
		val se = SimpleStringProperty("The text is: ").concat( t3.textProperty() )
				
		// Two TextFields. First binding to integer properties, then
		// summing up, then binding with result TextField
		val t4 = TextField("0")
		val t5 = TextField("0")
		val t4i = SimpleIntegerProperty()
		val t5i = SimpleIntegerProperty()
		Bindings.bindBidirectional(t4.textProperty(), t4i, DecimalFormat())
		Bindings.bindBidirectional(t5.textProperty(), t5i, DecimalFormat())
		val ie = t4i.add(t5i).asString()
				
		// Cross-Connecting Controls
		val sl = Slider(0.0,30.0,20.0)
		val circ = Circle().apply {
			radiusProperty().bindBidirectional(sl.valueProperty())
		}
				
		// Custom Binding
		val prop = SimpleDoubleProperty() // <- could go to a ViewModel
		val sl2 = Slider(0.0,30.0,20.0).apply{valueProperty().bindBidirectional(prop)}
		val colorBinding:ObservableValue<Color> = Bindings.createObjectBinding( { -> 
              Color.color(0.0,0.0,0.0+prop.getValue()/30.0) 
            }, prop)
		val circ2 = Circle().apply {
			radiusProperty().set(20.0)
			fillProperty().bind(colorBinding)
		}
				
		// No bidrectional binding
		val sl3 = Slider(0.0,30.0,20.0)
		val circ3 = Circle().apply {
			// radiusProperty().bindBidirectional(sl2.valueProperty().divide(2.0))
			// <- error
			radiusProperty().bind(sl3.valueProperty().divide(2.0))
		}
				
		val gp = GridPane().apply {
			padding = Insets(5.0)
			hgap = 5.0
			add( t1,                  0,0)
			add( b1,                  1,0)
			// ---------------------------------
			add( VSTRUT(10),          0,1)
			add( t2,                  0,2)
			add( b2,                  1,2)
			// ---------------------------------
			add( VSTRUT(10),          0,3)
			add( Label("Some text:"), 0,4)
			add( t3,                  1,4)
		    // add( Label().apply { textProperty().bind(se) }, 0,1,2,1)   // without util.kt
		    add( Label(se),           0,5,2,1)
			// ---------------------------------
			add( VSTRUT(10),          0,6)
			add( Label("Some int:"),  0,7)
			add( t4,                  1,7)
			add( Label("Other int:"), 0,8)
			add( t5,                  1,8)
			add( Label("Sum:"),       0,9)
		    add( Label(ie),           1,9)
			// ---------------------------------
			add( VSTRUT(10),          0,10)
			add( sl,                  0,11)
			add( circ,                1,11)
			// ---------------------------------
			add( VSTRUT(10),          0,12)
			add( sl2,                 0,13)
			add( circ2,               1,13)
			// ---------------------------------
			add( VSTRUT(10),          0,14)
			add( sl3,                 0,15)
			add( circ3,               1,15)
		}
		
		return gp
	}
	
	private fun collections():Node {
		
		// Creating observable collections
		val s1 = FXCollections.emptyObservableSet<String>()
		val l1 = FXCollections.emptyObservableList<String>()
		val m1 = FXCollections.emptyObservableMap<Int,String>()
		
		val s2 = FXCollections.observableSet(1,2,3)
		val l2 = FXCollections.observableArrayList(1,2,3)
		
		val s3 = FXCollections.observableSet(mutableSetOf(1,2,3))
		val l3 = FXCollections.observableList(mutableListOf(1,2,3))
		val m3 = FXCollections.observableMap(
			mapOf(1 to "a", 2 to "b", 3 to "c"))
		
		// Change listeners
		l3.addListener( { chg:ListChangeListener.Change<out Int> ->
		   while (chg.next()) {
             if (chg.wasPermutated()) {
				 println("Permutated: " + (chg.from..chg.to-1))
                 (chg.from..chg.to-1).forEach{ i ->
				     val newIndex = chg.getPermutation(i)
					 println("index[${i}] moved to index[${newIndex}]") 
				 }
             } else if (chg.wasUpdated()) {
                 println("Updated: " + (chg.from..chg.to-1))
				 println("Updated elements: " + chg.list.subList(chg.from, chg.to))
             } else if (chg.wasReplaced()) {
                 println("Replaced: " + (chg.from..chg.to-1))
                 println("Removed Size: " + chg.removedSize)
                 println("Removed List: " + chg.removed)
                 println("Added Size: " + chg.addedSize)
                 println("Added List: " + chg.addedSubList)
             } else if (chg.wasRemoved()) {
                 println("Removed: " + (chg.from..chg.to-1))
                 println("Removed Size: " + chg.removedSize)
                 println("Removed List: " + chg.removed)
             } else if (chg.wasAdded()) {
                 println("Added: " + (chg.from..chg.to-1))
                 println("Added Size: " + chg.addedSize)
                 println("Added List: " + chg.addedSubList)
             }
           }
		})
		l3.addAll(4,5,6)
		l3.removeAt(2)

		s3.addListener { chg:SetChangeListener.Change<out Int> ->
          if (chg.wasAdded()) {
            println("Added to set: " + chg.elementAdded)
          } else if (chg.wasRemoved()) {
            println("Removed from set: " + chg.elementRemoved)
          }
          println("Set after the change: " + chg.set)	
		}
		s3.add(4)
		s3.remove(2)

		m3.addListener { chg:
		          MapChangeListener.Change<out Int,out String> ->
		    if (chg.wasRemoved() && chg.wasAdded()) {
		        println("Replaced in map: (${chg.key}, "+
		               "${chg.valueRemoved} -> ${chg.valueAdded})")
		    } else {
		        if (chg.wasRemoved()) {
		          println("Removed from map: (${chg.key}, "+
		                  "${chg.valueRemoved})")
		        } else if (chg.wasAdded()) {
		          println("Added to map: (${chg.key}, "+
		                  "${chg.valueAdded})")
		        }
		    }
		}
		m3.put(4,"d")
		m3.remove(2)
		m3.put(1,"x")

		// Invalidation listeners
		l3.addListener { observable: Observable ->
			println(observable::class.toString() + " has changed")
		}
		l3.add(99)
		
		// A ListView
		val l = FXCollections.observableArrayList("Apple","Peach","Banana")
		val listView = ListView(l)
		val btn = Button("Change it"){
			l.set(1, System.currentTimeMillis().toString())
		}
		
		val gp = GridPane().apply {
			padding = Insets(5.0)
			hgap = 5.0
			add( listView,             0,0)
			add( btn,                  0,1)
		}
		return gp
	}
}
