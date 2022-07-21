package com.sorsix.blogitbackend.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.SecurityFilterChain
import javax.servlet.Filter
import javax.servlet.http.HttpServletRequest

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests()?.anyRequest()?.permitAll();
    }

}