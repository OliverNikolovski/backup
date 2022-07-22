package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername1(username: String): User?

    fun existsByUsername1(username: String): Boolean

    @Query(value = "select b.blog_id from bookmarks b where b.user_id = ?1", nativeQuery = true)
    fun getBookmarks(user_id: Long): List<Long>

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        value = """
        insert into bookmarks(user_id, blog_id, date_created) 
        values(?1, ?2, ?3)
        """, nativeQuery = true
    )
    fun createBookmarks(user_id: Long, blog_id: Long, date_created: ZonedDateTime): Int

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        value = """
        insert into follows(follower_id, followed_id) 
        values(?1, ?2)
        """, nativeQuery = true
    )
    fun followSomeone(follower_id: Long, followed_id: Long): Int

    @Query(value = "select f.followed_id from follows f where f.follower_id = ?1", nativeQuery = true)
    fun userIsFollowing(follower_id: Long): List<Long>

    @Query(value = "select f.follower_id from follows f where f.followed_id = ?1", nativeQuery = true)
    fun userIsFollowedBy(followed_id: Long): List<Long>
}