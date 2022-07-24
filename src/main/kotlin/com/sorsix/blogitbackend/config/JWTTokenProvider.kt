package com.sorsix.blogitbackend.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

fun generateJwtToken(user: UserDetails): String {
    val algorithm = Algorithm.HMAC256(SECRET)
    return JWT.create()
        .withSubject(user.username)
        .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .withIssuer(ISSUER)
        .withClaim("roles", user.authorities.map { it.authority }.toList())
        .sign(algorithm)
}