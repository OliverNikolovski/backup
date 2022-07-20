package com.sorsix.blogitbackend.model.keys

import com.sorsix.blogitbackend.model.enumeration.Tag

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
data class TagKey (
    @Column(name = "blog_id", nullable = false)
    val blog_id: Long,

    @Column(name = "tag", nullable = false)
    @Enumerated(value = EnumType.STRING)
    val tag: Tag
) : Serializable
