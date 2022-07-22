package com.sorsix.blogitbackend.service.impl

import com.sorsix.blogitbackend.model.Comment
import com.sorsix.blogitbackend.model.CommentLike
import com.sorsix.blogitbackend.model.keys.CommentLikeKey
import com.sorsix.blogitbackend.model.results.comment.*
import com.sorsix.blogitbackend.repository.BlogRepository
import com.sorsix.blogitbackend.repository.CommentLikeRepository
import com.sorsix.blogitbackend.repository.CommentRepository
import com.sorsix.blogitbackend.repository.UserRepository
import com.sorsix.blogitbackend.service.CommentService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
class CommentServiceImpl(
    val commentRepository: CommentRepository,
    val userRepository: UserRepository,
    val blogRepository: BlogRepository,
    val commentLikeRepository: CommentLikeRepository
) : CommentService {

    override fun findAll(): List<Comment> = commentRepository.findAll()

    @Transactional
    override fun like(comment_id: Long, user_id: Long): CommentLikedResult {
        if (!userRepository.existsById(user_id)) return UserNotExisting("User with id $user_id does not exist")

        val comment = commentRepository.findByIdOrNull(comment_id)

        return comment?.let {
            val id = CommentLikeKey(user_id, comment_id)

            if (commentLikeRepository.existsById(id)) return CommentAlreadyLiked("Comment is already liked")

            val isSuccessful = commentRepository.like(comment.id, comment.numberOfLikes + 1)

            if (isSuccessful == 1) {
                commentLikeRepository.save(CommentLike(id))
                return CommentLiked(comment)
            } else {
                CommentNotLiked("Error liking comment")
            }
        } ?: CommentNotExisting("Comment with id $comment_id does not exist")
    }

    override fun save(content: String, user_id: Long, blog_id: Long): CommentSaveResult {

        if (!userRepository.existsById(user_id)) return UserNotExisting("User with id $user_id does not exist")

        if (!blogRepository.existsById(blog_id)) return BlogNotExisting("Blog with id $blog_id does not exist")

        val c = Comment(
            id = 0,
            content = content,
            dateCreated = ZonedDateTime.now(),
            numberOfLikes = 0,
            user_id = user_id,
            blog_id = blog_id
        )

        return try {
            CommentCreated(commentRepository.save(c))
        } catch (ex: Exception) {
            CommentCreateError("Error creating comment")
        }

    }

    @Transactional
    override fun update(user_id: Long, comment_id: Long, content: String): CommentUpdateResult {

        val comment = commentRepository.findByIdOrNull(comment_id)
            ?: return CommentNotExisting("Comment with id $comment_id does not exist")
        val user =
            userRepository.findByIdOrNull(user_id) ?: return UserNotExisting("User with id $user_id does not exist")

        if (user.id != comment.user_id) return UsersNotMatch("Permission denied to edit the comment")

        commentRepository.deleteById(comment_id)

        val c = Comment(
            id = comment.id,
            content = comment.content,
            dateCreated = comment.dateCreated,
            numberOfLikes = comment.numberOfLikes,
            user_id = comment.user_id,
            blog_id = comment.blog_id
        )

        return try {
            CommentCreated(commentRepository.save(c))
        } catch (ex: Exception) {
            CommentCreateError("Error updating comment")
        }

    }

    override fun delete(comment_id: Long) {
        commentRepository.deleteById(comment_id)
    }
}