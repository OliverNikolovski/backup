package com.sorsix.blogitbackend.model

import com.sorsix.blogitbackend.model.keys.BlogLikeKey
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "likes_blog")
data class BlogLike(
    @EmbeddedId
    val key: BlogLikeKey
)
