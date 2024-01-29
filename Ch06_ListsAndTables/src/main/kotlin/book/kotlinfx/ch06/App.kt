package book.kotlinfx.ch06

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage

import book.kotlinfx.util.*
import javafx.scene.control.Tab
import javafx.scene.Node
import javafx.scene.text.Text
import javafx.scene.layout.GridPane
import javafx.collections.FXCollections
import javafx.scene.control.ListView
import javafx.geometry.Insets
import javafx.scene.control.SelectionMode
import javafx.beans.Observable
import javafx.scene.layout.Region
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.layout.HBox
import javafx.util.Callback
import javafx.scene.control.ListCell
import javafx.util.StringConverter
import javafx.scene.control.cell.TextFieldListCell
import java.time.LocalDate
import javafx.collections.ObservableList
import java.time.Month
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.TableView
import javafx.scene.control.TableColumn.CellDataFeatures
import javafx.beans.value.ObservableValue
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.beans.property.Property
import javafx.scene.control.TableCell
import javafx.scene.control.cell.TextFieldTableCell
import kotlin.text.Regex
import java.time.format.DateTimeFormatter
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.control.cell.TextFieldTreeCell

fun main(args:Array<String>) {
	Application.launch(App::class.java, *args)
}
 
class App : Application() {
	override
	fun start(primaryStage:Stage) {
		primaryStage.title = "Lists And Tables"
		
	    val tabPane = TabPane(
            "Lists" to lists(),
            "Lists 2" to lists2(),
            "Lists 3" to lists3(),
            "Tables" to  tables(),
            "Trees" to trees()
		)

		with(primaryStage){
		    scene = Scene(tabPane, 800.0, 600.0)
		    show()
		}
	}
			
	private fun lists():Node {
		val l = FXCollections.observableArrayList("Apple","Peach","Banana")
		val listView = ListView(l).apply {
			placeholder = Text("The List is empty")
			selectionModel.selectionMode = SelectionMode.MULTIPLE
		}
		val btn1 = Button("Change it"){
			l.set(1, "Pineapple")
		}
		val btn2 = Button("Add an item"){
			listView.items.add(System.currentTimeMillis().toString())
		}
		val info = Text("")
		listView.selectionModel.getSelectedItems().addListener{list:Observable -> info.text = "Selected: ${list}" }
		
		val vb = VBox(
			Text("A basic ListView"),
			listView,
			btn1,
			btn2,
			info
		).apply{
			btn1.minWidthProperty().bind(this.widthProperty().divide(2.0))
			btn2.minWidthProperty().bind(this.widthProperty().divide(2.0))
		}
		return vb
	}

	private fun lists2():Node {
		data class Person(val firstName:String, val lastName:String)
		val l = FXCollections.observableArrayList(
			Person("John","Smith"), Person("Linda","Gray"), Person("Kate","Winslow") )
		val listView = ListView<Person>(l).apply{	
			cellFactory =
	            object : Callback<ListView<Person>,ListCell<Person>> {
	                override
	                fun call(listView:ListView<Person>):ListCell<Person> {
	                  return object : ListCell<Person>() {
	                    override
	                    fun updateItem(item:Person?, empty:Boolean) {
	                      // Must call super
	                      super.updateItem(item, empty)
	                      val index = this.index
	                      var name:String? = null
	                      // Format name
                          if (item != null && !empty) {
	                        name = "" + (index + 1) + ". " +
	                          item.lastName + ", " +
	                          item.firstName
	                      }
	                      setText(name)
	                      setGraphic(null)
	                    }
	                  }
	                }
				}
        }
				
		val vb = VBox(
			Text("List with custom renderer"),
			listView
		)
		return vb
	}

	private fun lists3():Node {
		data class Person(val firstName:String, val lastName:String)
		val l = FXCollections.observableArrayList(
			Person("John","Smith"), Person("Linda","Gray"), Person("Kate","Winslow") )
		
		val converter:StringConverter<Person> = object : StringConverter<Person>(){
			override fun toString(obj:Person):String =
				obj.lastName + ", " + obj.firstName
			override fun fromString(s:String):Person {
				val spl = s.split(',', limit=2)
				return if(spl.size < 2) Person("",spl[0].trim())
				  else Person(spl[1].trim(), spl[0].trim()) 
			}
		}
		// There are many built-in converters in package javafx.util.converter
        val cellFactory1:Callback<ListView<Person>, ListCell<Person>> = 
            TextFieldListCell.forListView(converter)
		// There are more list cell factories in package javafx.scene.control.cell
		val listView = ListView<Person>(l).apply{
			isEditable = true
			cellFactory = cellFactory1 
        }
		val vb = VBox(
			Text("Double-Click or press SPACE to start editing"),
			listView
		)
		return vb
	}
	
