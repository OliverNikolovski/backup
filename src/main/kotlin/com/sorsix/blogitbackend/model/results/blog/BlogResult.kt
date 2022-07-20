package com.sorsix.blogitbackend.model.results.blog

sealed interface BlogResult {
    fun success(): Boolean
}


