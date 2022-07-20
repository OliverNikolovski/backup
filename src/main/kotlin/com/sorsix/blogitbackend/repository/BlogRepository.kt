package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.Blog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BlogRepository: JpaRepository<Blog, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Blog b set b.title = :title, b.content = :content where b.id = :id")
    fun update(id: Long, title: String, content: String): Int

}