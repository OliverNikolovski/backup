package com.sorsix.blogitbackend.service

import com.sorsix.blogitbackend.model.Blog
import com.sorsix.blogitbackend.model.dto.BlogDto
import com.sorsix.blogitbackend.model.enumeration.Tag
import com.sorsix.blogitbackend.model.results.blog.BlogCreateResult
import com.sorsix.blogitbackend.model.results.blog.BlogDeleteResult
import com.sorsix.blogitbackend.model.results.blog.BlogLikeResult
import com.sorsix.blogitbackend.model.results.blog.BlogUpdateResult
import com.sorsix.blogitbackend.model.results.bookmark.BookmarkResult
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BlogService {

    fun findAll(): List<BlogDto>

    fun findAllWithPagination(pageable: Pageable): Page<BlogDto>

    fun findByIdOrThrow(id: Long): Blog

    fun save(title: String, content: String): BlogCreateResult

    fun update(title: String, content: String, blog_id: Long): BlogUpdateResult

    fun like(blog_id: Long): BlogLikeResult

    fun delete(blog_id: Long): BlogDeleteResult

    fun getBookmarksForLoggedInUser(): List<BlogDto>

    fun createBookmarkForLoggedInUser(blogId: Long): BookmarkResult

    //fun deleteBookmarkForUser(userId: Long, blogId: Long): BookmarkResult

    fun getBlogsByTag(tag: Tag): List<BlogDto>

    fun getBlogsByLoggedInUser(): List<BlogDto>

    fun getBlogsByUser(username: String): List<BlogDto>
}