import phoestit.NoteRepository
import phoestit.NoteService
import phoestit.model.Note

external fun require(module:String):dynamic

val noteRepository = NoteRepository()
val noteService = NoteService(noteRepository)

fun main(args: Array<String>) {

    val express = require("express")
    val app = express()

    // Static files
    app.use(express.static("public"))

    // Socket.io server
    val server = require("http").Server(app)
    val io = require("socket.io")(server)

    // Templates
    app.set("view engine", "pug")

    // Bodyparser for handling post body data
    val bodyParser = require("body-parser")
    app.use(bodyParser.json())

    /**
     * Sockets
     */
    io.on("connection", { socket ->

        console.log("${socket.handshake.address} connected")

        noteService.findNotes().then { notes ->
            socket.emit("notes", notes)
        }

        socket.on("create_note", { note ->
            noteService.createNote(note).then { id ->
                console.log("${socket.handshake.address} created note $id")
                noteService.findNoteById(id)
            }
            .then { newNote ->
                socket.broadcast.emit("create_note", newNote)
                socket.emit("create_note", newNote)
            }
        })

        socket.on("update_note", { note ->
            noteService.updateNote(note.id, note).then { newNote ->
                console.log("${socket.handshake.address} updated note", note)
                socket.broadcast.emit("update_note", newNote)
            }
        })

        socket.on("update_note_contents", { note ->
            noteService.updateNoteContents(note.id, note.contents).then {
                console.log("${socket.handshake.address} updated note", note)
                noteService.findNoteById(note.id)
                           .then { note ->
                               socket.broadcast.emit("update_note", note)
                               socket.emit("update_note", note)
                           }
            }
        })

        socket.on("move_note", { note ->
            console.log("${socket.handshake.address} moved note", note)
            noteService.moveNote(note.id, note.x, note.y, note.width, note.height).then {
                noteService.findNoteById(note.id)
                        .then { note ->
                            socket.broadcast.emit("update_note", note)
                            socket.emit("update_note", note)
                        }
            }
        })

        socket.on("delete_note", { id ->
            noteService.deleteNote(id)
                .then {
                    console.log("${socket.handshake.address} deleted note $id")
                    socket.broadcast.emit("delete_note", id)
                    socket.emit("delete_note", id)
                }
        })

        socket.on("disconnect", {
            console.log("${socket.handshake.address} disconnected")
        })
    })

    /**
     * Returns notes
     */
    app.get("/notes", { req, res ->
        noteService.findNotes().then { notes ->
            res.type("application/json")
            res.send(notes)
        }
    })

    app.get("/notes/:id", { req, res ->
        noteService.findNoteById(req.params.id).then { note ->
            res.type("application/json")
            res.send(note)
        }
    })

    /**
     * Creates a note
     */
    app.post("/notes", { req, res ->
        val note = req.body as Note
        noteService.createNote(note)
                .then { result ->
                    res.type("application/json")
                    res.send(result)
                }
    })

    /**
     * Updates an API generated note
     */
    app.put("/api/notes/:apiId", { req, res ->
        val apiId = req.params.apiId
        val contents = res.body.contents
        noteService.updateNoteByApiId(apiId, contents)
            .then { note ->
                io.emit("note_updated", note)
                res.type("application/json")
                res.send(note)
            }
    })

    // Start up the server
    server.listen(3000, {
        println("Listening on port 3000")
    })
}
