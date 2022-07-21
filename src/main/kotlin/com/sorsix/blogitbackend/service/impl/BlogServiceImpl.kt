package com.sorsix.blogitbackend.service.impl

import com.sorsix.blogitbackend.model.Blog
import com.sorsix.blogitbackend.model.results.blog.*
import com.sorsix.blogitbackend.repository.BlogRepository
import com.sorsix.blogitbackend.repository.UserRepository
import com.sorsix.blogitbackend.service.BlogService
import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.awt.print.Pageable
import java.time.ZonedDateTime

@Service
class BlogServiceImpl(
    private val blogRepository: BlogRepository,
    private val userRepository: UserRepository
) : BlogService {
    override fun findAll(): List<Blog> {
        return blogRepository.findAll()
    }

    override fun findAllWithPagination(pageable: Pageable): Page<Blog> {
        TODO("Not yet implemented")
    }

    override fun save(title: String, content: String, user_id: Long): BlogCreateResult {

        return userRepository.findByIdOrNull(user_id)?.let {
            val blog = Blog(
                id = 0, title = title, content = content, dateCreated = ZonedDateTime.now(),
                numberOfLikes = 0, estimatedReadTime = 0, user_id = it.id
            )
            try {
                BlogCreated(blogRepository.save(blog))
            } catch (ex: Exception) {
                BlogCreateError("Error creating blog")
            }
        } ?: UserNotExisting("Blog can't be created because user does not exist.")
    }

    override fun update(title: String, content: String, blog_id: Long, user_id: Long): BlogUpdateResult {
        val blog = blogRepository.findByIdOrNull(blog_id) ?: return BlogNotExisting("Blog does not exist")
        val user = userRepository.findByIdOrNull(user_id) ?: return UserNotExisting("User does not exist")
        if (blog.user_id != user.id)
            return BlogNotOwnedBySpecifiedUser("Permission denied to edit the blog")
        return if (blogRepository.update(id = blog_id, title = title, content = content) > 0)
            BlogUpdated(blogRepository.findById(blog_id).get())
        else BlogUpdateError("Blog update error.")
    }

    override fun like(user_id: Long, blog_id: Long): BlogLikeResult {
        return BlogAlreadyLiked("")
    }

    override fun delete(blog_id: Long, user_id: Long) {
        val blog = blogRepository.findByIdOrNull(blog_id) ?: return
        val user = userRepository.findByIdOrNull(user_id) ?: return
        if (blog.user_id != user.id)
            return
        blogRepository.delete(blog)
    }
}