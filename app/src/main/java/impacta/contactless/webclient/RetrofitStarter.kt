package impacta.contactless.webclient

import impacta.contactless.webclient.services.UserService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitStarter {
    private val retrofit = Retrofit.Builder()
        .baseUrl("")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val userService = retrofit.create(UserService::class.java)
}
