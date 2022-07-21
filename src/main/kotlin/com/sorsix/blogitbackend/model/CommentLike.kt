package com.sorsix.blogitbackend.model

import com.sorsix.blogitbackend.model.keys.CommentLikeKey
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table


@Entity
@Table(name = "likes_comment")
data class CommentLike(
    @EmbeddedId
    val Id: CommentLikeKey,
)
