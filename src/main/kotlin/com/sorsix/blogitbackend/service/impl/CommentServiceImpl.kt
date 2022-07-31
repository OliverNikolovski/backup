package com.sorsix.blogitbackend.service.impl

import com.sorsix.blogitbackend.model.Comment
import com.sorsix.blogitbackend.model.dto.CommentDto
import com.sorsix.blogitbackend.model.results.comment.*
import com.sorsix.blogitbackend.model.results.comment.BlogNotExisting
import com.sorsix.blogitbackend.repository.BlogRepository
import com.sorsix.blogitbackend.repository.CommentRepository
import com.sorsix.blogitbackend.repository.UserRepository
import com.sorsix.blogitbackend.service.CommentService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
class CommentServiceImpl(
    val commentRepository: CommentRepository,
    val userRepository: UserRepository,
    val blogRepository: BlogRepository
) : CommentService {



    override fun findAll(blog_id: Long): List<CommentDto> {
        val commentIds = this.commentRepository.getCommentsForBlog(blog_id)
        val comments = this.commentRepository.findAllById(commentIds)
        return comments.map { comment ->
            val username = userRepository.findByIdOrNull(comment.user_id)!!.username
            toDto(comment, username)
        }
    }

    @Transactional
    override fun like(comment_id: Long): CommentLikedResult {
        val username: String = SecurityContextHolder.getContext().authentication.principal as String
        val user = userRepository.findByUsername(username)

        val comment = commentRepository.findByIdOrNull(comment_id)

        return comment?.let {
            if (commentRepository.isLikedByUser(comment_id, user!!.id)) {
                val changedRecords = commentRepository.unlike(comment_id)
                return if (changedRecords > 0) {
                    commentRepository.unmarkCommentAsLikedByUser(comment_id = comment_id, user_id = user.id)
                    CommentUnliked("Comment successfully unliked.")
                }
                else CommentLikeError("Error liking comment.")
            }

            val isSuccessful = commentRepository.like(comment.id)

            if (isSuccessful == 1) {
                val usernameOfCommenter = userRepository.findByIdOrNull(comment.id)!!.username
                commentRepository.markCommentAsLikedByUser(comment_id, user.id)
                return CommentLiked(toDto(comment, usernameOfCommenter))
            } else {
                CommentLikeError("Error upvoting comment")
            }
        } ?: CommentNotExisting("Comment with id $comment_id does not exist")
    }

    override fun save(content: String, blog_id: Long): CommentSaveResult {

        val username: String = SecurityContextHolder.getContext().authentication.principal as String
        val user = userRepository.findByUsername(username)

        if (!blogRepository.existsById(blog_id)) return BlogNotExisting("Blog with id $blog_id does not exist")

        val c = Comment(
            id = 0,
            content = content,
            dateCreated = ZonedDateTime.now(),
            numberOfLikes = 0,
            user_id = user!!.id,
            blog_id = blog_id
        )

        return try {
            CommentCreated(toDto(commentRepository.save(c), username))
        } catch (ex: Exception) {
            CommentCreateError("Error creating comment")
        }

    }

    @Transactional
    override fun update(comment_id: Long, content: String): CommentUpdateResult {

        val username: String = SecurityContextHolder.getContext().authentication.principal as String
        val user = userRepository.findByUsername(username)

        val comment = commentRepository.findByIdOrNull(comment_id)
            ?: return CommentNotExisting("Comment with id $comment_id does not exist")

        if (user!!.id != comment.user_id) return UsersNotMatch("Permission denied to edit the comment")

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
            CommentCreated(toDto(comment, username))
        } catch (ex: Exception) {
            CommentCreateError("Error updating comment")
        }

    }

    override fun delete(comment_id: Long): CommentDeleteResult {
        val comment =
            commentRepository.findByIdOrNull(comment_id) ?: return CommentNotExisting("Comment does not exist.")

        val username: String = SecurityContextHolder.getContext().authentication.principal as String
        val user = userRepository.findByUsername(username)

        if (comment.user_id != user!!.id) return CommentDeletePermissionDenied("Permission denied.")

        commentRepository.delete(comment)

        commentRepository.findByIdOrNull(comment.id)?.let {
            return CommentDeleteError("Comment delete error.")
        } ?: return CommentDeleted(toDto(comment, username))
    }

    fun toDto(comment: Comment, username: String): CommentDto {
        val usernameOfLoggedInUser: String = SecurityContextHolder.getContext().authentication?.principal as String
        val loggedInUser = this.userRepository.findByUsername(usernameOfLoggedInUser)
        if (loggedInUser != null) {
            val isLiked = this.commentRepository.isLikedByUser(comment_id = comment.id, user_id = loggedInUser.id)
            return CommentDto(
                id = comment.id,
                content = comment.content,
                dateCreated = comment.dateCreated,
                numberOfLikes = comment.numberOfLikes,
                username = username,
                blog_id = comment.blog_id,
                isLikedByCurrentlyLoggedInUser = isLiked
            )
        }
        return CommentDto(
            id = comment.id,
            content = comment.content,
            dateCreated = comment.dateCreated,
            numberOfLikes = comment.numberOfLikes,
            username = username,
            blog_id = comment.blog_id
        )
    }
}