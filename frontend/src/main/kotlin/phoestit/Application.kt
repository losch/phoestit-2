package phoestit

import kotlinext.js.jsObject
import phoestit.components.newNoteButton
import phoestit.components.noteComponent
import phoestit.model.Note
import phoestit.model.UpdateNoteContentsDto
import phoestit.store.*
import react.*
import react.dom.div
import kotlin.js.Math

class ApplicationProps(var state: StoreState,
                       var dispatch: (action: Action) -> Unit) : RProps {}

class Application(props: ApplicationProps) : RComponent<ApplicationProps, RState>(props) {

    private fun onDeleteNote(id: Int) = {
        socket.emit("delete_note", id)
        props.dispatch(noteDeletedAction(id))
    }

    private fun onNoteContentsChanged(id_: Int) = { contents_: String ->
        val update: UpdateNoteContentsDto = jsObject {
            id = id_
            contents = contents_
        }

        console.log("*** Updating", id_, contents_)

        socket.emit("update_note_contents", update)
    }

    private fun onNewNoteClick() = {
        val note = Note(
                id = null,
                apiId = null,
                contents = "New note",
                x = (Math.random() * 100).toInt(),
                y = (Math.random() * 100).toInt(),
                width = 100,
                height = 100
        )

        socket.emit("create_note", note)
    }

    override fun RBuilder.render() {
        div {
            props.state.notes.notes.map { note ->
                noteComponent(note,
                              { onDeleteNote(note.id!!) },
                              { onNoteContentsChanged(note.id!!) }
                )
            }
        }

        newNoteButton({onNewNoteClick()})
    }
}

fun RBuilder.application(store: Store) = child(Application::class) {
    attrs.state = store.getState()
    attrs.dispatch = { store.dispatch(it) }
}
