package impacta.contactless.features.signin

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import impacta.contactless.infra.database.models.SignInResult
import impacta.contactless.ui.GoogleAuthUiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
@HiltViewModel
class SignInScreenViewModel @Inject constructor(
    private val googleAuthUiClient: GoogleAuthUiClient
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())

    val sign = _state.asStateFlow()

    fun onSignResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }


    fun onSignIn() {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { result ->
                if (result.resultCode == ComponentActivity.RESULT_OK) {
                    viewModelScope.launch {
                        val signInResult = googleAuthUiClient.signInWithIntent(
                            intent = result.data ?: return@launch
                        )
                        onSignResult(signInResult)
                    }
                }
            }
        )
//        viewModelScope.launch {
//            val signInIntentSender = googleAuthUiClient.signIn()
//
//            _state.value = SignInState(isSignInSuccessful = true)
//        }
    }

    fun resetState() {
        _state.value = SignInState()
    }
}