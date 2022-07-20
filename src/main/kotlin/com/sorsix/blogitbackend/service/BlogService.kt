package com.sorsix.blogitbackend.service

import com.sorsix.blogitbackend.model.Blog
import com.sorsix.blogitbackend.model.results.blog.BlogCreateResult
import com.sorsix.blogitbackend.model.results.blog.BlogResult
import com.sorsix.blogitbackend.model.results.blog.BlogUpdateResult
import org.springframework.data.domain.Page
import java.awt.print.Pageable

interface BlogService {

    fun findAll(): List<Blog>

    fun findAllWithPagination(pageable: Pageable): Page<Blog>

    fun save(title: String, content: String, user_id: Long): BlogCreateResult

    fun update(title: String, content: String, blog_id: Long, user_id: Long): BlogUpdateResult

    fun delete(blog_id: Long, user_id: Long)

}