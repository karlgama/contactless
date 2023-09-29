package impacta.contactless.features.signin.data

import android.content.Context
import android.util.Log
import impacta.contactless.infra.database.AppDatabase
import impacta.contactless.infra.database.dao.UserDao
import impacta.contactless.infra.database.models.User
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val context: Context
) : UserRepository {

    private val appDatabase: AppDatabase by lazy {
        AppDatabase.build(context)
    }

    private val dao: UserDao by lazy {
        appDatabase.userDao()
    }
    override suspend fun save(user: User) {
        dao.insertAll(user)
        Log.d("KEYZ", "users: ${dao.findById(user.id)}")
    }

    override suspend fun findById(id: String): User? {
        return dao.findById(id)
    }
}