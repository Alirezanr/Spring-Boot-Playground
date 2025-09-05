package dan.nr.first_project.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class HashEncoder {
    private val bcrypt = BCryptPasswordEncoder()

    fun encode(raw: String): String = bcrypt.encode(raw)

    /**
     * Compare entered password with the hashed password.
     * */
    fun matches(raw: String, hashed: String): Boolean = bcrypt.matches(raw, hashed)
}