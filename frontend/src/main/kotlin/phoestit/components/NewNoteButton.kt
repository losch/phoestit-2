package phoestit.components

import kotlinext.js.js
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import react.dom.jsStyle

class NewNoteButtonProps(var onClick: () -> Unit) : RProps

class NewNoteButton(props: NewNoteButtonProps) : RComponent<NewNoteButtonProps, RState>(props) {

    override fun RBuilder.render() {
        button {
            attrs.jsStyle = js {
                position = "absolute"
                right = "15px"
                bottom = "15px"
            }

            attrs {
                onClickFunction = {
                    props.onClick()
                }
            }

            +"Luo uusi"
        }
    }
}

fun RBuilder.newNoteButton(onClick: () -> () -> Unit) =
        child(NewNoteButton::class) {
            attrs.onClick = onClick()
        }
