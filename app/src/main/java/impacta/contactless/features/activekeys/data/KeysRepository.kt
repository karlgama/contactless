package impacta.contactless.features.activekeys.data

interface KeysRepository {
    suspend fun getKeyForUser(id: String): String
}