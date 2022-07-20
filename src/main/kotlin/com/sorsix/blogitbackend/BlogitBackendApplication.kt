package com.sorsix.blogitbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlogitBackendApplication

fun main(args: Array<String>) {
    runApplication<BlogitBackendApplication>(*args)
}
