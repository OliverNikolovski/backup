package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): User?

    fun existsByUsername(username: String): Boolean

    @Query(value = "select b.blog_id from bookmarks b where b.user_id = ?1", nativeQuery = true)
    fun getBookmarks(user_id: Long): List<Long>

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        value = """
        insert into bookmarks(user_id, blog_id, date_created) 
        values(?1, ?2, ?3)
        """, nativeQuery = true
    )
    fun createBookmark(user_id: Long, blog_id: Long, date_created: ZonedDateTime)
}