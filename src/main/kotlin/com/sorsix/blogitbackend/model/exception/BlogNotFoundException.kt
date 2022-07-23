package com.sorsix.blogitbackend.model.exception

class BlogNotFoundException(val error: String) : RuntimeException(error) {
}