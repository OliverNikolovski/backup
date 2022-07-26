package com.sorsix.blogitbackend.config.filters

import com.fasterxml.jackson.databind.ObjectMapper
import com.sorsix.blogitbackend.config.generateJwtToken
import com.sorsix.blogitbackend.model.dto.UserDto
import com.sorsix.blogitbackend.service.UserService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthenticationFilter(
    private val authManager: AuthenticationManager,
    private val userService: UserService) :
    UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val username = request?.getParameter("username")
        val password = request?.getParameter("password")
        val authenticationToken = UsernamePasswordAuthenticationToken(username, password)
        return authManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        val userDetails: UserDetails = authResult?.principal as UserDetails
        val user: UserDto? = this.userService.getUserDtoByUsername(userDetails.username)
        val token = generateJwtToken(userDetails)
        response?.contentType = APPLICATION_JSON_VALUE
        ObjectMapper().writeValue(
            response?.outputStream,
            mapOf("access_token" to token, "user" to user))
    }
}