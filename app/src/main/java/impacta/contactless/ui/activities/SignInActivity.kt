package impacta.contactless.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import impacta.contactless.MainActivity
import impacta.contactless.features.signin.SignInScreen
import impacta.contactless.features.signin.SignInScreenViewModel
import impacta.contactless.features.signin.SignInUIState
import impacta.contactless.ui.components.TopKeyzAppBar
import impacta.contactless.ui.theme.KeyzTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInActivity : ComponentActivity() {
    private val viewModel: SignInScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val signInState by viewModel.sign.collectAsStateWithLifecycle()
            val state = signInState.signInUIState

            if (state is SignInUIState.Success)
                logSuccess()


            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK)
                        launchSignInResult(result.data)

                }
            )

            KeyzTheme {
                TopKeyzAppBar()
                SignInScreen(state, onSignInClick = {
                    launchSignInIntent(launcher)
                })

            }
        }
    }

    private fun logSuccess() {
        Log.d("KEYZ", "loggin success")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchSignInResult(data: Intent?) {
        lifecycleScope.launch {
            viewModel.onSignInResult(data)
        }
    }

    private fun launchSignInIntent(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        lifecycleScope.launch {
            launcher.launch(
                viewModel.getSignInIntentSender()?.let {
                    IntentSenderRequest.Builder(
                        it
                    ).build()
                }
            )
        }
    }
}
