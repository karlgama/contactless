package impacta.contactless.features.signin

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import impacta.contactless.R
import impacta.contactless.infra.navigation.Screen
import impacta.contactless.ui.GoogleAuthUiClient


@Composable
fun SignInScreen(
    navController: NavController,
    intent: Intent?
) {
    val context = LocalContext.current
    val viewModel: SignInScreenViewModel = hiltViewModel()
    val signState by viewModel.sign.collectAsStateWithLifecycle()
    val signInState = signState.signInUIState

    if (signInState is SignInUIState.Success)
        navController.navigate(Screen.ActiveKeys.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

    LaunchedEffect(key1 = signInState) {
        if (signInState is SignInUIState.Error)
            Toast.makeText(
                context, "Error on sign in",
                Toast.LENGTH_SHORT
            ).show()

    }
    Column(
        Modifier
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(R.string.login_title))
        Image(
            modifier = Modifier.fillMaxSize(0.5f),
            painter = painterResource(R.drawable.login_lock_screen),
            contentDescription = stringResource(R.string.img_description_grey_lock)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(),
        ) {
            LoginOptionButton("Google") {
                intent?.let {it ->
                    viewModel.onSignIn(it)
                }
            }
//            LoginOptionButton("Facebook")
        }
    }
}

@Composable
private fun LoginOptionButton(content: String, onSignInClick: () -> Unit) {
    Button(
        content = { Text(text = content) },
        onClick = onSignInClick,
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp),
        modifier = Modifier
            .widthIn(min = 120.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    )
}

//
//val launcher = rememberLauncherForActivityResult(
//    contract = ActivityResultContracts.StartIntentSenderForResult(),
//    onResult = { result ->
//        if (result.resultCode == ComponentActivity.RESULT_OK) {
//            lifecycleScope.launch {
//                val signInResult = googleAuthUiClient.signInWithIntent(
//                    intent = result.data ?: return@launch
//                )
//                viewModel.onSignResult(signInResult)
//            }
//        }
//    }
//)

