package api

import app.LayoutInterface
import model.Note
import model.NoteApiId
import model.NoteContents
import model.NotePositionDimension

external fun require(module:String): dynamic

val openSocket = require("socket.io-client")
val socket: dynamic = openSocket("http://localhost:3000")

fun createNote(note: Note) {
    socket.emit(SocketRequest.CREATE_NOTE.event, note)
}

fun deleteNote(id: Int) {
    socket.emit(SocketRequest.DELETE_NOTE.event, id)
}

fun updateNoteApiId(id: Int, apiId: String) {
    socket.emit(SocketRequest.UPDATE_NOTE_API_ID.event, NoteApiId(id, apiId))
}

fun updateNoteContents(id: Int, contents: String) {
    socket.emit(SocketRequest.UPDATE_NOTE_CONTENTS.event, NoteContents(id, contents))
}

fun moveNote(id: Int, layout: LayoutInterface) {
    socket.emit(SocketRequest.MOVE_NOTE.event,
                NotePositionDimension(
                    id,
                    layout.x,
                    layout.y,
                    layout.w,
                    layout.h
                ))
}

enum class SocketRequest(val event: String) {
    CONNECTED("connected"),
    DISCONNECTED("disconnected"),
    CREATE_NOTE("create_note"),
    UPDATE_NOTE("update_note"),
    UPDATE_NOTE_CONTENTS("update_note_contents"),
    UPDATE_NOTE_API_ID("update_note_api_id"),
    MOVE_NOTE("move_note"),
    DELETE_NOTE("delete_note")
}

enum class SocketResponse(val event: String) {
    NOTES("notes"),
    NOTE_UPDATED("update_note"),
    NOTE_CREATED("create_note"),
    NOTE_DELETED("delete_note")
}
