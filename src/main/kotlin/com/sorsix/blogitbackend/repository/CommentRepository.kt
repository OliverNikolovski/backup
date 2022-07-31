package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Comment c set c.numberOfLikes = c.numberOfLikes + 1 where c.id = :id")
    fun like(id: Long): Int

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Comment c set c.numberOfLikes = c.numberOfLikes - 1 where c.id = :comment_id")
    fun unlike(comment_id: Long): Int

    @Query(value = "select exists(select 1 from likes_comment where comment_id = ?1 and user_id = ?2)", nativeQuery = true)
    fun isLikedByUser(comment_id: Long, user_id: Long): Boolean

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("insert into likes_comment values(?2, ?1)", nativeQuery = true)
    fun markCommentAsLikedByUser(comment_id: Long, user_id: Long)

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from likes_comment where comment_id = ?1 and user_id = ?2", nativeQuery = true)
    fun unmarkCommentAsLikedByUser(comment_id: Long, user_id: Long)

    @Query("select c.id from Comment c where c.blog_id = :blogId")
    fun getCommentsForBlog(blogId: Long): List<Long>
}