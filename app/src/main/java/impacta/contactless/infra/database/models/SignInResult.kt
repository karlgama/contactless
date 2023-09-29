package impacta.contactless.infra.database.models


data class SignInResult(
    val data: User?,
    val errorMessage: String?
)

