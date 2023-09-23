package impacta.contactless.features.signin

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import impacta.contactless.R
import impacta.contactless.infra.navigation.Screen
import kotlinx.coroutines.launch


@Composable
fun SignInScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: SignInScreenViewModel = hiltViewModel()
    val state by viewModel.sign.collectAsStateWithLifecycle()

    if(state.isSignInSuccessful)
        navController.navigate(Screen.ActiveKeys.route){
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context, error,
                Toast.LENGTH_SHORT
            ).show()
        }
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
            LoginOptionButton("Google") { viewModel.onSignIn() }
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




//lifecycleScope.launch {
//    val signInIntentSender = googleAuthUiClient.signIn()
//    launcher.launch(
//        IntentSenderRequest.Builder(
//            signInIntentSender ?: return@launch
//        ).build()
//    )
//}
//
//
//launcher.launch(
//IntentSenderRequest.Builder(
//signInIntentSender ?: return@launch
//).build()
//)
