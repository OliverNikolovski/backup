package com.sorsix.blogitbackend.model.results.comment

import com.sorsix.blogitbackend.model.Comment

data class CommentCreated(val comment: Comment) : CommentResult, CommentSaveResult, CommentUpdateResult {
    override fun success() = true
}
