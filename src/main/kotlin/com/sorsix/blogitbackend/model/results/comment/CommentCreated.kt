package com.sorsix.blogitbackend.model.results.comment

import com.sorsix.blogitbackend.model.dto.CommentDto

data class CommentCreated(val comment: CommentDto) : CommentResult, CommentSaveResult, CommentUpdateResult {
    override fun success() = true
}
