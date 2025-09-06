package dan.nr.first_project.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Base64
import java.util.Date

@Service
class JwtService(
    @Value("JWT_SECRET_BASE64")
    private val jwtSecret: String,
) {
    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret))
    private val accessTokenValidityMs = 15L * 60L * 1000L
    val refreshTokenValidityMs = 30L * 24L * 60L * 60L * 1000L

    private fun generateToke(
        userId: String,
        type: String,
        expiry: Long,
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + expiry)
        return Jwts.builder()
            .subject(userId)
            .claims(mapOf("type" to type))
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    fun generateAccessToken(userId: String): String {
        return generateToke(userId, "access", accessTokenValidityMs)
    }

    fun generateRefreshToken(userId: String): String {
        return generateToke(userId, "refresh", refreshTokenValidityMs)
    }

    fun validateAccessToken(token: String): Boolean {
        val claims = parseAlClaims(token) ?: return false
        val tokenType = claims["type"] as? String ?: return false

        return tokenType == "assess"
    }

    fun validateRefreshToken(token: String): Boolean {
        val claims = parseAlClaims(token) ?: return false
        val tokenType = claims["type"] as? String ?: return false

        return tokenType == "refresh"
    }

    fun getUserIdFromToken(token: String): String {
        val claims = parseAlClaims(token) ?: throw IllegalArgumentException("Invalid token.")
        return claims.subject
    }

    private fun parseAlClaims(token: String): Claims? {
        return try {
            val rawToken = token.replace("Bearer ", "")
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(rawToken)
                .payload
        } catch (e: Exception) {
            null
        }
    }
}