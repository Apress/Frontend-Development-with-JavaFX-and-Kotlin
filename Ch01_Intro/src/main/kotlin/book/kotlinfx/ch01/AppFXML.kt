package book.kotlinfx.ch01

import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.text.Text
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import javafx.fxml.FXMLLoader
import javafx.scene.layout.Pane
import javafx.fxml.FXML
import java.lang.SuppressWarnings

fun main(args:Array<String>) {
	Application.launch(HelloWorldFXML::class.java, *args)
}
 
class MyController {
	@FXML var actiontarget:Text? = null
	  
	@Suppress("UNUSED_PARAMETER")
    @FXML fun handleButtonAction( event:ActionEvent) {
        actiontarget?.setText("Sign in button pressed")
    }
}

/**
 * If you want to test this, you must set
 *    application {
 *         mainClass = 'book.kotlinfx.ch01.AppFXMLKt'
 *    }
 * in build.gradle
 */
class HelloWorldFXML : Application() {
	override
	fun start(primaryStage:Stage) {
        primaryStage.title = "Hello World!"

		val location = this::class.java.classLoader.getResource("helloworld.fxml")
		println(location)
        //val resources = ResourceBundle.getBundle("com.foo.example")
        val fxmlLoader = FXMLLoader(location) //, resources);

        val root = fxmlLoader.load<Pane>()
        //val controller = fxmlLoader.getController<MyController>();
		
        with(primaryStage){
            scene = Scene(root, 300.0, 250.0)
            show()
        }
	}
}
