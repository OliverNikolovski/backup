package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.Blog
import com.sorsix.blogitbackend.model.enumeration.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

@Repository
interface BlogRepository: JpaRepository<Blog, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Blog b set b.title = :title, b.content = :content where b.id = :id")
    fun update(id: Long, title: String, content: String): Int

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Blog b set b.numberOfLikes = b.numberOfLikes + 1 where b.id = :blog_id")
    fun like(blog_id: Long): Int

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Blog b set b.numberOfLikes = b.numberOfLikes - 1 where b.id = :blog_id")
    fun unlike(blog_id: Long): Int

    @Query(value = "select exists(select 1 from likes_blog where blog_id = ?1 and user_id = ?2)", nativeQuery = true)
    fun isBlogLikedByUser(blog_id: Long, user_id: Long): Boolean

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("insert into likes_blog values(?2, ?1)", nativeQuery = true)
    fun markBlogAsLikedByUser(blog_id: Long, user_id: Long)

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from likes_blog where blog_id = ?1 and user_id = ?2", nativeQuery = true)
    fun unmarkBlogAsLikedByUser(blog_id: Long, user_id: Long)

    @Query(value = "select b.blog_id from bookmarks b where b.user_id = ?1", nativeQuery = true)
    fun getBookmarksForUser(user_id: Long): List<Long>

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        value = """
        insert into bookmarks(user_id, blog_id, date_created) 
        values(?1, ?2, ?3)
        """, nativeQuery = true
    )
    fun createBookmarkForUser(user_id: Long, blog_id: Long, date_created: ZonedDateTime)

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        value="""
        delete from bookmarks
        where user_id = ?1 and blog_id = ?2
        """, nativeQuery = true
    )
    fun deleteBookmarkForUser(user_id: Long, blog_id: Long): Int

    @Query(value = "select exists(select 1 from bookmarks where user_id = ?1 and blog_id = ?2)", nativeQuery = true)
    fun isBlogBookmarkedByUser(userId: Long, blogId: Long): Boolean

    @Query("select blog_id from tags where tag = ?1", nativeQuery = true)
    fun findBlogsByTags(tag: String): List<Long>

    @Query("select id from blog where user_id = ?1", nativeQuery = true)
    fun findBlogsByUser(userId: Long): List<Long>

    @Query("select b.id from blog b join users u on b.user_id = u.id where u.username = ?1", nativeQuery = true)
    fun findBlogsByUserWithUsername(username: String): List<Long>

    @Query("select tag from tags where blog_id = ?1", nativeQuery = true)
    fun getTagsForBlog(blogId: Long): List<Tag>

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("insert into tags values(?1, ?2)", nativeQuery = true)
    fun saveTagsForBlog(blog_id: Long, tag: Tag)
}