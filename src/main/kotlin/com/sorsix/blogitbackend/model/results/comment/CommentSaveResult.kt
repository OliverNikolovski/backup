package com.sorsix.blogitbackend.model.results.comment

sealed interface CommentSaveResult : CommentResult

data class BlogNotExisting(val message: String) : CommentSaveResult {
    override fun success() = false
}