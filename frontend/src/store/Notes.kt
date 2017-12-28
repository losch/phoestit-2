package store

import kotlinext.js.jsObject
import model.Note

val RECEIVED_NOTES = "notes/RECEIVED_NOTES"
val NOTE_CREATED = "notes/NOTE_CREATED"
val NOTE_UPDATED = "notes/NOTE_UPDATED"
val NOTE_DELETED = "notes/NOTE_DELETED"

external interface NotesReceivedAction : Action {
    var notes: Array<Note>
}

external interface NoteCreatedAction : Action {
    var note: Note
}

external interface NoteUpdatedAction : Action {
    var note: Note
}

external interface NoteDeletedAction : Action {
    var id: Int
}

fun notesReceivedAction(notes_: Array<Note>): NotesReceivedAction = jsObject {
    type = RECEIVED_NOTES
    notes = notes_
}

fun noteCreatedAction(note_: Note): NoteCreatedAction = jsObject {
    type = NOTE_CREATED
    note = note_
}

fun noteUpdatedAction(note_: Note): NoteUpdatedAction = jsObject {
    type = NOTE_UPDATED
    note = note_
}

fun noteDeletedAction(id_: Int): NoteDeletedAction = jsObject {
    type = NOTE_DELETED
    id = id_
}

data class NotesState(val notes: Array<Note>)

val initialState = NotesState(
        notes = arrayOf()
)

fun notesReducer(state: NotesState = initialState, action: Action): NotesState =
    when(action.type) {
        RECEIVED_NOTES ->
            state.copy(notes = (action as NotesReceivedAction).notes)

        NOTE_DELETED -> {
            val id = (action as NoteDeletedAction).id
            state.copy(notes = state.notes.filter { it.id != id }.toTypedArray())
        }

        NOTE_CREATED -> {
            val note = (action as NoteCreatedAction).note
            state.copy(notes = state.notes + note)
        }

        NOTE_UPDATED -> {
            val newNote = (action as NoteUpdatedAction).note

            fun addNote(note: Note) = when {
                note.id == newNote.id -> newNote
                else                  -> note
            }

            state.copy(notes = state.notes.map { addNote(it) }.toTypedArray())
        }

        else -> state
    }
