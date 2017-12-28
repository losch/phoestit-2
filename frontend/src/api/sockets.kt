package api

import model.Note

external fun require(module:String): dynamic

val openSocket = require("socket.io-client")
val socket: dynamic = openSocket("http://localhost:3000")

fun createNote(note: Note) {
    socket.emit("create_note", note)
}

fun deleteNote(id: Int) {
    socket.emit("delete_note", id)
}
