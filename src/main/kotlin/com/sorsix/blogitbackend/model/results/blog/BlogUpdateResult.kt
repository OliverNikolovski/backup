package com.sorsix.blogitbackend.model.results.blog

import com.sorsix.blogitbackend.model.dto.BlogDto

sealed interface BlogUpdateResult : BlogResult {
}

data class BlogUpdated(val blogDto: BlogDto) : BlogUpdateResult {
    override fun success() = true
}

data class BlogUpdateError(val message: String) : BlogUpdateResult {
    override fun success() = false
}

data class BlogUpdatePermissionDenied(val message: String) : BlogUpdateResult {
    override fun success() = false

}