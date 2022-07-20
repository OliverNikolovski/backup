package com.sorsix.blogitbackend.model

import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "comment")
data class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    val content: String,

    @Column(name = "date_created")
    val dateCreated: ZonedDateTime,

    @Column(name = "number_of_likes")
    val numberOfLikes: Int,

    val user_id: Long,

    val blog_id: Long
)
