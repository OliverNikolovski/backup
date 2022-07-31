package com.sorsix.blogitbackend.api.controller

import com.sorsix.blogitbackend.model.dto.CommentDto
import com.sorsix.blogitbackend.model.results.comment.*
import com.sorsix.blogitbackend.service.CommentService
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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

    @DeleteMapping("/delete/{id}")
    fun deleteComment(@PathVariable(name = "id") commentId: Long): ResponseEntity<CommentDeleteResult> {
        return when (val result = commentService.delete(commentId)) {
            is CommentDeleted -> ResponseEntity.ok(CommentDeleted(result.comment))
            is CommentNotExisting -> ResponseEntity.badRequest().build()
            is CommentDeletePermissionDenied -> ResponseEntity.status(FORBIDDEN).build()
            is CommentDeleteError -> ResponseEntity.internalServerError().build()
        }
    }

    @PutMapping("/update/{id}")
    fun updateComment(@PathVariable(name = "id") commentId: Long, @RequestParam content: String):
            ResponseEntity<CommentUpdateResult> {
        return when (val result = commentService.update(comment_id = commentId, content = content)) {
            is CommentCreated -> ResponseEntity.ok(CommentCreated(result.comment))
            is CommentNotExisting -> ResponseEntity.badRequest().build()
            is UsersNotMatch -> ResponseEntity.status(FORBIDDEN).build()
            is CommentCreateError -> ResponseEntity.internalServerError().build()
            else -> ResponseEntity.badRequest().build()
        }
    }

}