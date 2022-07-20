package com.sorsix.blogitbackend.model.enumeration

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    ROLE_USER {
        override fun getAuthority() = name
    }
}