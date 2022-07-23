package com.sorsix.blogitbackend.model.exception

class CommentNotFoundException(val error: String) : RuntimeException(error) {
}