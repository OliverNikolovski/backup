package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.Blog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BlogRepository: JpaRepository<Blog, Long> {
}