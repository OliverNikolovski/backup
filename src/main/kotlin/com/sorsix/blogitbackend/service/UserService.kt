package com.sorsix.blogitbackend.service

import com.sorsix.blogitbackend.model.User

interface UserService {






    fun findById(id: Long): User
}