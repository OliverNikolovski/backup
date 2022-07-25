package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): User?

    fun existsByUsername(username: String): Boolean

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("insert into follows values(?1, ?2)", nativeQuery = true)
    fun follow(followerId: Long, followedId: Long): Int

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from follows where follower_id = ?1 and followed_id = ?2", nativeQuery = true)
    fun unfollow(followerId: Long, followedId: Long)

    @Query(
        value = "select exists(select 1 from follows where follower_id = ?1 and followed_id = ?2)",
        nativeQuery = true
    )
    fun followExists(followerId: Long, followedId: Long): Boolean

    @Query("select follower_id from follows where followed_id = ?1", nativeQuery = true)
    fun getFollowersForUser(userId: Long): List<Long>

    @Query("select followed_id from follows where follower_id = ?1", nativeQuery = true)
    fun getFollowingForUser(userId: Long): List<Long>

}