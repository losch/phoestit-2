package api

import app.LayoutInterface
import model.Note
import model.NoteContents
import model.NotePositionDimension

external fun require(module:String): dynamic

val openSocket = require("socket.io-client")
val socket: dynamic = openSocket("http://localhost:3000")

fun createNote(note: Note) {
    socket.emit("create_note", note)
}

fun deleteNote(id: Int) {
    socket.emit("delete_note", id)
}

fun updateNoteContents(id: Int, contents: String) {
    socket.emit("update_note_contents", NoteContents(id, contents))
}

fun moveNote(id: Int, layout: LayoutInterface) {
    socket.emit("move_note",
                NotePositionDimension(
                    id,
                    layout.x,
                    layout.y,
                    layout.w,
                    layout.h
                ))
}
