package impacta.contactless.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import impacta.contactless.ui.theme.LightGreen

@Composable
fun BottomNavigationBar() {
    BottomNavigation(
        backgroundColor = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        navigationItem("Chaves", Icons.Default.Home,true)
        navigationItem("Perfil", Icons.Default.AccountCircle)
        navigationItem("Configurações", Icons.Default.Settings)
    }
}

@Composable
private fun RowScope.navigationItem(text: String, icon: ImageVector, selected: Boolean = false) {
    BottomNavigationItem(
        icon = { Icon(icon, "") },
        label = { Text(text) },
        selected = selected,
        selectedContentColor = LightGreen,
        unselectedContentColor = Color.Black,
        onClick = {}
    )
}

@Preview
@Composable
fun preview() {
    BottomNavigationBar()
}

