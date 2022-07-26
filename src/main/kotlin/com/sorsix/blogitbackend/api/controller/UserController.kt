package com.sorsix.blogitbackend.api.controller

import com.sorsix.blogitbackend.model.Blog
import com.sorsix.blogitbackend.model.dto.BlogDto
import com.sorsix.blogitbackend.service.BlogService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = ["http://localhost:4200"])
class UserController(val blogService: BlogService) {

    @GetMapping("/blogs")
    fun getBlogsByUser(): ResponseEntity<List<BlogDto>> =
        ResponseEntity.ok(blogService.getBlogsByLoggedInUser())

}