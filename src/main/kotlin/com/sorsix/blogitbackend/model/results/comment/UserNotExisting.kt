package com.sorsix.blogitbackend.model.results.comment

data class UserNotExisting(val message: String) : CommentResult, CommentSaveResult, CommentUpdateResult {
    override fun success() = false
}
