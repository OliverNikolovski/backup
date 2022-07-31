package com.sorsix.blogitbackend.api.controller

import com.sorsix.blogitbackend.model.dto.CommentDto
import com.sorsix.blogitbackend.model.results.comment.BlogNotExisting
import com.sorsix.blogitbackend.model.results.comment.CommentCreateError
import com.sorsix.blogitbackend.model.results.comment.CommentCreated
import com.sorsix.blogitbackend.model.results.comment.CommentSaveResult
import com.sorsix.blogitbackend.service.CommentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/comments")
class CommentController(val commentService: CommentService) {

    @GetMapping("/blog/{blogId}")
    fun getCommentsForBlog(@PathVariable blogId: Long): ResponseEntity<List<CommentDto>> =
        ResponseEntity.ok(commentService.findAll(blogId))

    @PostMapping("/save/{blogId}")
    fun saveComment(@PathVariable blogId: Long, @RequestParam content: String): ResponseEntity<CommentSaveResult> {
        return when (val result = commentService.save(content = content, blog_id = blogId)) {
            is CommentCreated -> ResponseEntity.ok(CommentCreated(result.comment))
            is BlogNotExisting -> ResponseEntity.badRequest().build()
            is CommentCreateError -> ResponseEntity.internalServerError().build()
        }
    }

}