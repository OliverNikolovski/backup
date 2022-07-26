package com.sorsix.blogitbackend.api.controller

import com.sorsix.blogitbackend.api.responseobjects.UserRegisterResponse
import com.sorsix.blogitbackend.model.dto.BlogDto
import com.sorsix.blogitbackend.model.dto.UserDto
import com.sorsix.blogitbackend.model.results.user.*
import com.sorsix.blogitbackend.service.BlogService
import com.sorsix.blogitbackend.service.UserService
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.InputStream
import java.net.URLConnection

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = ["http://localhost:4200"])
class UserController(val blogService: BlogService,
                    val userService: UserService) {

    @GetMapping("/{username}")
    fun getUserByUsername(@PathVariable username: String): ResponseEntity<UserDto?> {
        return this.userService.getUserDtoByUsername(username)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/{username}/blogs")
    fun getBlogsByUser(@PathVariable username: String): ResponseEntity<List<BlogDto>> =
        ResponseEntity.ok(blogService.getBlogsByUser(username))


    @GetMapping("/{username}/picture")
    fun getUserProfilePicture(@PathVariable username: String): ResponseEntity<ByteArray> {
        return this.userService.getUserDtoByUsername(username)?.let {
            ResponseEntity.ok(it.profilePicture)
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/blog/{blogId}")
    fun getBlogPoster(@PathVariable blogId: Long): ResponseEntity<UserDto> {
        return this.userService.getBlogPoster(blogId)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping("/register")
    fun register(@RequestParam username: String, @RequestParam password: String,
                 @RequestParam repeatedPassword: String, @RequestParam(required = false) email: String?,
                 @RequestParam(required = false) shortBio: String?,
                 @RequestParam(required = false) profilePicture: MultipartFile?): ResponseEntity<UserRegisterResponse> {
        val result = this.userService.register(
            username = username,
            password = password,
            repeatedPassword = repeatedPassword,
            email = email,
            shortBio = shortBio,
            profilePicture = profilePicture?.bytes,
            profilePictureFormat = profilePicture?.contentType
        )
        return when(result) {
            is UserRegistered -> ResponseEntity.ok(UserRegisterResponse(result.user, "success"))
            is InvalidUsername -> ResponseEntity.badRequest().body(UserRegisterResponse(null, result.message))
            is InvalidPassword -> ResponseEntity.badRequest().body(UserRegisterResponse(null, result.message))
            is PasswordsDoNotMatch -> ResponseEntity.badRequest().body(UserRegisterResponse(null, result.message))
            is UsernameTaken -> ResponseEntity.status(CONFLICT).body(UserRegisterResponse(null, result.message))
            is UsernameRegistrationError -> ResponseEntity.internalServerError().build()
        }
    }

}