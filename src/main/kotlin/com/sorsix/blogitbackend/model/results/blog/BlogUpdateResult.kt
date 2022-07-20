package com.sorsix.blogitbackend.model.results.blog

import com.sorsix.blogitbackend.model.Blog

sealed interface BlogUpdateResult : BlogResult {
}

data class BlogUpdated(val blog: Blog) : BlogUpdateResult {
    override fun success() = true
}

data class BlogUpdateError(val message: String) : BlogUpdateResult {
    override fun success() = false
}

data class BlogNotOwnedBySpecifiedUser(val message: String) : BlogUpdateResult {
    override fun success() = false

}