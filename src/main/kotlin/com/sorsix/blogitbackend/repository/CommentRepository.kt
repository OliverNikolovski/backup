package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: JpaRepository<Comment, Long> {
}