package com.sorsix.blogitbackend.model

import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "blog")
data class Blog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    val title: String,

    val content: String,

    @Column(name = "date_created")
    val dateCreated: ZonedDateTime,

    @Column(name = "number_of_likes")
    val numberOfLikes: Int,

    @Column(name = "estimated_read_time")
    val estimatedReadTime: Int,

    val picture: ByteArray? = null,

    val user_id: Long
)

