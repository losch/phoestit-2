package model

data class Note(
        val id: Int?,
        val apiId: String?,
        val contents: String,
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int)