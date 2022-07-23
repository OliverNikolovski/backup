package com.sorsix.blogitbackend.service.impl

import com.sorsix.blogitbackend.model.Blog
import com.sorsix.blogitbackend.model.enumeration.Tag
import com.sorsix.blogitbackend.model.exception.BlogNotFoundException
import com.sorsix.blogitbackend.model.results.blog.*
import com.sorsix.blogitbackend.model.results.bookmark.*
import com.sorsix.blogitbackend.repository.BlogRepository
import com.sorsix.blogitbackend.service.BlogService
import com.sorsix.blogitbackend.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
class BlogServiceImpl(
    private val blogRepository: BlogRepository,
    private val userService: UserService,
) : BlogService {

    override fun findAll(): List<Blog> {
        return blogRepository.findAll()
    }

    override fun findAllWithPagination(pageable: Pageable): Page<Blog> =
        blogRepository.findAll(pageable)

    override fun findByIdOrThrow(id: Long): Blog {
        return blogRepository.findByIdOrNull(id) ?: throw BlogNotFoundException("Blog with id: $id does not exist")
    }

    override fun save(title: String, content: String, user_id: Long): BlogCreateResult {
        userService.findByIdOrThrow(user_id)
        val blog = Blog(
            id = 0, title = title, content = content, dateCreated = ZonedDateTime.now(),
            numberOfLikes = 0, estimatedReadTime = 0, user_id = user_id
        )
        return try {
            BlogCreated(blogRepository.save(blog))
        } catch (ex: Exception) {
            BlogCreateError("Error creating blog")
        }
    }

    @Transactional
    override fun update(title: String, content: String, blog_id: Long, user_id: Long): BlogUpdateResult {
        val blog = findByIdOrThrow(blog_id)
        val user = userService.findByIdOrThrow(user_id)
        if (blog.user_id != user.id)
            return BlogNotOwnedBySpecifiedUser("Permission denied to edit the blog")
        return if (blogRepository.update(id = blog_id, title = title, content = content) > 0)
            BlogUpdated(blogRepository.findById(blog_id).get())
        else BlogUpdateError("Blog update error.")
    }

    @Transactional
    override fun like(user_id: Long, blog_id: Long): BlogLikeResult {
        userService.findByIdOrThrow(user_id)
        val blog = findByIdOrThrow(blog_id)
        if (blogRepository.isBlogLikedByUser(blog_id, user_id)) {
            val changedRecords = blogRepository.unlike(blog_id)
            return if (changedRecords > 0) BlogUnliked("Blog successfully unliked.")
            else BlogLikeError("Error unliking blog.")
        }

        val changedRecords = blogRepository.like(blog.id)
        return if (changedRecords > 0) {
            blogRepository.markBlogAsLikedByUser(blog_id, user_id)
            return BlogLiked(blog)
        }
        else {
            BlogLikeError("Error liking blog.")
        }
    }

    override fun delete(blog_id: Long, user_id: Long): BlogDeleteResult {
        val blog = findByIdOrThrow(blog_id)
        val user = userService.findByIdOrThrow(user_id)
        if (blog.user_id != user.id) return BlogDeletePermissionDenied("Permission denied.")
        blogRepository.delete(blog)
        blogRepository.findByIdOrNull(blog_id)?.let {
            return BlogDeleteError("Blog delete error.")
        } ?: return BlogDeleted(blog)
    }

    override fun getBookmarksForUser(userId: Long): List<Blog> {
        val bookmarkIds: List<Long> = blogRepository.getBookmarksForUser(userId)
        return blogRepository.findAllById(bookmarkIds)
    }

    @Transactional
    override fun createBookmarkForUser(userId: Long, blogId: Long, dateCreated: ZonedDateTime): BookmarkResult {
        userService.findByIdOrThrow(userId)
        findByIdOrThrow(blogId)
        if (blogRepository.isBlogBookmarkedByUser(userId, blogId)) {
            return if (blogRepository.deleteBookmarkForUser(userId, blogId) > 0) BookmarkRemoved("Bookmark removed.")
            else BookmarkError("Bookmark error.")
        }
        blogRepository.createBookmarkForUser(userId, blogId, ZonedDateTime.now())
        return if (blogRepository.isBlogBookmarkedByUser(userId, blogId)) BookmarkCreated("Bookmark created.")
        else BookmarkError("Bookmark error.")
    }

    @Transactional
    override fun deleteBookmarkForUser(userId: Long, blogId: Long): BookmarkResult {
        userService.findByIdOrThrow(userId)
        findByIdOrThrow(blogId)
        return if (blogRepository.deleteBookmarkForUser(userId, blogId) > 0) BookmarkRemoved("Bookmark removed.")
        else BookmarkError("Bookmark error.")
    }

    override fun getBlogsByTag(tag: Tag): List<Blog> {
        val blogIds = blogRepository.findBlogsByTags(tag.name)
        return blogRepository.findAllById(blogIds)
    }

    override fun getBlogsByUser(userId: Long): List<Blog> {
        val blogIds = blogRepository.findBlogsByUser(userId)
        return blogRepository.findAllById(blogIds)
    }
}