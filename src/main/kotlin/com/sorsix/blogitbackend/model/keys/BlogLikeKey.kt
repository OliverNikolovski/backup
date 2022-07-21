package com.sorsix.blogitbackend.model.keys

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class BlogLikeKey(
    @Column(name = "user_id")
    val userId: Long,
    @Column(name = "blog_id")
    val blogId: Long
) : Serializable
