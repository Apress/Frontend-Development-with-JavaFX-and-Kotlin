package book.kotlinfx.ch05

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import javafx.scene.control.*
import javafx.scene.text.*
import javafx.stage.Screen
import javafx.application.Platform
import javafx.geometry.Rectangle2D

import book.kotlinfx.util.*
import javafx.scene.layout.VBox
import javafx.geometry.Insets
import javafx.stage.Modality
import javafx.scene.layout.Priority
import javafx.scene.shape.Circle
import javafx.scene.paint.Color
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import javafx.scene.effect.DropShadow
import javafx.scene.Group
import javafx.collections.FXCollections
import javafx.scene.canvas.Canvas
import javafx.scene.shape.Path
import javafx.scene.shape.MoveTo
import javafx.scene.shape.LineTo
import javafx.scene.layout.Region
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.ImageView
import javafx.scene.image.Image
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Orientation
import javafx.beans.property.SimpleDoubleProperty

fun main(args: Array<String>) {
	Application.launch(App::class.java, *args)
}

class App : Application() {
	var contents = StackPane().apply{id="contents"}
	lateinit var scene1:Scene

	override
	fun start(primaryStage: Stage) {
		primaryStage.title = "Visual Nodes"

		val root = VBox(
		      MenuBar(
				  Menu("Menu").apply {
				    with(items){
						add(MenuItem("Coordinate Systems"){ coordinateSystems() })
						add(MenuItem("Texts"){ texts() })
						add(MenuItem("Canvas"){ canvas() })
						add(MenuItem("Images"){ images() })
						add(MenuItem("Julia Canvas"){ JuliaCanvas().start() })
						add(MenuItem("Random Shapes"){ randomShapes() })
						add(MenuItem("Text Input"){ inputText() })
						add(MenuItem("Text Input and Binding"){ inputTextBinding() })
						add(MenuItem("Buttons"){ buttons() })
						add(MenuItem("Button Bars"){ DialogButtons().start() })
						add(MenuItem("Tool Bars"){ toolBars() })
						add(MenuItem("Menus"){ Menus().start() })
						add(MenuItem("Checkboxes"){ checkBoxes() })
						add(MenuItem("Radio Buttons"){ radioButtons() })
						add(MenuItem("Combo Boxes"){ comboBoxes() })
						add(MenuItem("Sliders"){ sliders() })
					}
				  },
				  Menu("Control Panes").apply {
				    with(items){
						add(MenuItem("Controll Panes"){ ControlPanes().start() })
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

	private fun Scene.customCSS():Scene {
		stylesheets.add("css/styles.css")
		return this
	}
	
	private fun buttons() {
        val btn1 = Button("Klick me").apply {
			setOnAction{ event ->
				println("Button pressed")
			}
		}
		val btn2 = Button("Klick me (pseudo constructor)") {
          println("Button pressed")
          println("I am " + this.toString())
        }

		with(contents.children){
		    clear()
			add(
			  VBox(
			    VBox(
			      btn1, VSTRUT(5),
			      btn2, VSTRUT(5)
			    ).apply { id = "btnBox" }
			  )
			)
		}
	}
	
	private fun sliders() {
		val prop = SimpleDoubleProperty(0.33)
		
        val sl1 = Slider(0.0,100.0,33.0).apply{
			valueProperty().bindBidirectional(prop)
			orientation = Orientation.HORIZONTAL
		}
        val sl2 = Slider(0.0,100.0,33.0).apply{
			orientation = Orientation.VERTICAL
			setShowTickMarks(true)
            setShowTickLabels(true)
            setMajorTickUnit(25.0)
            setBlockIncrement(10.0)
		}
		
		with(contents.children){
		    clear()
			add(
			  VBox(
			    VBox(
			      sl1, VSTRUT(5),
			      sl2, VSTRUT(5)
			    )
			  )
			)
		}
	}
	
	private fun checkBoxes(){
		class PersonModel {
			var firstName = SimpleStringProperty()
			var lastName = SimpleStringProperty()
			var employed = SimpleBooleanProperty()
		}
		val p1 = PersonModel()
		
		with(contents.children){
		    clear()
			add(
			  GridPane(5.0,5.0).apply {
				add(Text("First name:"), 0,0)  
				add(TextField(p1.firstName), 1,0)  
				add(Text("Last name:"), 0,1)  
				add(TextField(p1.lastName), 1,1)  
				add(Text("Employed:"), 0,2)  
				add(CheckBox(p1.employed), 1,2)  
			  }
			)
		}		
	}

	private fun radioButtons(){
		class FruitModel {
			var selectedId = SimpleStringProperty("Bananas")
		}
		val f1 = FruitModel()

		// Just an example: observe model changes
		f1.selectedId.addListener{_,_,v ->
			println("Selected: ${v}")
		}
		
		// A ToggleGroup with a custom listener
		val tg = ToggleGroup{ value:String ->
			f1.selectedId.value = value
		}
		
		with(contents.children){
		    clear()
			add(
			  VBox(
				  RadioButton(id="Bananas","Bananas",tg),
				  RadioButton(id="Apples","Apples",tg),
				  RadioButton(id="Lemons","Lemons",tg),
				  RadioButton(id="Other","Other",tg)
		      )
			)
		}
		
		// Select toggle by model
		tg.selectToggle( f1.selectedId.value )
	}
	
	private fun toolBars(){
        val tb = ToolBar()
		(1..20).forEach{ i ->
			tb.items.add( Button("Button #${i}") )
		}
		
		with(contents.children){
		    clear()
			add(
			  VBox(
		        tb
			  )
			)
		}		
	}

	private fun images() {
        val img = ImageView(Image("images/frog.jpg"))
		
		with(contents.children){
		    clear()
			add(
			  VBox(
				  img
			  )
			)
		}
	}

	private fun inputText() {
		val tf1 = TextField()
		val ta1 = TextArea()

		val tf2 = TextField("Hello World")
		val ta2 = TextArea("Hello World\nLine 2")

		val tf3 = TextField("Size via CSS").apply{ id="tf3"; maxWidth = Region.USE_PREF_SIZE  }
		val ta3 = TextArea("Size via CSS").apply{ id="ta3"
			maxWidth = Region.USE_PREF_SIZE ; maxHeight = Region.USE_PREF_SIZE  }

		val tf4 = TextField("Size via Code").apply{
			prefColumnCount = 24
			maxWidth = Region.USE_PREF_SIZE  }
		val ta4 = TextArea("Size via Code").apply{
			prefColumnCount = 24
			prefRowCount = 3
			maxWidth = Region.USE_PREF_SIZE
			maxHeight = Region.USE_PREF_SIZE  }
				
		val tf5 = TextField().apply{
			promptText = "Enter something"
			prefColumnCount = 24
			maxWidth = Region.USE_PREF_SIZE  }

		with(contents.children){
		    clear()
			add(
			  ScrollPane( VBox(
			    tf1, VSTRUT(5),
			    ta1, VSTRUT(5),
			    tf2, VSTRUT(5),
			    ta2, VSTRUT(5),
			    tf3, VSTRUT(5),
			    ta3, VSTRUT(5),
			    tf4, VSTRUT(5),
			    ta4, VSTRUT(5),
			    tf5, VSTRUT(5)
			  ) ) )
		}
	}

	private fun inputTextBinding() {
        class PersonModel {
			val firstName = SimpleStringProperty()
			val lastName = SimpleStringProperty()
		}
		
		val pm = PersonModel()
		val pm2 = PersonModel()
		
		with(contents.children){
		    clear()
			add( VBox(
				GridPane(5.0,5.0).apply{
					add(Text("Frst Name:"),
					    0,0)
					add(TextField().apply{ textProperty().bindBidirectional(pm.firstName) },
					    1,0)
					add(Text("Last Name:"),
					    0,1)
					add(TextField().apply{ textProperty().bindBidirectional(pm.lastName) },
					    1,1)
				},
				VSTRUT(10),
				GridPane(5.0,5.0).apply{
					add(Text("Frst Name:"),
					    0,0)
					add(TextField(pm2.firstName),
					    1,0)
					add(Text("Last Name:"),
					    0,1)
					add(TextField(pm2.lastName),
					    1,1)
				}
			) )
		}
	}

	private fun randomShapes() {
		val path = Path().apply {
			strokeWidth = 3.0
			with(elements){
			  add(MoveTo(0.0,0.0))
			  add(LineTo(50.0,20.0))
			  add(LineTo(20.0,50.0))				
		} }
		
		val circle = Circle(40.0)
		
		val text1 = Text("""Some text
            |Second line""".trimMargin())

		val text2 = Text("Some long text ".repeat(10))
			.apply{ wrappingWidth = 300.0 }

		val text3 = Text("Some long text ".repeat(10))
			.apply{ wrappingWidthProperty().bind(scene1.widthProperty()) }
		
		with(contents.children){
		    clear()
			add(
			  VBox(
			    path, VSTRUT(5),
				circle, VSTRUT(5),
				text1, VSTRUT(5),
				text2, VSTRUT(5),
				text3
			  ) )
		}
	}

	private fun canvas() {
		val canvas = Canvas(450.0, 300.0)
		
		with(contents.children){
		    clear()
			add(
			  VBox(
			    Button("Paint Rectangle"){
					val w = 20.0 + 400.0 * Math.random()
					val h = 20.0 + 180.0 * Math.random()
					val x = (450.0 - w) * Math.random()
					val y = (300.0 - h) * Math.random()
					val red = Math.random()
					val green = Math.random()
					val blue = Math.random()
					val opacity = 0.2 + 0.8 * Math.random()
					val g = canvas.graphicsContext2D
					g.fill = Color(red,green,blue,opacity)
					g.fillRect(x,y,w,h)
				},
			    canvas
			  ) )
		}
	}

	private fun texts() {
		val fontFamilies = FXCollections.observableList(Font.getFamilies())
		val fontFamiliesCombo = ComboBox(fontFamilies)
		val fontCombo = ComboBox<String>()
		val textWithFont = Text("This is some Text, not 42 at all, dude")
		fontFamiliesCombo.valueProperty().addListener{_,_,v ->
		    with(fontCombo.itemsProperty().value) {
			    clear(); addAll( Font.getFontNames(v) )
			}
			textWithFont.fontProperty().value = Font(v, 25.0)
		}
		fontCombo.valueProperty().addListener{_,_,v ->
			if( v != "" ) textWithFont.fontProperty().value = Font(v, 25.0)
		}
		
		with(contents.children){
		    clear()
			add( VBox(
			  	Text("Long text Long text Long text Long text "+
						"Long text Long text Long text Long text "+
						"Long text Long text Long text Long text "+
						"Long text Long text Long text Long text "+
						"Long text").apply{ wrappingWidth = 200.0 },
			    VSTRUT(10),
			    Text("First Line\nSecond Line"),
				Button("Print Font Families"){
					println("=== Font Families:")
					Font.getFamilies().forEach(::println)
				},
			    VSTRUT(10),
			    HBox(
					fontFamiliesCombo,
					fontCombo
				),
				textWithFont
			) )
		}
	}

	private fun comboBoxes() {
		val FRUITS = FXCollections.observableList(listOf("Bananas", "Apples", "Lemons", "OTHER"))
		
		class Model {
			var fruit = SimpleStringProperty()
		}
		val fm = Model()
		
		val combo = ComboBox(FRUITS).apply{
			valueProperty().bindBidirectional(fm.fruit)
		}
		
		with(contents.children){
		    clear()
			add( VBox(
				combo
			) )
		}
	}

	private fun coordinateSystems() {
		with(contents.children){
		    clear()
			add( HBox(
			  VBox(
			    Text("Plain VBox"),
				VSTRUT(20),
			    Button("Button"),
			    Button("Button with Effect").apply{
					effect = DropShadow()
				},
			    Button("Button with Rotation").apply{
					effect = DropShadow()
					rotate = 30.0
				}
			  ),
			  VBox(
			    Text("VBox, children in Groups"),
				VSTRUT(20),
			    Button("Button"),
				Group(
			        Button("Button with Effect").apply{
					    effect = DropShadow()
				} ),
				Group(
			        Button("Button with Rotation").apply{
					    effect = DropShadow()
					    rotate = 30.0
				} )
			  )
			) )			
		}
	}

}
