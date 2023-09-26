package impacta.contactless.features.activekeys.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface KeysApi {
    @GET("v1/keys/{id}")
    suspend fun getByUserId(@Path("id") id: String): Key?
}