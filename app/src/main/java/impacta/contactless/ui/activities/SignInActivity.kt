package impacta.contactless.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import impacta.contactless.MainActivity
import impacta.contactless.features.signin.SignInScreen
import impacta.contactless.features.signin.SignInScreenViewModel
import impacta.contactless.features.signin.SignInUIState
import impacta.contactless.ui.GoogleAuthUiClient
import impacta.contactless.ui.theme.KeyzTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInActivity() : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: SignInScreenViewModel = hiltViewModel()
            val signState by viewModel.sign.collectAsStateWithLifecycle()
            val signInState = signState.signInUIState

            if (signInState is SignInUIState.Success) {
                Log.d("KEYZ", "loggin success")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        lifecycleScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )
            KeyzTheme {
                SignInScreen(signInState, onSignInClick = {
                    lifecycleScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                })

            }
        }
    }
}
