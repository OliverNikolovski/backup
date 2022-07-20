package com.sorsix.blogitbackend.model.results.comment

sealed interface CommentResult {
    fun success(): Boolean
}

