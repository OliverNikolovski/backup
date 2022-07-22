package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.Blog
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull

import java.time.ZonedDateTime


//@DataJpaTest
class BlogRepositoryTest : AbstractTest() {

    @Autowired
    lateinit var blogRepository: BlogRepository

    @BeforeEach
    fun addUser() {
        jdbcTempate.execute("insert into users(id, username, password, role) values(1, 'john.doe', 'pass123', 'ROLE_USER')")
    }

    @AfterEach
    fun cleanup() {
        jdbcTempate.execute("truncate table blog cascade ")
    }

    @Test
    fun `test save blog`() {
        val blog = Blog(id = 0, title = "Test title", content = "Test content", dateCreated = ZonedDateTime.now(),
        numberOfLikes = 0, estimatedReadTime = 0, 1)
        blogRepository.save(blog)
        assertEquals(1, blogRepository.findAll().size)
        assertEquals("Test title", blogRepository.findAll()[0].title)
        assertEquals("Test content", blogRepository.findAll()[0].content)
        assertEquals(0, blogRepository.findAll()[0].numberOfLikes)
        assertEquals(0, blogRepository.findAll()[0].estimatedReadTime)
    }

    @Test
    fun `test update blog`() {
        val blog = Blog(id = 0, title = "Test title", content = "Test content", dateCreated = ZonedDateTime.now(),
            numberOfLikes = 0, estimatedReadTime = 0, 1)
        val savedBlog = blogRepository.save(blog)
        val numUpdatedRecords = blogRepository.update(id = savedBlog.id, title = "New title", content = "New content")
        val updatedBlog = blogRepository.findByIdOrNull(blog.id)
        assertEquals(1, numUpdatedRecords)
        assertEquals("New title", updatedBlog?.title)
        assertEquals("New content", updatedBlog?.content)
        assertEquals(0, updatedBlog?.numberOfLikes)
        assertEquals(0, updatedBlog?.estimatedReadTime)
    }

    @Test
    fun `test upvote blog`() {
        val blog = Blog(id = 0, title = "Test title", content = "Test content", dateCreated = ZonedDateTime.now(),
            numberOfLikes = 0, estimatedReadTime = 0, 1)
        val savedBlog = blogRepository.save(blog)
        var affectedRecords = blogRepository.upvote(savedBlog.id)
        var updatedBlog = blogRepository.findByIdOrNull(savedBlog.id)
        assertEquals(1, affectedRecords)
        assertEquals(1, updatedBlog!!.numberOfLikes)
        assertEquals(0, updatedBlog.estimatedReadTime)
        affectedRecords = blogRepository.upvote(updatedBlog.id)
        updatedBlog = blogRepository.findByIdOrNull(updatedBlog.id)
        assertEquals(1, affectedRecords)
        assertEquals(2, updatedBlog!!.numberOfLikes)
        assertEquals(0, updatedBlog.estimatedReadTime)
        blogRepository.upvote(updatedBlog.id)
        blogRepository.upvote(updatedBlog.id)
        updatedBlog = blogRepository.findByIdOrNull(updatedBlog.id)
        assertEquals(4, updatedBlog!!.numberOfLikes)
    }

    @Test
    fun `test is liked by user`() {
        jdbcTempate.execute("insert into users(id, username, password, role) values(2, 'john.travolta', 'p1', 'ROLE_USER')")
        jdbcTempate.execute("""
            |insert into blog(id, title, content, date_created, estimated_read_time, user_id) 
            |values (1, 'Blog 1 title', 'Blog 1 content', '2016-06-22 19:10:25-07', 5, 1)""".trimMargin())
        jdbcTempate.execute("""
            |insert into blog(id, title, content, date_created, estimated_read_time, user_id) 
            |values (2, 'Blog 2 title', 'Blog 2 content', '2016-06-22 19:10:25-07', 5, 1)""".trimMargin())
        jdbcTempate.execute("insert into likes_blog values (1, 1)")
        jdbcTempate.execute("insert into likes_blog values (2, 2)")
        assertTrue(blogRepository.isLikedByUser(1, 1))
        assertTrue(blogRepository.isLikedByUser(2, 2))
        assertFalse(blogRepository.isLikedByUser(1, 2))
        assertFalse(blogRepository.isLikedByUser(2, 1))
    }

    @Test
    fun `test mark blog as liked by user`() {
        jdbcTempate.execute("insert into users(id, username, password, role) values(2, 'john.travolta', 'p1', 'ROLE_USER')")
        jdbcTempate.execute("""
            |insert into blog(id, title, content, date_created, estimated_read_time, user_id) 
            |values (1, 'Blog 1 title', 'Blog 1 content', '2016-06-22 19:10:25-07', 5, 1)""".trimMargin())
        jdbcTempate.execute("""
            |insert into blog(id, title, content, date_created, estimated_read_time, user_id) 
            |values (2, 'Blog 2 title', 'Blog 2 content', '2016-06-22 19:10:25-07', 5, 1)""".trimMargin())
        blogRepository.markBlogAsLikedByUser(1, 1)
        assertTrue(blogRepository.isLikedByUser(1, 1))
        assertFalse(blogRepository.isLikedByUser(1, 2))
    }

}