package impacta.contactless.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import impacta.contactless.features.signin.SignInScreen
import impacta.contactless.ui.GoogleAuthUiClient

@AndroidEntryPoint
class SignInActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            SignInScreen(navController, intent)
        }
    }
}