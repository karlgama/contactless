package impacta.contactless.features.signin

import android.content.Intent
import android.content.IntentSender
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import impacta.contactless.features.signin.domain.SaveUserUseCase
import impacta.contactless.ui.GoogleOneTapAuthenticator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@Immutable
sealed interface SignInUIState {
    data class Success(val isSignInSuccessful: Boolean = false, val signInError: String? = null) :
        SignInUIState

    object Error : SignInUIState
    object Loading : SignInUIState
}

data class SignInScreenUIState(
    var signInUIState: SignInUIState
)

@HiltViewModel
class SignInScreenViewModel @Inject constructor(
    private val googleOneTapAuthenticator: GoogleOneTapAuthenticator,
    private val saveUser: SaveUserUseCase
) : ViewModel() {

    private val _sign = MutableStateFlow(SignInScreenUIState(SignInUIState.Loading))
    val sign = _sign.asStateFlow()

    suspend fun onSignInResult(data: Intent?) {
        val signInResult = data?.let {
            googleOneTapAuthenticator.signInWithIntent(it)
        }

        signInResult?.data?.let { user ->
            saveUser(user)
        }

        _sign.update {
            it.copy(
                signInUIState = SignInUIState.Success(
                    isSignInSuccessful = signInResult != null,
                    signInResult?.errorMessage
                )
            )
        }
    }


    suspend fun getSignInIntentSender(): IntentSender? {
        return googleOneTapAuthenticator.signIn()
    }


    fun resetState() {
        _sign.value = SignInScreenUIState(SignInUIState.Loading)
    }
}

