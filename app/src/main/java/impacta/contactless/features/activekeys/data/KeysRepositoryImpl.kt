package impacta.contactless.features.activekeys.data

import impacta.contactless.features.activekeys.data.remote.KeysApi
import retrofit2.Retrofit
import javax.inject.Inject

class KeysRepositoryImpl @Inject constructor(retrofit: Retrofit): KeysRepository {

    private val keysApi = retrofit.create(KeysApi::class.java)

    override suspend fun getKeyForUser(id: String): String {
        return keysApi.getByUserId(id) ?: ""
    }
}