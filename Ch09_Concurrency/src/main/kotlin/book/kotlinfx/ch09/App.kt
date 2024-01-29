package book.kotlinfx.ch09

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage

import book.kotlinfx.util.*
import javafx.scene.Node
import javafx.scene.layout.VBox
import javafx.scene.control.TextField
import java.time.LocalTime
import javafx.application.Platform
import javafx.concurrent.Worker
import java.util.concurrent.Executors
import javafx.scene.control.Slider
import javafx.concurrent.Task
import javafx.scene.layout.HBox
import javafx.scene.text.Text
import javafx.beans.property.ObjectProperty
import javafx.event.EventHandler
import javafx.stage.Window
import javafx.stage.WindowEvent
import javafx.concurrent.Service

import kotlinx.coroutines.javafx.JavaFx as Main
import javafx.scene.control.ListView
import javafx.collections.FXCollections
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.NonCancellable.isCancelled

fun main(args:Array<String>) {
	Application.launch(App::class.java, *args)
}
 
class App : Application() {
	val closeListeners = mutableListOf<()->Unit>()
	
	override
	fun start(primaryStage:Stage) {
		primaryStage.title = "Concurrency"
		primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, { closeListeners.forEach{ it() } })
		
		val root = StackPane().apply {
		    children.add(contents())
		}
		
		with(primaryStage){
		    scene = Scene(root, 500.0, 250.0)
		    show()
		}
	}
	
	@DelicateCoroutinesApi
	@ExperimentalCoroutinesApi
	private fun contents():Node {
		////////////////////////////////////////////////////////////////////
		// Thread Example
		////////////////////////////////////////////////////////////////////
		val tf = TextField()
		val btn = Button("Click me"){
			Thread{
				Thread.sleep(5000)
                // Platform.runLater{ tf.text = "Setting at " + LocalTime.now() }
				tf.text = "Setting at " + LocalTime.now()				
			}.start()
			// Thread.sleep(5000)
			// tf.text = "Setting at " + LocalTime.now()
		}
		
		////////////////////////////////////////////////////////////////////
		// Task Example
		////////////////////////////////////////////////////////////////////
		val pool = Executors.newFixedThreadPool(10).also{ pool ->
			// shutdown pool on window close request, otherwise the app
			// won't exit.
			closeListeners.add( pool::shutdownNow )
		}
		val sl2 = Slider(0.0,1.0,0.0)
		val txt2 = Text()
		val btn2 = Button("Click me"){
			val tsk = object : Task<Unit>() {
				override fun call() {
					updateMessage("Started")
					for(i in 0 until 1000) {
						if(isCancelled) break
						try{ Thread.sleep(10) }catch(e:InterruptedException){break}
						updateProgress(i.toDouble(),1000.0)
					}
					updateMessage("Done")
				}
			}
			userData = tsk // just make the task accessible
			sl2.valueProperty().bind(tsk.progressProperty())
			txt2.textProperty().bind(tsk.messageProperty())
			pool.submit(tsk)
		}
		val btnCancel2 = Button("Cancel"){
			(btn2.userData as Task<*>).cancel(true)
		}

		////////////////////////////////////////////////////////////////////
		// Service Example
		////////////////////////////////////////////////////////////////////
		val sl3 = Slider(0.0,1.0,0.0)
		val txt3 = Text()
		val srvc3 = object : Service<Unit>() {
			override fun createTask() =
				object : Task<Unit>() {
					override fun call() {
						updateMessage("Started")
						for(i in 0 until 1000) {
							if(isCancelled) break
							try{ Thread.sleep(10) }catch(e:InterruptedException){break}
							updateProgress(i.toDouble(),1000.0)
						}
						updateMessage("Done")
					}
				}.also{
					sl3.valueProperty().bind(it.progressProperty())
					txt3.textProperty().bind(it.messageProperty())
				}
			override fun cancelled() { unbindAll() }
			override fun failed() { unbindAll() }
			override fun succeeded() { unbindAll() }
			private fun unbindAll() { sl3.valueProperty().unbind(); txt3.textProperty().unbind() }
		}
		val btn3 = Button("Start Service"){
			if(srvc3.state == Worker.State.READY) srvc3.start()
		}
		val btnCancel3 = Button("Reset"){
			srvc3.cancel()
			srvc3.reset()
			sl3.value = 0.0
			txt3.text = "" 
		}
					
		////////////////////////////////////////////////////////////////////
		// Coroutines Example
		////////////////////////////////////////////////////////////////////
		val listData4 = FXCollections.observableArrayList("","","","")
		suspend fun pushMsg(s:String){ with(listData4){add(0,s); if(size>4) removeAt(4)} }
		val lv4 = ListView<String>(listData4)
		// ----- run everything in JavaFX main UI thread --------------------------
        val btn4a = Button("Countdown"){
			 val job = GlobalScope.launch(Dispatchers.Main) { // launch coroutine in the main UI thread
			    for (i in 10 downTo 1) {        // countdown 
			        pushMsg("Countdown $i ...") // show text
		            delay(500L)                 // wait half a second
		        }
			    pushMsg("Done!")
			 }
			 userData = job // make accessible from outside
		}
		val btn4b = Button("←Stop"){ (btn4a.userData as Job).cancel() }
		// ----- real background threads ------------------------------------------- 
        val btn4c = Button("Calculate PI"){
			// run using background threads
			suspend fun CoroutineScope.stupidPi() = produce<Double>(Dispatchers.Default) {
				var i = 1L
				var sgn = 1
				var v = 0.0
				while(true){
					v += 4.0 * sgn/( 2.0*i.toDouble() - 1.0) 
					sgn *= -1; i++
					if(i%100_000_000 == 0L) send(v)
				}
			}
			val job = GlobalScope.launch(Dispatchers.Main) { // launch coroutine in the main UI thread
			    val producer = stupidPi()
				while(true) {
				    pushMsg(producer.receive().toString())	
				}
			}
			userData = job
		}		
		val btn4d = Button("←Stop"){ (btn4c.userData as Job).cancel() }
					
		return VBox(5.0,
			btn, tf,
			HBox(4.0, btn2, btnCancel2, sl2, txt2),
			HBox(4.0, btn3, btnCancel3, sl3, txt3),
			HBox(4.0, VBox(  HBox(btn4a, btn4b), HBox(btn4c, btn4d) ), lv4)
		).apply{ style="-fx-padding:10;" }
	}
	
}
