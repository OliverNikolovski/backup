package com.sorsix.blogitbackend.model.keys

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class CommentLikeKey(

    @Column(name = "user_id", nullable = false)
    val user_id: Long,

    @Column(name = "comment_id", nullable = false)
    val comment_id: Long
) : Serializable
