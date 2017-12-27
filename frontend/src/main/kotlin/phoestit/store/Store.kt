@file:Suppress("UnsafeCastFromDynamic")

package phoestit.store

external fun require(module:String): dynamic

val createStore: dynamic = require("redux").createStore
val combineReducers: dynamic = require("redux").combineReducers

interface StoreState {
    val notes: NotesState
}

class Reducers(
    val notes: (state: NotesState, action: Action) -> NotesState
)

val rootReducer: dynamic = combineReducers(Reducers(notes = ::notesReducer))

external interface Action {
    var type: String
}

external interface Store {
    fun getState(): StoreState
    fun subscribe(callback: () -> Any)
    fun dispatch(action: Action)
}

val store: Store = createStore(rootReducer)
