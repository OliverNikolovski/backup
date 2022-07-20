package com.sorsix.blogitbackend.service.impl

import com.sorsix.blogitbackend.model.Comment
import com.sorsix.blogitbackend.model.results.*
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
    val blogRepository: BlogRepository
) : CommentService {

    override fun findAll(): List<Comment> = commentRepository.findAll()

    override fun like(comment_id: Long): CommentResult {

        val comment = commentRepository.findByIdOrNull(comment_id)

        return comment?.let {

            val isSuccessful = commentRepository.like(comment.id, comment.numberOfLikes)

            if(isSuccessful == 1) {
              return CommentLiked(
                  Comment(
                      id = comment.id,
                      content = comment.content,
                      dateCreated = comment.dateCreated,
                      numberOfLikes = comment.numberOfLikes + 1,
                      user_id = comment.user_id,
                      blog_id = comment.blog_id
                  )
              )
            } else {
                CommentNotLiked("Comment was not liked")
            }

        } ?: CommentNotExisting("Comment with id $comment_id does not exist")
    }

    override fun save(content: String, user_id: Long, blog_id: Long): CommentResult {

        if (!userRepository.existsById(user_id)) return UserNotExisting("User with id $user_id does not exist")

        if (!blogRepository.existsById(blog_id)) return BlogNotExisting("Blog with id $blog_id does not exist")

        return CommentCreated(
            commentRepository.save(
                Comment(
                    id = 0,
                    content = content,
                    dateCreated = ZonedDateTime.now(),
                    numberOfLikes = 0,
                    user_id = user_id,
                    blog_id = blog_id
                )
            )
        )
    }

    @Transactional
    override fun update(user_id: Long, comment_id: Long, content: String): CommentResult {

        val comment = commentRepository.findByIdOrNull(comment_id)
            ?: return CommentNotExisting("Comment with id $comment_id does not exist")
        val user =
            userRepository.findByIdOrNull(user_id) ?: return UserNotExisting("User with id $user_id does not exist")

        if (user.id != comment.user_id) return UsersNotMatch("Permission denied to edit the comment")

        commentRepository.deleteById(comment_id)

        return CommentCreated(
            commentRepository.save(
                Comment(
                    id = comment.id,
                    content = comment.content,
                    dateCreated = comment.dateCreated,
                    numberOfLikes = comment.numberOfLikes,
                    user_id = comment.user_id,
                    blog_id = comment.blog_id
                )
            )
        )
    }

    override fun delete(comment_id: Long) {
        commentRepository.deleteById(comment_id)
    }
}