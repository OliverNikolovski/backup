package com.sorsix.blogitbackend.service.impl

import com.sorsix.blogitbackend.model.Blog
import com.sorsix.blogitbackend.model.BlogLike
import com.sorsix.blogitbackend.model.keys.BlogLikeKey
import com.sorsix.blogitbackend.model.results.blog.*
import com.sorsix.blogitbackend.repository.BlogLikeRepository
import com.sorsix.blogitbackend.repository.BlogRepository
import com.sorsix.blogitbackend.repository.UserRepository
import com.sorsix.blogitbackend.service.BlogService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
class BlogServiceImpl(
    private val blogRepository: BlogRepository,
    private val userRepository: UserRepository,
    private val blogLikeRepository: BlogLikeRepository
) : BlogService {
    override fun findAll(): List<Blog> {
        return blogRepository.findAll()
    }

    override fun findAllWithPagination(pageable: Pageable): Page<Blog> =
        blogRepository.findAll(pageable)

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
        } ?: UserNotExisting("User with id $user_id does not exist")
    }

    override fun update(title: String, content: String, blog_id: Long, user_id: Long): BlogUpdateResult {
        val blog = blogRepository.findByIdOrNull(blog_id) ?: return BlogNotExisting("Blog with id $blog_id does not exist")
        val user = userRepository.findByIdOrNull(user_id) ?: return UserNotExisting("User with id $user_id does not exist")
        if (blog.user_id != user.id)
            return BlogNotOwnedBySpecifiedUser("Permission denied to edit the blog")
        return if (blogRepository.update(id = blog_id, title = title, content = content) > 0)
            BlogUpdated(blogRepository.findById(blog_id).get())
        else BlogUpdateError("Blog update error.")
    }

    @Transactional
    override fun like(user_id: Long, blog_id: Long): BlogLikeResult {
        if(!userRepository.existsById(user_id)) return UserNotExisting("User with id $user_id does not exist")
        val blog = blogRepository.findByIdOrNull(blog_id) ?: return BlogNotExisting("Blog with id $blog_id does not exist")

        if (blogLikeRepository.existsById(BlogLikeKey(user_id, blog_id))) {
            return BlogAlreadyLiked("Blog is already liked")
        }

        val id = BlogLikeKey(user_id, blog_id)

        if (blogLikeRepository.existsById(id)) return BlogAlreadyLiked("Blog is already liked")

        val count = blogRepository.like(blog.id, blog.numberOfLikes + 1)
        return if (count > 0) {
            blogLikeRepository.save(BlogLike(id))
            return BlogLiked(blog)
        }
        else {
            BlogLikeError("Error liking blog.")
        }
    }

    override fun delete(blog_id: Long, user_id: Long) {
        val blog = blogRepository.findByIdOrNull(blog_id) ?: return
        val user = userRepository.findByIdOrNull(user_id) ?: return
        if (blog.user_id != user.id)
            return
        blogRepository.delete(blog)
    }
}