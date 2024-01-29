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

fun main(args:Array<String>) {
	Application.launch(AppWithViewModel::class.java, *args)
}

class MyViewModel {
	val text1Property = SimpleStringProperty("Some text")
      .apply{ addListener { _, oldValue, newValue -> 
            println("text1Property changed from " + 
                oldValue + " to " + newValue)
            // inform model (backend) ...
      } }
}
 
/**
 * If you want to test this, you must set
 *    application {
 *         mainClass = 'book.kotlinfx.ch02.AppWithViewModelKt'
 *    }
 * in build.gradle
 */
class AppWithViewModel : Application() {
	override
	fun start(primaryStage:Stage) {
		primaryStage.title = "Properties"

		val vm = MyViewModel()
				
        val text1 = TextField().apply{
            textProperty().bindBidirectional(vm.text1Property) }
	
        val root = GridPane().apply {
            padding = Insets(5.0)
            hgap = 5.0
            add( text1,              0,0)
        }
	
        with(primaryStage){
            scene = Scene(root, 300.0, 250.0)
            show()
        }
	}
}
