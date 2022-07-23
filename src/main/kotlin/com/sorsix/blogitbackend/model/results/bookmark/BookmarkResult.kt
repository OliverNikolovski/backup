package com.sorsix.blogitbackend.model.results.bookmark

import com.sorsix.blogitbackend.model.Blog

sealed interface BookmarkResult {
    fun success(): Boolean
}

data class BookmarkCreated(val message: String) : BookmarkResult {
    override fun success() = true
}

data class BookmarkError(val message: String) : BookmarkResult {
    override fun success() = false
}

data class BookmarkRemoved(val message: String) : BookmarkResult {
    override fun success() = true
}
