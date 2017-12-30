package app

import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import model.Note
import org.w3c.dom.HTMLTextAreaElement
import react.*
import react.dom.*
import kotlin.browser.window

@JsModule("react-markdown")
external val reactMarkdown : RClass<ReactMarkdownProps>

external interface ReactMarkdownProps : RProps {
    var source: String
}

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

    private fun editApiId() {
        val newId = window.prompt("Uusi API-ID?", props.note.apiId ?: "")

        if (newId != null) {
            console.log("Updating api id", newId)
            api.updateNoteApiId(props.note.id!!, newId)
        }
    }

    override fun RBuilder.render() {
        val note = props.note

        div("NoteBody") {
            div("NoteButtons") {
                button(classes = "ApiIdNoteButton") {
                    attrs {
                        onClickFunction = {
                            editApiId()
                        }
                    }

                    +"#"
                    if (!note.apiId.isNullOrEmpty()) {
                        +"${note.apiId}"
                    }
                }

                button(classes = "EditNoteButton") {
                    attrs {
                        onClickFunction = {
                            setState {
                                isInEditMode = !state.isInEditMode
                            }
                        }
                    }
                    if (state.isInEditMode) +"OK" else +"E"
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
                        defaultValue = note.contents

                        onChangeFunction = {
                            val target = it.target as HTMLTextAreaElement
                            api.updateNoteContents(note.id!!, target.value)
                        }
                    }
                }
            }
            else {
                div(classes = "NoteContents") {
                    reactMarkdown {
                        attrs.source = note.contents
                    }
                }
            }

            div(classes = "NoteDragHandle") {}
        }
    }
}

fun RBuilder.note(note: Note) = child(NoteComponent::class) {
    attrs.note = note
}
