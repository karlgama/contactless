package impacta.contactless.infra.database.models

import java.util.UUID


data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?
)
