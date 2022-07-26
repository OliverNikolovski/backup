package com.sorsix.blogitbackend.config

import com.sorsix.blogitbackend.config.filters.CustomAuthenticationFilter
import com.sorsix.blogitbackend.config.filters.CustomAuthorizationFilter
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
class WebSecurityConfig(val authConfig: AuthenticationConfiguration) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val customAuthenticationFilter: CustomAuthenticationFilter = CustomAuthenticationFilter(authConfig.authenticationManager)
        customAuthenticationFilter.setFilterProcessesUrl("/api/login")
        return http.cors().and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/login", "/api/register", "/api/blogs", "/api/blogs/all").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .addFilter(customAuthenticationFilter)
            .addFilterBefore(CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}