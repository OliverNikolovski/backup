package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.Blog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BlogRepository: JpaRepository<Blog, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Blog b set b.title = :title, b.content = :content where b.id = :id")
    fun update(id: Long, title: String, content: String): Int

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Blog b set b.numberOfLikes = b.numberOfLikes + 1 where b.id = :blog_id")
    fun upvote(blog_id: Long): Int

    @Query(value = "select exists(select 1 from likes_blog where blog_id = ?1 and user_id = ?2)", nativeQuery = true)
    fun isLikedByUser(blog_id: Long, user_id: Long): Boolean

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("insert into likes_blog values(?2, ?1)", nativeQuery = true)
    fun markBlogAsLikedByUser(blog_id: Long, user_id: Long)

}