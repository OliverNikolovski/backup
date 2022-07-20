package com.sorsix.blogitbackend.model

import com.sorsix.blogitbackend.model.keys.TagKey
import javax.persistence.*

@Entity
@Table(name = "tags")
data class Tags(
    @EmbeddedId
    val Id: TagKey,
)

