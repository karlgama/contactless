package impacta.contactless.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import impacta.contactless.features.signin.SignInScreen

@AndroidEntryPoint
class SignInActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            SignInScreen(navController)
        }
    }
}