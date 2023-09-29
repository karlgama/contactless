package impacta.contactless.features.signin.data

import impacta.contactless.infra.database.models.User

interface UserRepository {
    suspend fun save(user: User)
    suspend fun findById(id: String): User?

}