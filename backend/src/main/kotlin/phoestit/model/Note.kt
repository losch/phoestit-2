package phoestit.model

external interface NoteDto {
    var id: Int?
    var apiId: String?
    var contents: String
    var x: Int
    var y: Int
    var width: Int
    var height: Int
}

data class Note(
    val id: Int?,
    val apiId: String?,
    val contents: String,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int) {

    companion object {
        fun fromDto(noteDto: NoteDto) =
            Note(
                id = noteDto.id,
                apiId = noteDto.apiId,
                contents = noteDto.contents,
                x = noteDto.x,
                y = noteDto.y,
                width = noteDto.width,
                height = noteDto.height)
    }
}
