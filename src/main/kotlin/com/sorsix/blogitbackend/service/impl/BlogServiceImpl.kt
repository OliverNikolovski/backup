package com.sorsix.blogitbackend.service.impl

import com.sorsix.blogitbackend.model.Blog
import com.sorsix.blogitbackend.model.dto.BlogDto
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
import org.springframework.security.core.context.SecurityContextHolder
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

    override fun save(title: String, content: String): BlogCreateResult {
        val username: String = SecurityContextHolder.getContext().authentication.principal as String
        val user = userService.findByUsername(username)
        val blog = Blog(
            id = 0, title = title, content = content, dateCreated = ZonedDateTime.now(),
            numberOfLikes = 0, estimatedReadTime = 0, user_id = user?.id
        )
        return try {
            val savedBlog = blogRepository.save(blog)
            val tags: List<Tag> = blogRepository.getTagsForBlog(savedBlog.id)
            BlogCreated(toDto(blog = savedBlog, username = username, tags = tags))
        } catch (ex: Exception) {
            BlogCreateError("Error creating blog")
        }
    }

    @Transactional
    override fun update(title: String, content: String, blog_id: Long): BlogUpdateResult {
        val blog = findByIdOrThrow(blog_id)
        val username: String = SecurityContextHolder.getContext().authentication.principal as String
        val user = userService.findByUsername(username)
        if (blog.user_id != user?.id)
            return BlogUpdatePermissionDenied("Permission denied to edit the blog")
        return if (blogRepository.update(id = blog_id, title = title, content = content) > 0) {
            val tags: List<Tag> = blogRepository.getTagsForBlog(blog.id)
            BlogUpdated(toDto(blog = blogRepository.findById(blog_id).get(), username = username, tags = tags))
        }
        else BlogUpdateError("Blog update error.")
    }

    @Transactional
    override fun like(blog_id: Long): BlogLikeResult {
        val blog = findByIdOrThrow(blog_id)
        val username: String = SecurityContextHolder.getContext().authentication.principal as String
        val user = userService.findByUsername(username)!!
        if (blogRepository.isBlogLikedByUser(blog_id, user.id)) {
            val changedRecords = blogRepository.unlike(blog_id)
            return if (changedRecords > 0) {
                blogRepository.unmarkBlogAsLikedByUser(blog_id, user.id)
                BlogUnliked("Blog successfully unliked.")
            }
            else BlogLikeError("Error unliking blog.")
        }

        val changedRecords = blogRepository.like(blog.id)
        return if (changedRecords > 0) {
            blogRepository.markBlogAsLikedByUser(blog_id, user.id)
            val tags: List<Tag> = blogRepository.getTagsForBlog(blog.id)
            return BlogLiked(toDto(blog = blog, username = username, tags = tags))
        }
        else {
            BlogLikeError("Error liking blog.")
        }
    }

    override fun delete(blog_id: Long): BlogDeleteResult {
        val blog = findByIdOrThrow(blog_id)
        val username: String = SecurityContextHolder.getContext().authentication.principal as String
        val user = userService.findByUsername(username)!!
        val tags: List<Tag> = blogRepository.getTagsForBlog(blog.id)
        if (blog.user_id != user.id) return BlogDeletePermissionDenied("Permission denied.")
        blogRepository.delete(blog)
        blogRepository.findByIdOrNull(blog_id)?.let {
            return BlogDeleteError("Blog delete error.")
        } ?: return BlogDeleted(toDto(blog = blog, username = username, tags = tags))
    }

    override fun getBookmarksForLoggedInUser(): List<Blog> {
        val username: String = SecurityContextHolder.getContext().authentication.principal as String
        val user = userService.findByUsername(username)!!
        val bookmarkIds: List<Long> = blogRepository.getBookmarksForUser(user.id)
        return blogRepository.findAllById(bookmarkIds)
    }

    @Transactional
    override fun createBookmarkForLoggedInUser(blogId: Long): BookmarkResult {
        findByIdOrThrow(blogId)
        val username: String = SecurityContextHolder.getContext().authentication.principal as String
        val user = userService.findByUsername(username)!!
        if (blogRepository.isBlogBookmarkedByUser(user.id, blogId)) {
            return if (blogRepository.deleteBookmarkForUser(user.id, blogId) > 0) BookmarkRemoved("Bookmark removed.")
            else BookmarkError("Bookmark error.")
        }
        blogRepository.createBookmarkForUser(user.id, blogId, ZonedDateTime.now())
        return if (blogRepository.isBlogBookmarkedByUser(user.id, blogId)) BookmarkCreated("Bookmark created.")
        else BookmarkError("Bookmark error.")
    }

//    @Transactional
//    override fun deleteBookmarkForUser(userId: Long, blogId: Long): BookmarkResult {
//        userService.findByIdOrThrow(userId)
//        findByIdOrThrow(blogId)
//        return if (blogRepository.deleteBookmarkForUser(userId, blogId) > 0) BookmarkRemoved("Bookmark removed.")
//        else BookmarkError("Bookmark error.")
//    }

    override fun getBlogsByTag(tag: Tag): List<Blog> {
        val blogIds = blogRepository.findBlogsByTags(tag.name)
        return blogRepository.findAllById(blogIds)
    }

    override fun getBlogsByLoggedInUser(): List<Blog> {
        val username: String = SecurityContextHolder.getContext().authentication.principal as String
        val user = userService.findByUsername(username)!!
        val blogIds = blogRepository.findBlogsByUser(user.id)
        return blogRepository.findAllById(blogIds)
    }

    private fun toDto(blog: Blog, username: String, tags: List<Tag>): BlogDto {
        return BlogDto(
            title = blog.title, content = blog.content, dateCreated = blog.dateCreated,
            estimatedReadTime = blog.estimatedReadTime,
            numberOfLikes = blog.numberOfLikes, username = username,
            tags = tags
        )
    }
}