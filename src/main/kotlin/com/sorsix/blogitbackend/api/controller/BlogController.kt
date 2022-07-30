package com.sorsix.blogitbackend.api.controller

import com.sorsix.blogitbackend.api.requestobjects.BlogUpdateRequest
import com.sorsix.blogitbackend.api.responseobjects.BlogDeleteResponse
import com.sorsix.blogitbackend.api.responseobjects.BlogUpdateResponse
import com.sorsix.blogitbackend.api.responseobjects.BlogUpvoteResponse
import com.sorsix.blogitbackend.model.dto.BlogDto
import com.sorsix.blogitbackend.model.enumeration.Tag
import com.sorsix.blogitbackend.model.results.blog.*
import com.sorsix.blogitbackend.service.BlogService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = ["http://localhost:4200"])
class BlogController(val blogService: BlogService) {

    @GetMapping("/all")
    fun getAllBlogs(@RequestParam(required = false) tag: String?): ResponseEntity<List<BlogDto>> {
//        return tag?.let {
//            ResponseEntity.ok(blogService.getBlogsByTag(Tag.valueOf(tag.uppercase(Locale.getDefault()))))
//        } ?: ResponseEntity.ok(blogService.findAll())
        val res = tag?.let {
            ResponseEntity.ok(blogService.getBlogsByTag(Tag.valueOf(tag.uppercase(Locale.getDefault()))))
        } ?: ResponseEntity.ok(blogService.findAll())
        return res;
    }

    @GetMapping
    fun getBlogs(pageable: Pageable): ResponseEntity<Page<BlogDto>> {
        return ResponseEntity.ok(blogService.findAllWithPagination(pageable))
    }

    @GetMapping("/{id}")
    fun getBlogById(@PathVariable id: Long): ResponseEntity<BlogDto> =
        this.blogService.findById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()

    @PostMapping("/add")
    fun saveBlog(@RequestParam title: String,
                 @RequestParam content: String,
                 @RequestParam tags: List<String>,
                 @RequestParam(required = false) picture: MultipartFile?): ResponseEntity<BlogDto> {
        return when (val result = blogService.save(title, content, tags, picture)) {
            is BlogCreated -> ResponseEntity.ok(result.blogDto)
            is BlogCreateError -> ResponseEntity.internalServerError().build()
        }
    }

    @PutMapping("/update/{id}")
    fun updateBlog(
        @PathVariable(name = "id") blogId: Long,
        @RequestBody request: BlogUpdateRequest
    ): ResponseEntity<BlogUpdateResponse> {
        return when (val result = blogService.update(request.title, request.content, blogId)) {
            is BlogUpdated -> ResponseEntity.ok(BlogUpdateResponse(result.blogDto, "success"))
            is BlogUpdatePermissionDenied ->
                ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(BlogUpdateResponse(blog = null, message = result.message))
            is BlogUpdateError ->
                ResponseEntity.internalServerError()
                    .body(BlogUpdateResponse(blog = null, message = result.message))
        }
    }

    @DeleteMapping("/delete/{id}")
    fun deleteBlog(@PathVariable(name = "id") blogId: Long): ResponseEntity<BlogDeleteResponse> {
        return when (val result = blogService.delete(blogId)) {
            is BlogDeleted -> ResponseEntity.ok(BlogDeleteResponse(blog = result.blogDto, message = "success"))
            is BlogDeletePermissionDenied ->
                ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(BlogDeleteResponse(blog = null, message = result.message))
            is BlogDeleteError ->
                ResponseEntity.internalServerError()
                    .body(BlogDeleteResponse(blog = null, message = result.message))
        }
    }

    @PostMapping("/upvote/{id}")
    fun upvoteBlog(@PathVariable(name = "id") blogId: Long): ResponseEntity<BlogUpvoteResponse> {
        return when (val result = blogService.like(blogId)) {
            is BlogLiked -> ResponseEntity.ok(BlogUpvoteResponse(blog = result.blogDto, message = "success"))
            is BlogUnliked -> ResponseEntity.ok(BlogUpvoteResponse(blog = null, message = result.message))
            is BlogLikeError ->
                ResponseEntity.internalServerError()
                    .body(BlogUpvoteResponse(blog = null, message = result.message))
        }
    }

}