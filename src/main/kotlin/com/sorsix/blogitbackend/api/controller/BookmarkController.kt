package com.sorsix.blogitbackend.api.controller

import com.sorsix.blogitbackend.api.responseobjects.BookmarkResponse
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

    @GetMapping
    fun getBookmarksForUser(): ResponseEntity<List<Blog>> =
        ResponseEntity.ok(blogService.getBookmarksForLoggedInUser())

    @PostMapping("/save/{blogId}")
    fun saveOrRemoveBookmarkForUser(@PathVariable blogId: Long): ResponseEntity<BookmarkResponse> {
        return when (val result = blogService.createBookmarkForLoggedInUser(blogId)) {
            is BookmarkCreated -> ResponseEntity.ok(BookmarkResponse(result.message))
            is BookmarkRemoved -> ResponseEntity.ok(BookmarkResponse(result.message))
            is BookmarkError -> ResponseEntity.internalServerError().build()
        }
    }

}