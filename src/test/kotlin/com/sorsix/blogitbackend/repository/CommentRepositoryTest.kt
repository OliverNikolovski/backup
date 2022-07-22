package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.Comment
import org.junit.jupiter.api.AfterEach
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
        var affectedRecords = commentRepository.upvote(savedComment.id)
        var upvotedComment = commentRepository.findByIdOrNull(savedComment.id)
        assertEquals(1, affectedRecords)
        assertEquals(3, upvotedComment!!.numberOfLikes)
        affectedRecords = commentRepository.upvote(upvotedComment.id)
        upvotedComment = commentRepository.findByIdOrNull(upvotedComment.id)
        assertEquals(1, affectedRecords)
        assertEquals(4, upvotedComment!!.numberOfLikes)
        commentRepository.upvote(upvotedComment.id)
        commentRepository.upvote(upvotedComment.id)
        upvotedComment = commentRepository.findByIdOrNull(upvotedComment.id)
        assertEquals(6, upvotedComment!!.numberOfLikes)
    }
}