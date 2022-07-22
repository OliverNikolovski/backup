package com.sorsix.blogitbackend.service.impl

import com.sorsix.blogitbackend.model.Comment
import com.sorsix.blogitbackend.model.results.comment.*
import com.sorsix.blogitbackend.model.results.comment.BlogNotExisting
import com.sorsix.blogitbackend.model.results.comment.UserNotExisting
import com.sorsix.blogitbackend.repository.BlogRepository
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
) : CommentService {

    override fun findAll(): List<Comment> = commentRepository.findAll()

    @Transactional
    override fun like(comment_id: Long, user_id: Long): CommentLikedResult {
        if (!userRepository.existsById(user_id)) return UserNotExisting("User with id $user_id does not exist")

        val comment = commentRepository.findByIdOrNull(comment_id)

        return comment?.let {
            if (commentRepository.isLikedByUser(comment_id, user_id)) {
                val changedRecords = commentRepository.unlike(comment_id)
                return if (changedRecords > 0) CommentUnliked("Comment successfully unliked.")
                else CommentLikeError("Error liking comment.")
            }

            val isSuccessful = commentRepository.like(comment.id)

            if (isSuccessful == 1) {
                commentRepository.markCommentAsLikedByUser(comment_id, user_id)
                return CommentLiked(comment)
            } else {
                CommentLikeError("Error upvoting comment")
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

    override fun delete(comment_id: Long, user_id: Long): CommentDeleteResult {
        val comment = commentRepository.findByIdOrNull(comment_id) ?: return CommentNotExisting("Comment does not exist.")
        val user = userRepository.findByIdOrNull(user_id) ?: return UserNotExisting("User does not exist.")
        if (comment.user_id != user.id) return CommentDeletePermissionDenied("Permission denied.")
        commentRepository.delete(comment)
        commentRepository.findByIdOrNull(comment.id)?.let {
            return CommentDeleteError("Comment delete error.")
        } ?: return CommentDeleted(comment)
    }
}