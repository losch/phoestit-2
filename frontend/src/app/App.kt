package app

import api.SocketResponse
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

        console.log(SocketResponse.NOTES.event)

        // Connect sockets

        socket.on(SocketResponse.NOTES.event) { notes ->
            store.dispatch(notesReceivedAction(notes))
        }

        socket.on(SocketResponse.NOTE_CREATED.event) { note ->
            store.dispatch(noteCreatedAction(note))
        }

        socket.on(SocketResponse.NOTE_UPDATED.event) { note ->
            store.dispatch(noteUpdatedAction(note))
        }

        socket.on(SocketResponse.NOTE_DELETED.event) { id ->
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
            height = 2,
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
