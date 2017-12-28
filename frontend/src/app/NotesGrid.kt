package app

import model.Note
import react.RBuilder
import react.RClass
import react.RProps
import react.dom.div
import react.key

@JsModule("react-grid-layout")
external val reactGridLayout : RClass<ReactGridLayoutProps>

external interface LayoutInterface {
    // Key
    var i: String

    // Position
    var x: Int
    var y: Int

    // Dimensions
    var w: Int
    var h: Int
    var minW: Int?
    var minH: Int?
}

class Layout(
    override var i: String,
    override var x: Int,
    override var y: Int,
    override var w: Int,
    override var h: Int,
    override var minW: Int?,
    override var minH: Int?
) : LayoutInterface

external interface ReactGridLayoutProps : RProps {
    var cols: Int
    var rowHeight: Int
    var width: Int
    var draggableHandle: String?
    var layout: Array<LayoutInterface>
    var onLayoutChange: (layouts: Array<LayoutInterface>) -> Unit
}

fun createNoteLayout(note: Note): Layout = Layout(
    i = "${note.id}",
    x = note.x,
    y = note.y,
    w = note.width,
    h = note.height,
    minW = 1,
    minH = 1
)

fun RBuilder.notesGrid(notes: Array<Note>) {
    reactGridLayout {
        attrs {
            cols = 12
            rowHeight = 30
            width = 1600
            layout = notes.map { createNoteLayout(it) }.toTypedArray()
            draggableHandle = ".NoteDragHandle"
            onLayoutChange = { layouts ->
                console.log("*** layouts", layouts)

                layouts.forEach { layout ->
                    notes.find { "${it.id}" == layout.i }
                        ?.let {
                            if (layout.x != it.x ||
                                layout.y != it.y ||
                                layout.w != it.width ||
                                layout.h != it.height) {
                                console.log("**** moving note", it)
                                api.moveNote(it.id!!, layout)
                            }
                        }
                }
            }
        }

        notes.map {
            div("Note") {
                attrs {
                    key = "${it.id}"
                }
                note(it)
            }
        }
    }
}
