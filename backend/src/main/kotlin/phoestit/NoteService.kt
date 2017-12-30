package phoestit

import phoestit.model.Note
import kotlin.js.Promise

class NoteService (private val noteRepository: NoteRepository) {
    fun findNotes(): Promise<List<Note>> =
            noteRepository.getNotes()

    fun findNoteById(id: Int): Promise<Note> =
            noteRepository.findNoteById(id)

    fun createNote(note: Note): Promise<Note> =
            noteRepository.createNote(note)
                    .then { id -> findNoteById(id) }
                    .then { it }

    fun updateNote(id: Int, note: Note): Promise<Note> =
            noteRepository.updateNote(id, note)
                    .then { findNoteById(id) }
                    .then { it }

    fun updateNoteApiId(id: Int, apiId: String): Promise<Note> =
            noteRepository.updateNoteApiId(id, apiId)
                    .then { findNoteById(id) }
                    .then { it }

    fun updateNoteContents(id: Int, contents: String): Promise<Note> =
            noteRepository.updateNoteContents(id, contents)
                    .then { findNoteById(id) }
                    .then { it }

    fun moveNote(id: Int, x: Int, y: Int, width: Int, height: Int): Promise<Note> =
            noteRepository.moveNote(id, x, y, width, height)
                    .then { findNoteById(id) }
                    .then { it }

    fun deleteNote(id: Int): Promise<Int> =
            noteRepository.deleteNote(id)

    fun updateNoteByApiId(apiId: String, contents: String): Promise<Note> =
            noteRepository.updateNoteByApiId(apiId, contents)
                    .then { noteRepository.findNoteByApiId(apiId) }
                    .then { it }
}
