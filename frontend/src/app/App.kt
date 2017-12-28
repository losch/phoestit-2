package app

import react.*
import react.dom.*
import store.*
import api.socket
import kotlinx.html.js.onClickFunction
import model.Note

class App : RComponent<RProps, RState>() {

    init {
        // Subscribe to store. Any change in store will trigger re-rendering

        store.subscribe {
            forceUpdate()
        }

        // Connect sockets

        socket.on("notes") { notes ->
            store.dispatch(notesReceivedAction(notes))
        }

        socket.on("create_note") { note ->
            store.dispatch(noteCreatedAction(note))
        }

        socket.on("update_note") { note ->
            store.dispatch(noteUpdatedAction(note))
        }

        socket.on("delete_note") { id ->
            store.dispatch(noteDeletedAction(id))
        }
    }

    private fun createNewNote() {
        val note = Note(
            id = null,
            apiId= null,
            contents = "Uusi lappu",
            x = 12,
            y = 5,
            height = 1,
            width = 1
        )

        api.createNote(note)
    }

    override fun RBuilder.render() {
        val state = store.getState()

        notesGrid(state.notes.notes)

        button(classes = "CreateNoteButton") {
            attrs {
                onClickFunction = {
                    createNewNote()
                }
            }

            +"Uusi lappu"
        }
    }
}

fun RBuilder.app() = child(App::class) {}
