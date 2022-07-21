package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.BlogLike
import com.sorsix.blogitbackend.model.keys.BlogLikeKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BlogLikeRepository : JpaRepository<BlogLike, BlogLikeKey> {
}