package impacta.contactless.features.signin.data

import impacta.contactless.infra.database.dao.UserDao
import impacta.contactless.infra.database.models.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val dao: UserDao):UserRepository {

    override suspend fun save(user: User) {
        dao.insertAll(user)
    }
}