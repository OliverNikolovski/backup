package com.sorsix.blogitbackend.model.results.comment

data class CommentCreateError(val message: String) : CommentResult, CommentSaveResult, CommentUpdateResult,
    CommentLikedResult {
    override fun success() = false
}
