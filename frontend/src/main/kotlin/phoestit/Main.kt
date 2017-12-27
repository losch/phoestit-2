package phoestit

import phoestit.store.*
import react.dom.*
import kotlin.browser.document

fun main(args: Array<String>) {
    val root = document.getElementById("app")

    socket.on("notes") { notes ->
        console.log("Got", notes)
        store.dispatch(notesReceivedAction(notes))
    }

    socket.on("create_note") { note ->
        console.log("Note $note created")
        store.dispatch(noteCreatedAction(note))
    }

    socket.on("update_note") { note ->
        console.log("Note $note updated")
        store.dispatch(noteUpdatedAction(note))
    }

    socket.on("delete_note") { id ->
        console.log("Note $id deleted")
        store.dispatch(noteDeletedAction(id))
    }

    fun renderApp() {
        render(root, { application(store) })
    }

    store.subscribe {
        renderApp()
    }

    renderApp()
}
