package book.kotlinfx.util

import javafx.scene.control.*
import javafx.beans.property.StringProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.binding.StringExpression
import javafx.beans.property.ReadOnlyStringProperty
import javafx.scene.layout.Region
import javafx.scene.layout.GridPane
import javafx.scene.layout.TilePane
import javafx.geometry.Orientation
import javafx.beans.Observable
import javafx.beans.value.ObservableValue
import javafx.scene.text.Text
import javafx.beans.binding.Bindings
import javafx.beans.property.DoubleProperty
import javafx.beans.property.Property
import javafx.beans.property.BooleanProperty
import javafx.scene.Node
import javafx.event.Event
import javafx.event.EventHandler

fun HSTRUT(s:Int) = Region().apply{prefWidth=s.toDouble(); minWidth=s.toDouble()}
fun VSTRUT(s:Int) = Region().apply{prefHeight=s.toDouble(); minHeight=s.toDouble()}

fun Button(label: String, action: Button.() -> Unit): Button =
	Button(label).apply {
		setOnAction { _ -> action() }
	}

fun Label(p:StringExpression):Label = Label().apply{
	textProperty().bind(p)
}

fun MenuItem(label: String, action: MenuItem.() -> Unit): MenuItem =
	MenuItem(label).apply {
		setOnAction { _ -> action() }
	}

fun GridPane(hgap:Double, vgap:Double): GridPane =
	GridPane().apply {
		this.hgap = hgap
		this.vgap = vgap
	}

fun GridPane(hgap:Int, vgap:Int): GridPane =
	GridPane(hgap.toDouble(), vgap.toDouble())

fun TilePane(hgap:Double, vgap:Double): TilePane =
	TilePane().apply {
		this.hgap = hgap
		this.vgap = vgap
	}

fun TilePane(hgap:Int, vgap:Int): TilePane =
	TilePane(hgap.toDouble(), vgap.toDouble() )

fun TilePane(orientation:Orientation, hgap:Double, vgap:Double): TilePane =
	TilePane(orientation).apply {
		this.hgap = hgap
		this.vgap = vgap
	}

fun TilePane(orientation:Orientation, hgap:Int, vgap:Int): TilePane =
	TilePane(orientation, hgap.toDouble(), vgap.toDouble())

fun Text(observable:ObservableValue<String>) = Text().apply{ textProperty().bind(observable) }

// Allows for binding Double, ... properties as String observables
fun bindingNumberToString(nmbr:Property<Number>) : ObservableValue<String> =
	Bindings.createObjectBinding( { -> nmbr.value.toString() }, nmbr )

fun TextField(sp:StringProperty) = TextField().apply { textProperty().bindBidirectional(sp) }
fun TextArea(sp:StringProperty) = TextArea().apply { textProperty().bindBidirectional(sp) }
fun CheckBox(bp:BooleanProperty) = CheckBox().apply { selectedProperty().bindBidirectional(bp) }

fun ToggleGroup.addMyListener(listener: (value:String) -> Unit) {
	selectedToggleProperty().addListener{_,_,newVal ->
			listener(newVal.userData as String)
	} 
}

fun ToggleGroup(listener: (value:String) -> Unit) = ToggleGroup().apply{ addMyListener(listener) }

fun ToggleGroup.selectToggle(id:String) {
	selectToggle( toggles.find { it -> it.userData == id } )
}

fun RadioButton(id:String, label:String, toggleGroup:ToggleGroup) = RadioButton(label).apply{
	toggleGroup.toggles.add(this)
	this.userData = id
}

fun Accordion(pane1:Pair<String,Node>, vararg panes:Pair<String,Node>) = Accordion().apply{
	listOf(pane1,*panes).forEach{ pp ->
		this.panes.add(TitledPane(pp.first,pp.second))
	}
}

fun TabPane(pane1:Pair<String,Node>, vararg panes:Pair<String,Node>) = TabPane().apply{
	listOf(pane1,*panes).forEach{ pp ->
		this.tabs.add(Tab(pp.first,pp.second))
	}
}

fun <T : Event> EH(clos:(event:T) -> Unit): EventHandler<T> = object : EventHandler<T>{ override fun handle(event:T){
	clos(event)
}}
