import phoestit.NoteRepository
import phoestit.NoteService
import phoestit.model.Note

external fun require(module:String):dynamic

val noteRepository = NoteRepository()
val noteService = NoteService(noteRepository)

enum class SocketRequest(val event: String) {
    CONNECTION("connection"),
    DISCONNECTED("disconnected"),
    CREATE_NOTE("create_note"),
    UPDATE_NOTE("update_note"),
    UPDATE_NOTE_CONTENTS("update_note_contents"),
    MOVE_NOTE("move_note"),
    DELETE_NOTE("delete_note")
}

enum class SocketResponse(val event: String) {
    NOTES("notes"),
    NOTE_UPDATED("update_note"),
    NOTE_CREATED("create_note"),
    NOTE_DELETED("delete_note")
}

fun main(args: Array<String>) {

    val express = require("express")
    val app = express()

    // Static files
    app.use(express.static("public"))

    // Socket.io server
    val server = require("http").Server(app)
    val io = require("socket.io")(server)

    // Bodyparser for handling post body data
    val bodyParser = require("body-parser")
    app.use(bodyParser.json())

    /**
     * Socket API
     */
    io.on(SocketRequest.CONNECTION.event, { socket ->

        console.log("${socket.handshake.address} connected")

        noteService.findNotes().then { notes ->
            socket.emit(SocketResponse.NOTES.event, notes)
        }

        socket.on(SocketRequest.CREATE_NOTE.event, { note ->
            noteService.createNote(note).then { id ->
                console.log("${socket.handshake.address} created note $id")
                noteService.findNoteById(id)
            }
            .then {
                io.emit(SocketResponse.NOTE_CREATED.event, it)
            }
        })

        socket.on(SocketRequest.UPDATE_NOTE.event, { note ->
            console.log("${socket.handshake.address} updates note", note)
            noteService.updateNote(note.id, note)
                .then {
                    noteService.findNoteById(note.id)
                }
                .then {
                    io.emit(SocketResponse.NOTE_UPDATED.event, it)
                }
        })

        socket.on(SocketRequest.UPDATE_NOTE_CONTENTS.event, { note ->
            noteService.updateNoteContents(note.id, note.contents)
                .then {
                    noteService.findNoteById(note.id)
                }
                .then {
                    io.emit(SocketResponse.NOTE_UPDATED.event, it)
                }
        })

        socket.on(SocketRequest.MOVE_NOTE.event, { note ->
            noteService.moveNote(note.id, note.x, note.y, note.width, note.height)
                .then {
                    noteService.findNoteById(note.id)
                }
                .then {
                    io.emit(SocketResponse.NOTE_UPDATED.event, it)
                }
        })

        socket.on(SocketRequest.DELETE_NOTE.event, { id ->
            noteService.deleteNote(id)
                .then {
                    console.log("${socket.handshake.address} deleted note $id")
                    io.emit(SocketResponse.NOTE_DELETED.event, id)
                }
        })

        socket.on("disconnect", {
            console.log("${socket.handshake.address} disconnected")
        })
    })

    /**
     * HTTP API
     */

    // Returns all notes
    app.get("/notes", { _, res ->
        noteService.findNotes().then { notes ->
            res.type("application/json")
            res.send(notes)
        }
    })

    // Returns note by ID
    app.get("/notes/:id", { req, res ->
        noteService.findNoteById(req.params.id).then { note ->
            res.type("application/json")
            res.send(note)
        }
    })

    // Creates a new note
    app.post("/notes", { req, res ->
        val note = req.body as Note
        noteService.createNote(note)
                .then { result ->
                    res.type("application/json")
                    res.send(result)
                }
    })

    // Updates a note with specific API ID
    app.put("/api/notes/:apiId", { req, res ->
        val apiId = req.params.apiId
        val contents = res.body.contents
        noteService.updateNoteByApiId(apiId, contents)
            .then { note ->
                io.emit("update_note", note)
                res.type("application/json")
                res.send(note)
            }
    })

    /**
     * Start up the server
     */
    server.listen(3000, {
        println("Listening on port 3000")
    })
}
