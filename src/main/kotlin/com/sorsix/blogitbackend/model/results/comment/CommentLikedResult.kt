package com.sorsix.blogitbackend.model.results.comment

import com.sorsix.blogitbackend.model.Comment

sealed interface CommentLikedResult : CommentResult


data class CommentLiked(val comment: Comment) : CommentLikedResult {
    override fun success() = true
}

data class CommentUnliked(val message: String) : CommentLikedResult {
    override fun success() = true
}

data class CommentLikeError(val message: String) : CommentLikedResult {
    override fun success() = false
}