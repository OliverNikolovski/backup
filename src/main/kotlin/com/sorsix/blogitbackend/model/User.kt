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

    @Column(unique = true, length = 50, nullable = false)
    private val username: String,

    @Column(nullable = false)
    private val password: String,

    @Column
    val email: String? = null,

    @Column(name = "short_bio")
    val shortBio: String? = null,

    @Column(name = "profile_picture")
    val profilePicture: ByteArray? = null,

    @Enumerated(EnumType.STRING)
    val role: Role = Role.ROLE_USER,

    @Column(name = "is_account_non_expired")
    private val isAccountNonExpired: Boolean = true,

    @Column(name = "is_account_non_locked")
    private val isAccountNonLocked: Boolean = true,

    @Column(name = "is_credentials_non_expired")
    private val isCredentialsNonExpired: Boolean = true,

    @Column(name = "is_enabled")
    private val isEnabled: Boolean = true
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
        return isAccountNonExpired
    }

    override fun isAccountNonLocked(): Boolean {
        return isAccountNonLocked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return isCredentialsNonExpired
    }

    override fun isEnabled(): Boolean {
        return isEnabled
    }
}

