package impacta.contactless.features.login

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)