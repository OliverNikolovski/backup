package com.sorsix.blogitbackend.service.impl

import com.sorsix.blogitbackend.model.enumeration.Role
import com.sorsix.blogitbackend.repository.UserRepository
import com.sorsix.blogitbackend.service.UserService
import com.sorsix.blogitbackend.model.User
import com.sorsix.blogitbackend.model.dto.UserDto
import com.sorsix.blogitbackend.model.exception.UserNotFoundException
import com.sorsix.blogitbackend.model.results.follow.FollowResult
import com.sorsix.blogitbackend.model.results.follow.Followed
import com.sorsix.blogitbackend.model.results.follow.Unfollowed
import com.sorsix.blogitbackend.model.results.user.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserServiceImpl(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder
) : UserService {
    override fun findByIdOrThrow(id: Long) =
        userRepository.findByIdOrNull(id) ?: throw UserNotFoundException("User with id $id does not exist.")

    override fun findByUsername(username: String): User? = userRepository.findByUsername(username)


    override fun getUserDtoByUsername(username: String): UserDto? =
        this.findByUsername(username)?.let { toDto(it) }

    override fun existsByUsername(username: String) = userRepository.existsByUsername(username)

    override fun register(
        username: String,
        password: String,
        repeatedPassword: String,
        email: String?,
        shortBio: String?,
        profilePicture: ByteArray?,
        profilePictureFormat: String?
    ): UserRegisterResult {

        if (username.isEmpty() || containsWhiteSpace(username))
            return InvalidUsername("Invalid username: $username")

        if (password.isBlank() || password.length < 3)
            return InvalidPassword("Invalid password: $password")

        if (password != repeatedPassword)
            return PasswordsDoNotMatch("Passwords do not match.")

        if (userRepository.existsByUsername(username))
            return UsernameTaken("Username $username taken.")

        val user = User(
            id = 0,
            username = username,
            password = passwordEncoder.encode(password),
            email = email,
            shortBio = shortBio,
            profilePicture = profilePicture,
            profilePictureFormat = profilePictureFormat,
            role = Role.ROLE_USER
        )
        return try {
            val savedUser = this.userRepository.save(user)
            UserRegistered(toDto(savedUser))
        } catch (ex: Exception) {
            UsernameRegistrationError("There was an error. Please try again.")
        }
    }

    @Transactional
    override fun followOrUnfollow(followerId: Long, followedId: Long): FollowResult {
        findByIdOrThrow(followedId)
        findByIdOrThrow(followedId)
        return if (userRepository.followExists(followerId, followedId)) {
            userRepository.unfollow(followerId = followerId, followedId = followedId)
            Unfollowed("Unfollow successful.")
        }
        else {
            userRepository.follow(followerId = followerId, followedId = followedId)
            return Followed("Follow successful.")
        }
    }

    // return the users that follow the particular user
    override fun getFollowersForUser(userId: Long): List<User> {
        findByIdOrThrow(userId)
        val followerIds = userRepository.getFollowersForUser(userId)
        return userRepository.findAllById(followerIds)
    }

    // return the users that this particular user is following
    override fun getFollowingForUser(userId: Long): List<User> {
        findByIdOrThrow(userId)
        val followingIds = userRepository.getFollowingForUser(userId)
        return userRepository.findAllById(followingIds)
    }

    override fun getBlogPoster(blogId: Long): UserDto? {
        return this.userRepository.getBlogPoster(blogId)?.let {
            toDto(this.userRepository.findByIdOrNull(it)!!)
        }
    }

    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByUsername(username) ?: throw UsernameNotFoundException("Username does not exist.")

    private fun containsWhiteSpace(str: String) = str.matches(Regex(""".*\s+.*"""))

    private fun toDto(user: User): UserDto {
        return UserDto(
            username = user.username,
            email = user.email,
            shortBio = user.shortBio,
            profilePicture = user.profilePicture,
            profilePictureFormat = user.profilePictureFormat,
            role = user.role
        )
    }
}