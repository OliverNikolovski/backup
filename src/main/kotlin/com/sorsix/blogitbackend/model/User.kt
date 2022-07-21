package com.sorsix.blogitbackend.model

import com.sorsix.blogitbackend.model.enumeration.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(unique = true, length = 50, nullable = false, name = "username")
    val username1: String,

    @Column(nullable = false, name = "password")
    val password1: String,

    @Column
    val email: String,

    @Column(name = "short_bio")
    val shortBio: String,

    @Column(name = "profile_picture")
    val profilePicture: ByteArray,

    @Enumerated(EnumType.STRING)
    val role: Role,

    @Column(name = "is_account_non_expired")
    val isAccountNonExpired1: Boolean,

    @Column(name = "is_account_non_locked")
    val isAccountNonLocked1: Boolean,

    @Column(name = "is_credentials_non_expired")
    val isCredentialsNonExpired1: Boolean,

    @Column(name = "is_enabled")
    val isEnabled1: Boolean
) : UserDetails {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return Collections.singletonList(role)
    }

    override fun getPassword(): String {
        return password1
    }

    override fun getUsername(): String {
        return username1
    }

    override fun isAccountNonExpired(): Boolean {
        return isAccountNonExpired1
    }

    override fun isAccountNonLocked(): Boolean {
        return isAccountNonLocked1
    }

    override fun isCredentialsNonExpired(): Boolean {
        return isCredentialsNonExpired1
    }

    override fun isEnabled(): Boolean {
        return isEnabled1
    }
}

