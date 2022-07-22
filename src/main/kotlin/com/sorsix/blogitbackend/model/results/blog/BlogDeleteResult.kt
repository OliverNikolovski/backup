package com.sorsix.blogitbackend.model.results.blog

import com.sorsix.blogitbackend.model.Blog

sealed interface BlogDeleteResult : BlogResult

data class BlogDeleted(val blog: Blog) : BlogDeleteResult {
    override fun success() = true
}

data class BlogDeleteError(val message: String) : BlogDeleteResult {
    override fun success() = false
}

data class BlogDeletePermissionDenied(val message: String) : BlogDeleteResult {
    override fun success() = false
}