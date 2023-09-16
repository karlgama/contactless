package impacta.contactless.infra.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import impacta.contactless.R

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object ActiveKeys : Screen("keys", R.string.app_name, Icons.Filled.Home)
    object Settings: Screen("settings", R.string.settings_screen, Icons.Filled.Settings)
    object SignIn: Screen("sign_in", R.string.sign_in, Icons.Filled.Settings)
}