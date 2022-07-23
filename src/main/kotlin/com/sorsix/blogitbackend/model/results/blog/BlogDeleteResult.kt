package com.sorsix.blogitbackend.model.results.blog

import com.sorsix.blogitbackend.model.Blog
import com.sorsix.blogitbackend.model.dto.BlogDto

sealed interface BlogDeleteResult : BlogResult

data class BlogDeleted(val blogDto: BlogDto) : BlogDeleteResult {
    override fun success() = true
}

data class BlogDeleteError(val message: String) : BlogDeleteResult {
    override fun success() = false
}

data class BlogDeletePermissionDenied(val message: String) : BlogDeleteResult {
    override fun success() = false
}