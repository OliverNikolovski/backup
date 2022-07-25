package com.sorsix.blogitbackend.service

import com.sorsix.blogitbackend.model.Comment
import com.sorsix.blogitbackend.model.results.comment.CommentDeleteResult
import com.sorsix.blogitbackend.model.results.comment.CommentLikedResult
import com.sorsix.blogitbackend.model.results.comment.CommentSaveResult
import com.sorsix.blogitbackend.model.results.comment.CommentUpdateResult

interface CommentService {

    fun findAll(blog_id: Long): List<Comment>

    fun like(comment_id: Long): CommentLikedResult

    fun save(content: String, blog_id: Long): CommentSaveResult

    fun update(comment_id: Long, content: String): CommentUpdateResult

    fun delete(comment_id: Long): CommentDeleteResult
}