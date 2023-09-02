package impacta.contactless.features.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import impacta.contactless.ui.theme.KeyzTheme

// Sempre usar Componentes do Material3
@Composable
fun SettingsScreen(
    navController: NavController? = null,
) {
    Text("Settings")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KeyzTheme {
        SettingsScreen()
    }
}