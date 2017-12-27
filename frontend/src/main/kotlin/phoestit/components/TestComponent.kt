package phoestit.components

import kotlinx.html.js.onClickFunction
import phoestit.model.Note
import phoestit.store.Action
import phoestit.store.Store
import phoestit.store.StoreState
import phoestit.store.notesReceivedAction
import react.*
import react.dom.button
import react.dom.div

class TestComponentProps(var state: StoreState,
                         var dispatch: (action: Action) -> Unit) : RProps
class TestComponentState(var counter: Int) : RState

class TestComponent(props: TestComponentProps) : RComponent<TestComponentProps, TestComponentState>(props) {

    init {
        state = TestComponentState(counter = 0)
    }

    private fun increment() {

        val action = notesReceivedAction(
                arrayOf(
                        Note(1, null, "Test", 0, 0, 100, 100),
                        Note(2, null, "Egss, ham, bacon", 0, 0, 100, 100)
                )
        )

        props.dispatch(action)
    }

    override fun RBuilder.render() {
        div {
            div(classes = "subdiv") {
                +"Hello world!"
            }

            div(classes = "subdiv") {
                props.state.notes.notes.forEach { note ->
                    +"id: ${note.id}"
                    +"contents: ${note.contents}"
                }
            }

            button {
                attrs {
                    onClickFunction = { increment() }
                }
                +"Click me"
            }
        }
    }
}

fun RBuilder.testComponent(store: Store) = child(TestComponent::class) {
    attrs.state = store.getState()
    attrs.dispatch = { store.dispatch(it) }
}
