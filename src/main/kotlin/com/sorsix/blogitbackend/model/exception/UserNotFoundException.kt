package com.sorsix.blogitbackend.model.exception

class UserNotFoundException(val error: String) : RuntimeException(error) {
}