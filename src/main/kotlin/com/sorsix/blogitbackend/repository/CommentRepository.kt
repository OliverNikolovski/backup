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
    fun upvote(id: Long): Int
}