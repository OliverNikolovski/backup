package com.sorsix.blogitbackend.model.results.follow

sealed interface FollowResult {
    fun success(): Boolean
}

data class Followed(val message: String) : FollowResult {
    override fun success() = true
}

data class Unfollowed(val message: String) : FollowResult {
    override fun success() = true
}