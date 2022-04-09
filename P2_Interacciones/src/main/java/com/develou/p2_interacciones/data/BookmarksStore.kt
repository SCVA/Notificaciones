package com.develou.p2_interacciones.data

object BookmarksStore {

    var bookmarks: Int = 0
    private set

    fun addBookmark() = bookmarks++
}