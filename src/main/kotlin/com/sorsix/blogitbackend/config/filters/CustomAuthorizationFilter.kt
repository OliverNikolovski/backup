package com.sorsix.blogitbackend.config.filters

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.databind.ObjectMapper
import com.sorsix.blogitbackend.config.SECRET
import com.sorsix.blogitbackend.config.TOKEN_PREFIX
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.ArrayList

class CustomAuthorizationFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.servletPath.equals("/api/login") || request.servletPath.equals("/api/users/register")) {
            filterChain.doFilter(request, response)
        } else {
            val authorizationHeader: String? = request.getHeader(HttpHeaders.AUTHORIZATION)
            if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
                try {
                    val token: String = authorizationHeader.substring(TOKEN_PREFIX.length)
                    if (token == "null" && request.method == "GET") {
                        filterChain.doFilter(request, response)
                    }
                    else {
                        val algorithm: Algorithm = Algorithm.HMAC256(SECRET)
                        val verifier: JWTVerifier = JWT.require(algorithm).build()
                        val decodedJWT: DecodedJWT = verifier.verify(token)
                        val username: String = decodedJWT.subject
                        val roles: Array<String>? = decodedJWT.claims["roles"]?.asArray(String::class.java)
                        val authorities: MutableCollection<SimpleGrantedAuthority> = ArrayList()
                        Arrays.stream(roles).forEach { authorities.add(SimpleGrantedAuthority(it)) }
                        val authenticationToken =
                            UsernamePasswordAuthenticationToken(username, null, authorities)
                        SecurityContextHolder.getContext().authentication = authenticationToken
                        filterChain.doFilter(request, response)
                    }
                } catch (ex: Exception) {
                    response.status = HttpStatus.FORBIDDEN.value()
                    response.contentType = APPLICATION_JSON_VALUE
                    ObjectMapper().writeValue(response.outputStream, mapOf("error" to ex.message))
                }
            } else {
                filterChain.doFilter(request, response)
            }
        }

    }

}