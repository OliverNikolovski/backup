package com.sorsix.blogitbackend.model.results.comment

data class CommentNotExisting(val message: String) :
    CommentResult, CommentLikedResult, CommentUpdateResult, CommentDeleteResult {
    override fun success() = false
}