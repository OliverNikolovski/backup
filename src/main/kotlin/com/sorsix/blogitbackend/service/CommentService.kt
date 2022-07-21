package com.sorsix.blogitbackend.service

import com.sorsix.blogitbackend.model.Comment
import com.sorsix.blogitbackend.model.results.comment.CommentLikedResult
import com.sorsix.blogitbackend.model.results.comment.CommentSaveResult
import com.sorsix.blogitbackend.model.results.comment.CommentUpdateResult

interface CommentService {

    fun findAll(): List<Comment>

    fun like(comment_id: Long, user_id: Long): CommentLikedResult

    fun save(content: String, user_id: Long, blog_id: Long): CommentSaveResult

    fun update(user_id: Long, comment_id: Long, content: String): CommentUpdateResult

    fun delete(comment_id: Long)
}