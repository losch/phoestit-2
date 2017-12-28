package app

import kotlinx.html.js.onClickFunction
import model.Note
import react.RBuilder
import react.dom.button
import react.dom.div

fun RBuilder.note(note: Note) {
    div("Note") {
        attrs {
            key = "${note.id}"
        }

        div("NoteButtons") {
            button(classes = "EditNoteButton") {
                +"E"
            }

            button(classes = "DeleteNoteButton") {
                attrs {
                    onClickFunction = {
                        console.log("Deleting note ${note.id}")
                        api.deleteNote(note.id!!)
                    }
                }
                +"X"
            }
        }

        div(classes = "NoteContents") {
            +note.contents
        }
    }
}
