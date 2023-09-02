package impacta.contactless.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.Color
import impacta.contactless.ui.theme.LightGreen

@Composable
fun BottomNavigationBar() {
    BottomNavigation(
        backgroundColor = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Home,"") },
            label = { Text("Chaves") },
            selected = true,
            onClick ={}
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.AccountCircle,"") },
            label = { Text("Perfil") },
            selected = true,
            onClick = {}
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Settings,"") },
            label = { Text("Configurações") },
            selected = true,
            onClick = {}
        )
    }
}


@Preview
@Composable
fun preview() {
    BottomNavigationBar()
}