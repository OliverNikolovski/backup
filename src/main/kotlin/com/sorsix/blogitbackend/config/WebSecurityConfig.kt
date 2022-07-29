package com.sorsix.blogitbackend.config

import com.sorsix.blogitbackend.config.filters.CustomAuthenticationFilter
import com.sorsix.blogitbackend.config.filters.CustomAuthorizationFilter
import com.sorsix.blogitbackend.service.UserService
import com.sorsix.blogitbackend.service.impl.UserServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    val authConfig: AuthenticationConfiguration,
    val userService: UserService
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val customAuthenticationFilter: CustomAuthenticationFilter = CustomAuthenticationFilter(authConfig.authenticationManager, userService)
        customAuthenticationFilter.setFilterProcessesUrl("/api/login")
        return http.cors().and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/login", "/api/users/**", "/api/blogs", "/api/blogs/all").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .addFilter(customAuthenticationFilter)
            .addFilterBefore(CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}