	class Person {
		enum class Gender { F, M, D }
		val firstNameProperty: Property<String> = SimpleStringProperty()
		fun firstNameProperty() = firstNameProperty
		var firstName: String
			get() = firstNameProperty.getValue()
		    set(value) { firstNameProperty.setValue(value)  } 
		val lastNameProperty: Property<String> = SimpleStringProperty()
		fun lastNameProperty() = lastNameProperty
		var lastName: String
			get() = lastNameProperty.getValue()
		    set(value) { lastNameProperty.setValue(value)  } 
		val birthdateProperty: Property<LocalDate> = SimpleObjectProperty()
		fun birthdateProperty() = birthdateProperty
		var birthdate: LocalDate
			get() = birthdateProperty.getValue()
		    set(value) { birthdateProperty.setValue(value)  } 
		val genderProperty: Property<Gender> = SimpleObjectProperty()
		fun genderProperty() = genderProperty
		var gender: Gender
			get() = genderProperty.getValue()
		    set(value) { genderProperty.setValue(value)  } 
		
		constructor(firstName:String, lastName:String, birthdate:LocalDate, gender:Gender){
			this.firstName = firstName
			this.lastName = lastName
			this.birthdate = birthdate
			this.gender = gender
		}
	}
	private fun tables():Node {
		val l = FXCollections.observableArrayList<Person>(
			Person("Peter", "Smith", LocalDate.of(1988, Month.APRIL, 24), Person.Gender.M),
			Person("Linda", "Gray", LocalDate.of(1970, Month.DECEMBER, 10), Person.Gender.F),
			Person("Arny", "Bellevue", LocalDate.of(2001, Month.JANUARY, 4), Person.Gender.D)
		)
				
		val personTextFieldRenderer = TextFieldTableCell.forTableColumn<Person>()
		
		val genderTextFieldRenderer = TextFieldTableCell.forTableColumn<Person,Person.Gender>(
			object : StringConverter<Person.Gender>() {
			    override fun toString(p:Person.Gender) = "[${p}]"
				override fun fromString(s:String) = s.let {
					val v = it.replace(Regex("\\W+"),"").uppercase()
					when(v) {
						"M", "F", "D" -> Person.Gender.valueOf(v)
						else -> Person.Gender.D
					}
				}
		})
		
		val dateFieldRenderer = DatePickerTableCell.forTableColumn<Person>(datePickerEditable=true)
		//val dateFieldRenderer = TextFieldTableCell.forTableColumn<Person,LocalDate>(
		//	object : StringConverter<LocalDate>() {
		//	    override fun toString(d:LocalDate) = DateTimeFormatter.ISO_LOCAL_DATE.format(d)
		//		override fun fromString(s:String) = LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE)					
		//})

					
        val firstNameColumn =
          TableColumn<Person, String>("First Name").apply{
			  isEditable = true
			  cellValueFactory = PropertyValueFactory("firstName")
			  cellFactory = personTextFieldRenderer
		  }
        val lastNameColumn =
          TableColumn<Person, String>("Last Name").apply{
			  isEditable = true
			  cellValueFactory = PropertyValueFactory("lastName")
			  cellFactory = personTextFieldRenderer
		  }
        val birthDateColumn =
          TableColumn<Person, LocalDate>("Birthdate").apply{
			  isEditable = true
			  cellValueFactory = PropertyValueFactory("birthdate")
			  cellFactory = dateFieldRenderer
		  }
        val genderColumn =
          TableColumn<Person, Person.Gender>("Gender").apply{
			  isEditable = true
			  cellValueFactory = PropertyValueFactory("gender") // how to get value
			  cellFactory = genderTextFieldRenderer             // how to show
		  }
		
		val tv = TableView(l).apply{
			placeholder = Text("No visible columns and/or data exist.") // optional
			isEditable = true
			columns.addAll(
                firstNameColumn,
                lastNameColumn,
                birthDateColumn,
			    genderColumn)
		}
		
		return tv
	}	
	
	private fun trees():Node {
		val tree = tree {
	      item("Departments") {
		    item("Sales") {
			  item("Smith, John")
			  item("Evans, Linda")
			}
			item("IT") {
			  item("offshore") {
				item("Kalolu, Uru")
			  }
			  item("onshore"){
				item("Arab, Aurel")
				item("Gatter, Alina")
			  }			  
			}
			// We can perform logic inside the builder:
			(1..3).forEach{ i ->
				item("Extra "+i)
			}
		  }
		}.build()
					
		val tv = TreeView(tree).apply{
			setShowRoot(false)
			// Making it editable:
			isEditable = true
			cellFactory = TextFieldTreeCell.forTreeView()
		}
		
		return tv
	}

}
