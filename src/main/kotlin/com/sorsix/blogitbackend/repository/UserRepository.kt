package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
}