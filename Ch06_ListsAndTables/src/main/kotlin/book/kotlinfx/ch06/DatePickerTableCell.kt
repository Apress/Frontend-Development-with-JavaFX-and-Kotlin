package book.kotlinfx.ch06

import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.util.StringConverter;
import java.time.LocalDate
import javafx.util.converter.LocalDateStringConverter
import java.util.Locale

@SuppressWarnings("unchecked")
public class DatePickerTableCell<S, T>() : TableCell<S, LocalDate>() {
  private var datePicker:DatePicker? = null
  private var converter:StringConverter<LocalDate>
  private var datePickerEditable = true
	
  init {
    this.converter = LocalDateStringConverter()
  }
	
  constructor(datePickerEditable:Boolean):this() {
    this.datePickerEditable = datePickerEditable
  }

  constructor(converter:StringConverter<LocalDate>):this() {
    this.converter = converter
  }
	
  constructor(converter:StringConverter<LocalDate>, datePickerEditable:Boolean):this() {
    this.converter = converter
    this.datePickerEditable = datePickerEditable
  }
	
  override fun startEdit() {
    // Make sure the cell is editable
    if (!isEditable() || !tableView.isEditable() || !tableColumn.isEditable()) return

    // Let the ancestor do the plumbing job
    super.startEdit()
	
    // Create a DatePicker, if needed, and set it as the graphic for the cell
    datePicker ?: createDatePicker()
    graphic = datePicker
	text = null
  }
	
  override fun cancelEdit() {
    super.cancelEdit()
    text = converter.toString(item)
    graphic = null
  }
	
  override fun updateItem(item:LocalDate?, empty:Boolean) {
    super.updateItem(item, empty)
    // Take actions based on whether the cell is being edited or not
    if (empty) {
      text = null
      graphic = null
    } else {
      if (isEditing) {
        datePicker?.setValue(item as LocalDate)
        text = null
        graphic = datePicker
      } else {
        text = converter.toString(item)
        graphic = null
      }
    }
  }
	
  private fun createDatePicker() {
    datePicker = DatePicker().apply{
	    setConverter(converter)
        // Set the current value in the cell to the DatePicker
	    setValue(this@DatePickerTableCell.item as LocalDate)
	    // Configure the DatePicker properties
	    setPrefWidth(this@DatePickerTableCell.width - this@DatePickerTableCell.graphicTextGap * 2)
	    setEditable(this@DatePickerTableCell.datePickerEditable)
		// Commit the new value when the user selects or enters a date
	    valueProperty().addListener(object : ChangeListener<LocalDate> {
	      override fun changed(prop:ObservableValue<out LocalDate>, oldValue:LocalDate, newValue:LocalDate) {
	        if (this@DatePickerTableCell.isEditing) {
				text = converter.toString(newValue) 
	            this@DatePickerTableCell.commitEdit(newValue)
	        }
	      }
	    })
	}	  
  }
	
  companion object {
	fun <S> forTableColumn():Callback<TableColumn<S, LocalDate>, TableCell<S, LocalDate>> =
        forTableColumn(true)
    fun <S> forTableColumn(datePickerEditable:Boolean):Callback<TableColumn<S, LocalDate>, TableCell<S, LocalDate>> =
		object : Callback<TableColumn<S, LocalDate>, TableCell<S, LocalDate>> {
			override fun call(p:TableColumn<S, LocalDate>):TableCell<S, LocalDate> = DatePickerTableCell<S, LocalDate>(datePickerEditable)
		}
        //return { col:TableColumn<S, LocalDate> -> DatePickerTableCell<S>(datePickerEditable)) }
    fun <S> forTableColumn(converter:StringConverter<LocalDate>):Callback<TableColumn<S, LocalDate>, TableCell<S,LocalDate>> =
        forTableColumn(converter, true)
    fun <S> forTableColumn(converter:StringConverter<LocalDate>, datePickerEditable:Boolean)
			:Callback<TableColumn<S, LocalDate>, TableCell<S,LocalDate>> =
		object : Callback<TableColumn<S, LocalDate>, TableCell<S, LocalDate>> {
			override fun call(p:TableColumn<S, LocalDate>):TableCell<S, LocalDate> = DatePickerTableCell<S, LocalDate>(converter, datePickerEditable)
		}
        //return (col -> DatePickerTableCell<S>(converter, datePickerEditable));
  }
}
