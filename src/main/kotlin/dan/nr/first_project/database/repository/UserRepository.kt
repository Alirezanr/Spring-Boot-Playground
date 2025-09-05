package dan.nr.first_project.database.repository

import dan.nr.first_project.database.model.User

interface UserRepository {
    fun findByEmail(email: String): User?
}