package com.sorsix.blogitbackend.model.results.comment

import com.sorsix.blogitbackend.model.dto.CommentDto

sealed interface CommentDeleteResult : CommentResult

data class CommentDeleted(val commentDto: CommentDto) : CommentDeleteResult {
    override fun success() = true
}

data class CommentDeleteError(val message: String) : CommentDeleteResult {
    override fun success() = false
}

data class CommentDeletePermissionDenied(val message: String) : CommentDeleteResult {
    override fun success() = false
}