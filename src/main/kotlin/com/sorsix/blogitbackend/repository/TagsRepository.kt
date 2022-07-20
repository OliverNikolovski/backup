package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.Tags
import com.sorsix.blogitbackend.model.keys.TagKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagsRepository : JpaRepository<Tags, TagKey> {
}