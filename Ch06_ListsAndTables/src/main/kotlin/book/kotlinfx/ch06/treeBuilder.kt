package book.kotlinfx.ch06

import javafx.scene.control.TreeItem

interface Element {
	var label:String
    fun compose(builder: TreeItem<String>)
}

abstract class Tag() : Element {
    val children = mutableListOf<Element>()
	override var label:String = ""

    protected fun <T : Element> initTag(tag: T, init: T.() -> Unit): T {
		tag.init()
        children.add(tag)
        return tag
    }

    override fun compose(builder: TreeItem<String>) {
        for (c in children) {
			builder.children.add( TreeItem<String>(c.label).also{ c.compose(it) } )
        }
    }

	fun build():TreeItem<String> = TreeItem<String>(label).also{ compose(it)  }
}

abstract class TagBase() : Tag() {
    fun item(label: String, init: (Item.() -> Unit)={}) {
        val a = initTag(Item(), init)
        a.label = label
    }
	// more child types...
}

class Tree : TagBase()
class Item : TagBase()

fun tree(init: Tree.() -> Unit) = Tree().also{ it.init() }
