package com.sorsix.blogitbackend.api.controller

import com.sorsix.blogitbackend.model.dto.BlogDto
import com.sorsix.blogitbackend.service.BlogService
import com.sorsix.blogitbackend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = ["http://localhost:4200"])
class UserController(val blogService: BlogService,
                    val userService: UserService) {

    @GetMapping("/blogs")
    fun getBlogsByUser(): ResponseEntity<List<BlogDto>> =
        ResponseEntity.ok(blogService.getBlogsByLoggedInUser())

    @PostMapping("/register")
    fun register(@RequestParam username: String, @RequestParam password: String,
                 @RequestParam repeatedPassword: String, @RequestParam(required = false) email: String?,
                @RequestParam(required = false) shortBio: String?, @RequestParam(required = false) profilePicture: MultipartFile?) {
        this.userService.register(
            username = username,
            password = password,
            repeatedPassword = repeatedPassword,
            email = email,
            shortBio = shortBio,
            profilePicture = profilePicture?.bytes
        )
    }

}