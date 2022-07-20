package com.sorsix.blogitbackend.model.results

import com.sorsix.blogitbackend.model.Comment

sealed interface CommentResult {
    fun success(): Boolean
}

data class CommentCreated(val comment: Comment) : CommentResult {
    override fun success() = true
}

data class CommentLiked(val comment: Comment) : CommentResult {
    override fun success() = true
}

data class CommentNotLiked(val message: String) : CommentResult {
    override fun success() = false
}

data class CommentNotExisting(val message: String) : CommentResult {
    override fun success() = false
}

data class BlogNotExisting(val message: String) : CommentResult {
    override fun success() = false
}

data class UserNotExisting(val message: String) : CommentResult {
    override fun success() = false
}

data class UsersNotMatch(val message: String) : CommentResult {
    override fun success() = false
}
