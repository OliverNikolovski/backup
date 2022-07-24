package com.sorsix.blogitbackend.api.controller

import com.sorsix.blogitbackend.api.requestresponseobjects.BookmarkResponse
import com.sorsix.blogitbackend.model.Blog
import com.sorsix.blogitbackend.model.results.bookmark.BookmarkCreated
import com.sorsix.blogitbackend.model.results.bookmark.BookmarkError
import com.sorsix.blogitbackend.model.results.bookmark.BookmarkRemoved
import com.sorsix.blogitbackend.service.BlogService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bookmarks")
class BookmarkController(val blogService: BlogService) {

    @GetMapping("{userId}")
    fun getBookmarksForUser(@PathVariable userId: Long): ResponseEntity<List<Blog>> =
        ResponseEntity.ok(blogService.getBookmarksForUser(userId))

    @PostMapping("/save/{blogId}")
    fun saveOrRemoveBookmarkForUser(@PathVariable blogId: Long, @RequestBody userId: Long): ResponseEntity<BookmarkResponse> {
        return when (val result = blogService.createBookmarkForUser(userId, blogId)) {
            is BookmarkCreated -> ResponseEntity.ok(BookmarkResponse(result.message))
            is BookmarkRemoved -> ResponseEntity.ok(BookmarkResponse(result.message))
            is BookmarkError -> ResponseEntity.internalServerError().build()
        }
    }

}