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
    private val id: Long?,

    @Column(unique = true, length = 50, nullable = false, name = "username")
    private val username: String,

    @Column(nullable = false, name = "password")
    private val password: String,

    @Column
    private val email: String?,

    @Column(name = "short_bio")
    private val shortBio: String?,

    @Column(name = "profile_picture")
    private val profilePicture: ByteArray?,

    @Enumerated(EnumType.STRING)
    private val role: Role = Role.ROLE_USER,

    @Column(name = "is_account_non_expired")
    private val isAccountNonExpired1: Boolean = true,

    @Column(name = "is_account_non_locked")
    private val isAccountNonLocked1: Boolean = true,

    @Column(name = "is_credentials_non_expired")
    private val isCredentialsNonExpired1: Boolean = true,

    @Column(name = "is_enabled")
    private val isEnabled1: Boolean = true
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
        return password
    }

    override fun getUsername(): String {
        return username
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

