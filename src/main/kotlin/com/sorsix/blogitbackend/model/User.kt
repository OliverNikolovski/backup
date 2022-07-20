package com.sorsix.blogitbackend.model

import javax.persistence.*

@Entity
@Table(name="users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(unique = true, length = 50, nullable = false)
    val username: String,

    @Column(nullable = false)
    val password: String,

    @Column
    val email: String,

    @Column(name = "short_bio")
    val shortBio: String,

    @Column(name = "profile_picture")
    val profilePicture: ByteArray,

    @Column(name = "is_account_non_expired")
    val isAccountNonExpired: Boolean,

    @Column(name = "is_account_non_locked")
    val isAcccountNonLocked: Boolean,

    @Column(name = "is_credentials_non_expired")
    val isCredentialsNonExpired: Boolean,

    @Column(name = "is_enabled")
    val isEnabled: Boolean
) {
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
}

