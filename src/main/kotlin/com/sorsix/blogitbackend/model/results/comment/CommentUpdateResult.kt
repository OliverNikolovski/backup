package com.sorsix.blogitbackend.model.results.comment

interface CommentUpdateResult : CommentResult

data class UsersNotMatch(val message: String) : CommentUpdateResult {
    override fun success() = false
}
