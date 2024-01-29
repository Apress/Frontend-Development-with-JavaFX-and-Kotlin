package book.kotlinfx.ch07

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.effect.*
import javafx.scene.layout.StackPane
import javafx.stage.Stage

import book.kotlinfx.util.*
import javafx.scene.Node
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.paint.Stop
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.CycleMethod
import javafx.scene.text.Text
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.Group
import javafx.scene.layout.VBox
import javafx.scene.layout.Pane
import javafx.scene.layout.GridPane
import javafx.scene.control.Slider
import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.shape.Circle
import javafx.animation.KeyValue
import javafx.animation.KeyFrame
import javafx.util.Duration
import javafx.animation.Timeline
import javafx.animation.Interpolator
import javafx.animation.FadeTransition
import javafx.animation.TranslateTransition

object Effects : VBox() {
	init {
		// -----------------------------------------------------------------------
		//     Blend
		// -----------------------------------------------------------------------
		val blend = HBox().apply{
			val bl = Blend().apply{ mode = BlendMode.COLOR_BURN }
            val colorInput = ColorInput().apply{
				 paint = Color.STEELBLUE
				 x = 10.0; y = 10.0; width = 100.0; height = 180.0				
			}
            bl.setTopInput(colorInput)

            val rect = Rectangle(220.0, 100.0).apply {
                val stops = arrayOf( Stop(0.0, Color.LIGHTSTEELBLUE), Stop(1.0, Color.PALEGREEN) )
                fill = LinearGradient(0.0, 0.0, 0.25, 0.25, true, CycleMethod.REFLECT, *stops)
			}

            val text = Text().apply{
				 x = 15.0; y = 65.0
				 fill = Color.PALEVIOLETRED
				 text = "COLOR_BURN"
				 font = Font.font(null, FontWeight.BOLD, 30.0)				
			}

            val g = Group().apply { effect = bl }.apply {
                children.addAll(rect, text)
			}
			
			children.addAll(g)
		}
		
		// -----------------------------------------------------------------------
		//     Bloom
		// -----------------------------------------------------------------------
        val bloom = Pane().apply{
			val blm = Bloom()
			
			val sl = Slider(0.0, 1.0, 0.1)
			blm.thresholdProperty().bind(sl.valueProperty())

	        val rect = Rectangle().apply{
				 layoutY = 10.0
				 x = 10.0; y = 10.0; width = 200.0; height = 80.0
                 val stops = arrayOf( Stop(0.0, Color.BLACK), Stop(1.0, Color.PALEGREEN) )
                 fill = LinearGradient(0.0, 0.0, 0.25, 0.25, true, CycleMethod.REFLECT, *stops)
				 effect = blm
			}
			val text = Text("Bloom!").apply{
				 layoutY = 10.0
				 x = 25.0; y = 65.0
				 font = Font.font(null, FontWeight.BOLD, 40.0)
				 fill = Color.WHITE
				 effect = blm
			}
			children.addAll(sl, rect, text)
		}

		// -----------------------------------------------------------------------
		//     Glow
		// -----------------------------------------------------------------------
        val glow = Pane().apply{
			val glw = Glow()
			
			val sl = Slider(0.0, 1.0, 0.1)
			glw.levelProperty().bind(sl.valueProperty())

	        val rect = Rectangle().apply{
				 layoutY = 10.0
				 x = 10.0; y = 10.0; width = 200.0; height = 80.0
                 val stops = arrayOf( Stop(0.0, Color.BLACK), Stop(1.0, Color.PALEGREEN) )
                 fill = LinearGradient(0.0, 0.0, 0.25, 0.25, true, CycleMethod.REFLECT, *stops)
				 effect = glw
			}
			val text = Text("Glow").apply{
				 layoutY = 10.0
				 x = 25.0; y = 65.0
				 font = Font.font(null, FontWeight.BOLD, 40.0)
				 fill = Color.WHITE
				 effect = glw
			}
			children.addAll(sl, rect, text)
		}

		// -----------------------------------------------------------------------
		//     BoxBlur
		// -----------------------------------------------------------------------
		val boxBlur = HBox().apply {
			val bb = BoxBlur(10.0,10.0,1)
			
			val sl = Slider(0.0, 10.0, 1.0)
			bb.iterationsProperty().bind(sl.valueProperty())
			
			val text = Text("Blurry Text!").apply{
				 fill = Color.web("0x3b596d")
				 font = Font.font(null, FontWeight.BOLD, 50.0)
				 x = 10.0; y = 50.0
				 effect = bb						
			}
			
			children.addAll(sl, text)
		}
		
		// -----------------------------------------------------------------------
		//     GaussianBlur
		// -----------------------------------------------------------------------
		val gaussBlur = HBox().apply {
			val gb = GaussianBlur()
			
			val sl = Slider(0.0, 10.0, 1.0)
			gb.radiusProperty().bind(sl.valueProperty())
			
			val text = Text("Blurry Text!").apply{
				 fill = Color.web("0x3b596d")
				 font = Font.font(null, FontWeight.BOLD, 50.0)
				 x = 10.0; y = 50.0
				 effect = gb						
			}
			
			children.addAll(sl, text)
		}
		
		// -----------------------------------------------------------------------
		//     MotionBlur
		// -----------------------------------------------------------------------
		val motionBlur = HBox().apply {
			val mb = MotionBlur(0.0, 15.0)
			
			val sl = Slider(0.0, 360.0, 180.0)
			mb.angleProperty().bind(sl.valueProperty())
			
			val text = Text("Motion").apply{
				 fill = Color.web("0x3b596d")
				 font = Font.font(null, FontWeight.BOLD, 50.0)
				 x = 10.0; y = 50.0
				 effect = mb						
			}
			
			children.addAll(sl, text)
		}
		
		// -----------------------------------------------------------------------
		//     ColorAdjust
		// -----------------------------------------------------------------------
		val colorAdjust = GridPane(5,5).apply {
			val contr = Slider(-1.0,1.0,0.0)
			val bright = Slider(-1.0,1.0,0.0)
			val satur = Slider(-1.0,1.0,0.0)
			val hue = Slider(-1.0, 1.0, 0.0)
		    val cadj = ColorAdjust().apply{
				 contrastProperty().bind(contr.valueProperty())
				 hueProperty().bind(hue.valueProperty())
				 brightnessProperty().bind(bright.valueProperty())
				 saturationProperty().bind(satur.valueProperty())				
			}
            val image = Image("frog3.jpg")
            val imageView = ImageView(image).apply{
              fitWidth = 200.0
              isPreserveRatio = true
              effect = cadj
			}
			add("Brightness".t,0,0)
			add(bright,1,0)
			add("Contrast".t,0,1)
			add(contr,1,1)
			add("Saturation".t,0,2)
			add(satur,1,2)
			add("Hue".t,0,3)
			add(hue,1,3)
			add(imageView,2,0,1,4)
			atTop(getAt(1,3))
			atTop(getAt(0,3))
		}

		// -----------------------------------------------------------------------
		//     DisplacementMap
		// -----------------------------------------------------------------------
		val displMap = HBox().apply{
			val width = 220
            val height = 100

            val floatMap = FloatMap(width, height).apply{
			  for (i in 0 until width) {
			     val v = (Math.sin(i / 20.0 * Math.PI) - 0.5) / 40.0
			     for (j in 0 until height) {
			         setSamples(i, j, 0.0f, v.toFloat())
			     }
			  }
			}
            val dm = DisplacementMap(floatMap)

            val text = Text("Wavy Text").apply{
			  x = 40.0; y = 80.0
			  fill = Color.web("0x3b596d")
			  font = Font.font(null, FontWeight.BOLD, 50.0)
			  effect = dm			
			}
			
			children.addAll(text)
		}

		// -----------------------------------------------------------------------
		//     SepiaTone
		// -----------------------------------------------------------------------
		val sepia = HBox(10.0).apply {
			val sl = Slider(0.0, 1.0, 0.0)

			val sep = SepiaTone().apply{
			    levelProperty().bind(sl.valueProperty())
			}

            val image = Image("frog3.jpg")
            val imageView = ImageView(image).apply{
				 fitWidth = 200.0
				 isPreserveRatio = true
				 effect = sep				
			}
			children.addAll(sl, imageView)
		}
		
		// -----------------------------------------------------------------------
		//     DropShadow
		// -----------------------------------------------------------------------
        val dropShadow = HBox(10.0).apply{
			val ds = DropShadow().apply{
				 radius = 5.0
				 offsetX = 3.0
				 offsetY = 3.0
				 color = Color.color(0.4, 0.5, 0.5)				
			}

            val text = Text("Drop shadow...").apply{
				 effect = ds
				 isCache = true
				 x = 10.0; y = 70.0
				 fill = Color.web("0x3b596d")
				 font = Font.font(null, FontWeight.BOLD, 40.0)				
			}

            val ds2 = DropShadow().apply{
				 offsetX = 6.0
				 offsetY = 4.0				
			}

            val circle = Circle().apply {
				 effect = ds2
				 centerX = 50.0
				 centerY = 125.0
				 radius = 30.0
				 fill = Color.STEELBLUE
				 isCache = true				
			}
 
			children.addAll(text, circle)
		}
		
		// -----------------------------------------------------------------------
		//     Shadow
		// -----------------------------------------------------------------------
        val shadow = HBox(10.0).apply{
			val sh = Shadow().apply{
				 radius = 15.0
				 //width = 3.0
				 //height = 3.0
				 color = Color.color(0.4, 0.5, 0.5)				
			}
            val bl = Blend().apply{
				bottomInput = sh
			}
			
            val text = Text("Shadow...").apply{
				 effect = bl // sh
				 isCache = true
				 x = 10.0; y = 70.0
				 fill = Color.web("0x3b596d")
				 font = Font.font(null, FontWeight.BOLD, 40.0)				
			}
 
			children.addAll(text)
		}
				
		// -----------------------------------------------------------------------
		//     InnerShadow
		// -----------------------------------------------------------------------
        val innerShadow = HBox(10.0).apply{
			val sl = Slider(0.0, 10.0, 0.0)
			
			val innerShadow = InnerShadow().apply{
				 offsetX = 4.0
				 offsetY = 4.0
				 color = Color.web("0x3b596d")
				 radiusProperty().bind(sl.valueProperty())	
			}

			val text = Text("InnerShadow").apply{
			  effect = innerShadow
			  x = 20.0; y = 100.0
			  fill = Color.ALICEBLUE
			  font = Font.font(null, FontWeight.BOLD, 50.0)				
			}			
 
			children.addAll(sl, text)
		}

		// -----------------------------------------------------------------------
		//     Lighting
		// -----------------------------------------------------------------------
        val lighting = HBox(10.0).apply{
			val sl = Slider(-180.0, 180.0, 0.0)
			val lgt = Light.Distant().apply  {
				azimuthProperty().bind(sl.valueProperty())
			}

            val lighting = Lighting().apply{
				light = lgt
				surfaceScale = 5.0 
			}
            val text = Text("JavaFX!").apply{
                fill = Color.STEELBLUE
                font = Font.font(null, FontWeight.BOLD, 60.0)
                x = 10.0; y = 10.0
                textOrigin = VPos.TOP
                effect = lighting				
			}
            children.addAll(sl, text)
        }		

		// -----------------------------------------------------------------------
		//     Reflection
		// -----------------------------------------------------------------------
        val reflection = HBox(10.0).apply{
			prefHeight = 120.0; minHeight = 120.0
			val sl = Slider(0.0, 1.0, 0.7)
			val refl = Reflection().apply{
				topOffset = -5.0
				fractionProperty().bind(sl.valueProperty())
			}

            val text = Text("Reflections").apply{
				x = 10.0; y = 50.0
				isCache = true
				fill = Color.web("0x3b596d")
				font = Font.font(null, FontWeight.BOLD, 40.0)
				effect = refl
			}
			children.addAll(sl, text)
		}
								
		// -----------------------------------------------------------------------
		// -----------------------------------------------------------------------
		children.add(ScrollPane( VBox(10.0,
			"Blending".t,
			blend, VSTRUT(80),
			"Bloom".t,
			bloom,
			"Glow".t,
			glow,
			"BoxBlur".t,
			boxBlur,
			"GaussBlur".t,
			gaussBlur,
			"MotionBlur".t,
			motionBlur,
			"ColorAdjust".t,
			colorAdjust,
			"SepiaTone".t,
			sepia,
			"DisplacementMap".t,
			displMap,
			"DropShadow".t,
			dropShadow,			
			"Shadow".t,
			shadow,			
			"InnerShadow".t,
			innerShadow,
			"Lighting".t,
			lighting,
			"Reflection".t,
			reflection		
		).apply{ style="-fx-padding:10;" } ) )
	}

	private val String.t get() = Text(this)
	private fun GridPane.getAt(column:Int, row:Int):Node? =
		children.find{GridPane.getColumnIndex(it) == column && GridPane.getRowIndex(it) == row }
	private fun GridPane.atTop(n:Node?){ GridPane.setFillHeight(n,false) 
			                            GridPane.setValignment(n,VPos.TOP) }
	private fun Interpolator(curvex:(d:Double)->Double) =
		object : Interpolator(){ override fun curve(v:Double) = curvex(v) }
	
}