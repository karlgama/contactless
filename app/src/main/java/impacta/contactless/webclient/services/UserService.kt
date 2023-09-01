package impacta.contactless.webclient.services

import impacta.contactless.database.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface UserService {
    @GET("users/{id}")
    suspend fun findById(@Path("id") id: UUID): Call<User?>
}