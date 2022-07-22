package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.Comment
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime


class CommentRepositoryTest : AbstractTest() {

    @Autowired
    lateinit var commentRepository: CommentRepository

    @AfterEach
    fun cleanup() {
        jdbcTempate.execute("truncate table comment cascade ")
    }

    @BeforeEach
    fun addUserAndBlog() {
        jdbcTempate.execute("insert into users(id, username, password, role) values(1, 'john.doe', 'pass123', 'ROLE_USER')")
        jdbcTempate.execute("""
            |insert into blog(id, title, content, date_created, estimated_read_time, user_id) 
            |values (1, 'Blog title', 'Blog content', '2016-06-22 19:10:25-07', 5, 1)""".trimMargin())
    }

    @Test
    fun `test save comment`() {
        val comment = Comment(id = 0, content = "Comment content", dateCreated = ZonedDateTime.now(),
            numberOfLikes = 2, user_id = 1, blog_id = 1)
        val savedComment = commentRepository.save(comment)
        assertEquals("Comment content", savedComment.content)
        assertEquals(2, savedComment.numberOfLikes)
        assertEquals(1, savedComment.user_id)
        assertEquals(1, savedComment.blog_id)
    }

    @Test
    fun `test upvote comment`() {
        val comment = Comment(id = 0, content = "Comment content", dateCreated = ZonedDateTime.now(),
            numberOfLikes = 2, user_id = 1, blog_id = 1)
        val savedComment = commentRepository.save(comment)
        var affectedRecords = commentRepository.like(savedComment.id)
        var upvotedComment = commentRepository.findByIdOrNull(savedComment.id)
        assertEquals(1, affectedRecords)
        assertEquals(3, upvotedComment!!.numberOfLikes)
        affectedRecords = commentRepository.like(upvotedComment.id)
        upvotedComment = commentRepository.findByIdOrNull(upvotedComment.id)
        assertEquals(1, affectedRecords)
        assertEquals(4, upvotedComment!!.numberOfLikes)
        commentRepository.like(upvotedComment.id)
        commentRepository.like(upvotedComment.id)
        upvotedComment = commentRepository.findByIdOrNull(upvotedComment.id)
        assertEquals(6, upvotedComment!!.numberOfLikes)
    }

    @Test
    fun `test mark blog as liked by user`() {
        jdbcTempate.execute("insert into users(id, username, password, role) values(2, 'john.travolta', 'p1', 'ROLE_USER')")
        jdbcTempate.execute("""
            |insert into comment(id, content, date_created, user_id, blog_id) 
            |values (1, 'Title 1 content', '2016-06-22 19:10:25-07', 1, 1)""".trimMargin())
        jdbcTempate.execute("""
            |insert into comment(id, content, date_created, user_id, blog_id) 
            |values (2, 'Title 2 content', '2016-06-22 19:10:25-07', 2, 1)""".trimMargin())
        commentRepository.markCommentAsLikedByUser(1, 1)
        Assertions.assertTrue(commentRepository.isLikedByUser(1, 1))
        Assertions.assertFalse(commentRepository.isLikedByUser(1, 2))
    }
}