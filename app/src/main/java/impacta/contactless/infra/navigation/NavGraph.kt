package impacta.contactless.infra.navigation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import impacta.contactless.features.login.SignInScreen
import impacta.contactless.features.login.SignInScreenViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.Identity
import impacta.contactless.ui.GoogleAuthUiClient
import kotlinx.coroutines.CoroutineScope

fun NavGraphBuilder.login(navController: NavController, applicationContext: Context, lifecycleScope: CoroutineScope)
{
    navigation(startDestination = Screen.SignIn.route, route = "login")
    {
        val googleAuthUiClient by lazy {
            GoogleAuthUiClient(
                context = applicationContext,
                oneTapClient = Identity.getSignInClient(applicationContext)
            )
        }
        composable(Screen.SignIn.route) {
            val viewModel = viewModel<SignInScreenViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == ComponentActivity.RESULT_OK) {
                        Log.d("LOGIN", "Trying to login...")
                        lifecycleScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignResult(signInResult)
                        }
                    }
                }
            )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful)
                {
                    Toast.makeText(
                        applicationContext, "Bem vindo(a)!",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(Screen.ActiveKeys.route)
                }
            }

            SignInScreen(state = state, onSignInClick = {
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