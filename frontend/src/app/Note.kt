package app

import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import model.Note
import org.w3c.dom.HTMLTextAreaElement
import react.*
import react.dom.button
import react.dom.div
import react.dom.textArea
import react.dom.value

interface NoteComponentProps : RProps {
    var note: Note
}

interface NoteComponentState : RState {
    var isInEditMode: Boolean
}

class NoteComponent(props: NoteComponentProps) : RComponent<NoteComponentProps, NoteComponentState>(props) {
    override fun NoteComponentState.init(props: NoteComponentProps) {
        isInEditMode = false
    }

    override fun RBuilder.render() {
        val note = props.note

        div {
            div("NoteButtons") {
                button(classes = "EditNoteButton") {
                    attrs {
                        onClickFunction = {
                            setState {
                                isInEditMode = !state.isInEditMode
                            }
                        }
                    }
                    if (state.isInEditMode) +"Valmis" else +"Muokkaa"
                }

                button(classes = "DeleteNoteButton") {
                    attrs {
                        onClickFunction = {
                            console.log("Deleting note ${note.id}")
                            api.deleteNote(note.id!!)
                        }
                    }
                    +"X"
                }
            }

            if (state.isInEditMode) {
                textArea(classes = "NoteContents") {
                    attrs {
                        value = note.contents

                        onChangeFunction = {
                            val target = it.target as HTMLTextAreaElement
                            api.updateNoteContents(note.id!!, target.value)
                        }
                    }
                }
            }
            else {
                div(classes = "NoteContents") {
                    +note.contents
                }
            }

            div(classes = "NoteDragHandle") {

            }
        }
    }
}

fun RBuilder.note(note: Note) = child(NoteComponent::class) {
    attrs.note = note
}
