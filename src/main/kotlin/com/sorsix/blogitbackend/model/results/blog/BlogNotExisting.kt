package com.sorsix.blogitbackend.model.results.blog

data class BlogNotExisting(val message: String) : BlogCreateResult, BlogUpdateResult {
    override fun success(): Boolean {
        return false
    }
}
