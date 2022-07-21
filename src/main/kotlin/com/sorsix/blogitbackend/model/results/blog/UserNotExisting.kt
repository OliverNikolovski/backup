package com.sorsix.blogitbackend.model.results.blog

data class UserNotExisting(val message: String) :
    BlogCreateResult, BlogUpdateResult, BlogLikeResult {
    override fun success(): Boolean {
        return false
    }
}