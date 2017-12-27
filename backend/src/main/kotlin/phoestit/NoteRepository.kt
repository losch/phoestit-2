@file:Suppress("UnsafeCastFromDynamic")

package phoestit

import phoestit.model.Note
import kotlin.js.Promise
import kotlin.js.json

external fun require(module:String):dynamic

class NoteRepository {
    private val knex = require("knex")(js("{client: 'sqlite3', connection: { filename: './db.sqlite' }}"))

    fun getNotes(): Promise<List<Note>> =
        knex("notes")
            .select("*")


    fun findNoteById(id: Int): Promise<Note> =
        knex("notes")
            .select("*")
            .where(json("id" to id))
            .then { notes -> notes[0] }

    fun createNote(note: Note): Promise<Int> =
        knex("notes")
            .insert(note)
            .returning("id")
            .then { ids -> ids[0] }

    fun updateNote(id: Int, note: Note): Promise<Note> =
        knex("notes")
            .where(json("id" to id))
            .update(note)
            .returning("*")

    fun updateNoteContents(id: Int, contents: String): Promise<Note> =
        knex("notes")
            .where(json("id" to id))
            .update(json("contents" to contents))
            .returning("*")

    fun deleteNote(id: Int): Promise<Int> =
        knex("notes")
            .where(json("id" to id))
            .delete()

    fun updateNoteByApiId(apiId: String, contents: String): Promise<Note> =
        knex("notes")
            .where(json("apiId" to apiId))
            .update(json("contents" to contents))
            .returning("*")

    init {
        knex.schema.createTableIfNotExists("notes") { table ->
            table.increments()
            table.integer("apiId")
            table.string("contents")
            table.integer("x")
            table.integer("y")
            table.integer("width")
            table.integer("height")
        }.then {
            _ -> "OK"
        }
    }
}
