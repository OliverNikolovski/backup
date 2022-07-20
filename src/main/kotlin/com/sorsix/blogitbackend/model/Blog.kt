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

    @Column(name = "datecreated")
    val dateCreated: ZonedDateTime,

    @Column(name = "numberoflikes")
    val numberOfLikes: Int,

    @Column(name = "estimatedreadtime")
    val estimatedReadTime: Int,

    val user_id: Long
)

