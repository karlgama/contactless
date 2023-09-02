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
        NavigationItem("Chaves", Icons.Default.Home,true)
        NavigationItem("Perfil", Icons.Default.AccountCircle)
        NavigationItem("Configurações", Icons.Default.Settings)
    }
}

@Composable
private fun RowScope.NavigationItem(text: String, icon: ImageVector, selected: Boolean = false) {
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

