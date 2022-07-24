package com.sorsix.blogitbackend.api.controller

import com.sorsix.blogitbackend.model.Blog
import com.sorsix.blogitbackend.service.BlogService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(val blogService: BlogService) {

    @GetMapping("/{id}/blogs")
    fun getBlogsByUser(@PathVariable id: Long): ResponseEntity<List<Blog>> =
        ResponseEntity.ok(blogService.getBlogsByUser(id))

}