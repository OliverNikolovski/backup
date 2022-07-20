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

    @Column(name = "datecreated")
    val dateCreated: ZonedDateTime,

    @Column(name = "numberoflikes")
    val numberOfLikes: Int,

    val user_id: Long,

    val blog_id: Long
)
