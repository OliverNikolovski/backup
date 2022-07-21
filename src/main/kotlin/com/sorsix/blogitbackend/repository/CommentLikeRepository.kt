package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.CommentLike
import com.sorsix.blogitbackend.model.keys.CommentLikeKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentLikeRepository : JpaRepository<CommentLike, CommentLikeKey> {
}