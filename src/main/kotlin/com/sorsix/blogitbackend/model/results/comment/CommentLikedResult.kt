package com.sorsix.blogitbackend.model.results.comment

import com.sorsix.blogitbackend.model.Comment

sealed interface CommentLikedResult : CommentResult


data class CommentLiked(val comment: Comment) : CommentLikedResult {
    override fun success() = true
}

data class CommentAlreadyLiked(val message: String) : CommentLikedResult {
    override fun success() = false
}

data class CommentNotLiked(val message: String) : CommentLikedResult {
    override fun success() = false
}