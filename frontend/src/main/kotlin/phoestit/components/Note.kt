package phoestit.components

import kotlinext.js.js
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLTextAreaElement
import phoestit.model.Note
import react.*
import react.dom.*

class NoteProps(var note: Note,
                var onDeleteNote: () -> Unit,
                var onContentsChanged: (contents: String) -> Unit) : RProps

class NoteState(var isInEditMode: Boolean,
                var contents: String) : RState

class NoteComponent(props: NoteProps) : RComponent<NoteProps, NoteState>(props) {

    init {
        state = NoteState(isInEditMode = false,
                          contents = props.note.contents)
    }

    private fun toggleEditMode() {
        setState {
            isInEditMode = !state.isInEditMode
        }
    }

    override fun componentWillReceiveProps(nextProps: NoteProps) {
        if (nextProps.note.contents != props.note.contents) {
            setState {
                contents = nextProps.note.contents
            }
        }
    }

    override fun RBuilder.render() {
        div {
            attrs.jsStyle = js {
                display = "block"
                position = "absolute"
                left = props.note.x.toString()
                top = props.note.y.toString()
                width = props.note.width.toString()
                height = props.note.height.toString()
                background = "gray"
                border = "1px solid black"
            }

            div {
                attrs.jsStyle = js {
                    position = "absolute"
                    top = "5px"
                    right = "5px"
                }

                button {
                    attrs {
                        onClickFunction = { props.onDeleteNote() }
                    }

                    +"x"
                }

                button {
                    attrs {
                        onClickFunction = { toggleEditMode() }
                    }

                    +"e"
                }
            }

            if (state.isInEditMode) {
                textArea {
                    attrs {
                        value = state.contents
                        onChangeFunction = {
                            val target = it.target as HTMLTextAreaElement
                            setState {
                                contents = target.value
                            }

                            console.log("**** calling onContentsChanged")

                            props.onContentsChanged(target.value)
                        }
                    }
                }
            }
            else {
                +props.note.contents
            }
        }
    }
}

fun RBuilder.noteComponent(note: Note,
                           onDeleteNote: () -> () -> Unit,
                           onContentsChanged: () -> (String) -> Unit) = child(NoteComponent::class) {
    attrs.note = note
    attrs.onDeleteNote = onDeleteNote()
    attrs.onContentsChanged = onContentsChanged()
}
