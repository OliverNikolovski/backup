package com.sorsix.blogitbackend.model.results.comment

data class UserNotExisting(val message: String) :
    CommentResult, CommentSaveResult, CommentUpdateResult, CommentDeleteResult, CommentLikedResult {
    override fun success() = false
}
