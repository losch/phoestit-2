package phoestit

import phoestit.model.Note
import kotlin.js.Promise

class NoteService (private val noteRepository: NoteRepository) {
    fun findNotes(): Promise<List<Note>> = noteRepository.getNotes()

    fun findNoteById(id: Int): Promise<Note> = noteRepository.findNoteById(id)

    fun createNote(note: Note): Promise<Int> = noteRepository.createNote(note)

    fun updateNote(id: Int, note: Note): Promise<Note> = noteRepository.updateNote(id, note)

    fun updateNoteContents(id: Int, contents: String): Promise<Note> = noteRepository.updateNoteContents(id, contents)

    fun deleteNote(id: Int): Promise<Int> = noteRepository.deleteNote(id)

    fun updateNoteByApiId(apiId: String, contents: String): Promise<Note> =
            noteRepository.updateNoteByApiId(apiId, contents)
}
