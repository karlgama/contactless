package impacta.contactless.features.signin.data

import android.util.Log
import impacta.contactless.infra.database.AppDatabase
import impacta.contactless.infra.database.dao.UserDao
import impacta.contactless.infra.database.models.User
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : UserRepository {

    private lateinit var dao: UserDao;
    override suspend fun save(user: User) {
        dao = appDatabase.userDao()
        dao.insertAll(user)
        Log.d("KEYZ", "users: ${dao.findById(user.id)}")
    }

    override suspend fun findById(id: String): User? {
        return dao.findById(id)
    }
}