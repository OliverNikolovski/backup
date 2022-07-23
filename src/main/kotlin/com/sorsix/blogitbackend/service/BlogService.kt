package com.sorsix.blogitbackend.service

import com.sorsix.blogitbackend.model.Blog
import com.sorsix.blogitbackend.model.enumeration.Tag
import com.sorsix.blogitbackend.model.results.blog.BlogCreateResult
import com.sorsix.blogitbackend.model.results.blog.BlogDeleteResult
import com.sorsix.blogitbackend.model.results.blog.BlogLikeResult
import com.sorsix.blogitbackend.model.results.blog.BlogUpdateResult
import com.sorsix.blogitbackend.model.results.bookmark.BookmarkResult
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.ZonedDateTime

interface BlogService {

    fun findAll(): List<Blog>

    fun findAllWithPagination(pageable: Pageable): Page<Blog>

    fun findByIdOrThrow(id: Long): Blog

    fun save(title: String, content: String, user_id: Long): BlogCreateResult

    fun update(title: String, content: String, blog_id: Long, user_id: Long): BlogUpdateResult

    fun like(user_id: Long, blog_id: Long): BlogLikeResult

    fun delete(blog_id: Long, user_id: Long): BlogDeleteResult

    fun getBookmarksForUser(userId: Long): List<Blog>

    fun createBookmarkForUser(userId: Long, blogId: Long): BookmarkResult

    //fun deleteBookmarkForUser(userId: Long, blogId: Long): BookmarkResult

    fun getBlogsByTag(tag: Tag): List<Blog>

    fun getBlogsByUser(userId: Long): List<Blog>
}