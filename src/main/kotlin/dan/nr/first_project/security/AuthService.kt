package dan.nr.first_project.security

import dan.nr.first_project.database.model.User
import dan.nr.first_project.database.repository.UserRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder
) {

    data class TokenPair(
        val accessToken: String,
        val refreshToken: String,
    )

    fun register(email: String, password: String): User {
        return userRepository.save(
            User(
                email = email,
                hashedPassword = hashEncoder.encode(password)
            )
        )
    }

    fun login(email: String, password: String): TokenPair {
        val user = userRepository.findByEmail(email) ?: throw BadCredentialsException("Wrong credential.")

        if (hashEncoder.matches(password, user.hashedPassword).not()) {
            throw BadCredentialsException("Wrong credential.")
        }
        val newAccessToken = jwtService.generateAccessToken(user.id.toHexString())
        val newRefreshToken = jwtService.generateRefreshToken(user.id.toHexString())
        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }
}