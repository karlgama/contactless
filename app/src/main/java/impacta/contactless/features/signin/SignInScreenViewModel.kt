package impacta.contactless.features.signin

import android.content.Intent
import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import impacta.contactless.infra.database.models.SignInResult
import impacta.contactless.ui.GoogleAuthUiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    var googleAuthUiClient: GoogleAuthUiClient
) : ViewModel() {

    private val _sign = MutableStateFlow(SignInScreenUIState(SignInUIState.Loading))
    val sign = _sign.asStateFlow()

    fun onSignIn(intent: Intent): LiveData<SignInResult> {
        val resultLiveData = MutableLiveData<SignInResult>()

        viewModelScope.launch {
            val signInResult = googleAuthUiClient.signInWithIntent(intent)
            resultLiveData.value = signInResult
        }

        return resultLiveData
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        return googleAuthUiClient.signInWithIntent(intent)
    }

    fun onSignResult(result: SignInResult) {
        _sign.update {
            it.copy(
                signInUIState = SignInUIState.Success(
                    isSignInSuccessful = result.data != null,
                    result.errorMessage
                )
            )
        }
    }

    fun resetState() {
        _sign.value = SignInScreenUIState(SignInUIState.Loading)
    }
}