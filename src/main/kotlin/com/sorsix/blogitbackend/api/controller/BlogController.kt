package com.sorsix.blogitbackend.api.controller

import com.sorsix.blogitbackend.api.requestobjects.BlogDeleteRequest
import com.sorsix.blogitbackend.api.requestobjects.BlogSaveRequest
import com.sorsix.blogitbackend.api.requestobjects.BlogUpdateRequest
import com.sorsix.blogitbackend.api.requestobjects.UpvoteRequest
import com.sorsix.blogitbackend.api.responseobjects.BlogDeleteResponse
import com.sorsix.blogitbackend.api.responseobjects.BlogUpdateResponse
import com.sorsix.blogitbackend.api.responseobjects.BlogUpvoteResponse
import com.sorsix.blogitbackend.model.Blog
import com.sorsix.blogitbackend.model.dto.BlogDto
import com.sorsix.blogitbackend.model.enumeration.Tag
import com.sorsix.blogitbackend.model.results.blog.*
import com.sorsix.blogitbackend.service.BlogService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/blogs")
class BlogController(val blogService: BlogService) {

    @GetMapping("/all")
    fun getAllBlogs(@RequestParam(required = false) tag: String?): ResponseEntity<List<Blog>> {
        return tag?.let {
            ResponseEntity.ok(blogService.getBlogsByTag(Tag.valueOf(tag.uppercase(Locale.getDefault()))))
        } ?: ResponseEntity.ok(blogService.findAll())
    }

    @GetMapping
    fun getBlogs(pageable: Pageable): ResponseEntity<Page<Blog>> {
        return ResponseEntity.ok(blogService.findAllWithPagination(pageable))
    }

    @PostMapping("/add")
    fun saveBlog(@RequestBody request: BlogSaveRequest): ResponseEntity<BlogDto> {
        return when (val result = blogService.save(request.title, request.content, request.userId)) {
            is BlogCreated -> ResponseEntity.ok(result.blogDto)
            is BlogCreateError -> ResponseEntity.internalServerError().build()
        }
    }

    @PutMapping("/update/{id}")
    fun updateBlog(@PathVariable(name = "id") blogId: Long, @RequestBody request: BlogUpdateRequest): ResponseEntity<BlogUpdateResponse> {
        return when (val result = blogService.update(request.title, request.content, blogId, request.userId)) {
            is BlogUpdated -> ResponseEntity.ok(BlogUpdateResponse(result.blogDto, "success"))
            is BlogUpdatePermissionDenied ->
                ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(BlogUpdateResponse(blog = null, message = result.message))
            is BlogUpdateError ->
                ResponseEntity.internalServerError()
                    .body(BlogUpdateResponse(blog = null, message = result.message))
        }
    }

    @DeleteMapping("/delete")
    fun deleteBlog(@RequestBody request: BlogDeleteRequest): ResponseEntity<BlogDeleteResponse> {
        return when (val result = blogService.delete(request.blogId, request.userId)) {
            is BlogDeleted -> ResponseEntity.ok(BlogDeleteResponse(blog = result.blogDto, message = "success"))
            is BlogDeletePermissionDenied ->
                ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(BlogDeleteResponse(blog = null, message = result.message))
            is BlogDeleteError ->
                ResponseEntity.internalServerError()
                    .body(BlogDeleteResponse(blog = null, message = result.message))
        }
    }

    @PostMapping("/upvote")
    fun upvoteBlog(@RequestBody request: UpvoteRequest): ResponseEntity<BlogUpvoteResponse> {
        return when (val result = blogService.like(request.userId, request.blogId)) {
            is BlogLiked -> ResponseEntity.ok(BlogUpvoteResponse(blog = result.blogDto, message = "success"))
            is BlogUnliked -> ResponseEntity.ok(BlogUpvoteResponse(blog = null, message = result.message))
            is BlogLikeError ->
                ResponseEntity.internalServerError()
                    .body(BlogUpvoteResponse(blog = null, message = result.message))
        }
    }

}