package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: JpaRepository<Comment, Long> {

    @Modifying
    @Query("update Comment c set c.numberOfLikes = :number_of_likes where c.id = :id")
    fun like(id: Long, number_of_likes: Int): Int
}