package com.sorsix.blogitbackend.service

import com.sorsix.blogitbackend.model.Comment
import com.sorsix.blogitbackend.model.results.CommentResult

interface CommentService {

    fun findAll(): List<Comment>

    fun like(comment_id: Long): CommentResult

    fun save(content: String, user_id: Long, blog_id: Long): CommentResult

    fun update(user_id: Long, comment_id: Long, content: String): CommentResult

    fun delete(comment_id: Long)
}