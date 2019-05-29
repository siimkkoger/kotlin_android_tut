package com.example.kotlin_android_tut

class FeedEntry {
    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var summary: String = ""
    var imageURL: String = ""
    override fun toString(): String {
        return "FeedEntry(name='$name', artist='$artist', releaseDate='$releaseDate', summary='$summary', imageURL='$imageURL')".trimIndent()
    }
}