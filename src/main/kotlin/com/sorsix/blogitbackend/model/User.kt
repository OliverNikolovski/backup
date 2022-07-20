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

    @Column(name = "shortbio")
    val shortBio: String,

    @Column(name = "profilepicture")
    val profilePicture: ByteArray,

    @Column(name = "isaccountnonexpired")
    val isAccountNonExpired: Boolean,

    @Column(name = "isaccountnonlocked")
    val isAcccountNonLocked: Boolean,

    @Column(name = "iscredentialsnonexpired")
    val isCredentialsNonExpired: Boolean,

    @Column(name = "isenabled")
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

