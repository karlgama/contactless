package impacta.contactless.features.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import impacta.contactless.ui.theme.KeyzTheme

// Sempre usar Componentes do Material3
@Composable
fun SettingsScreen(
    navController: NavController? = null,
) {
    val iconColor = Color(0XFF42493F)
//    val textColor = Color(0xFF1A1C19)
    val textColor = Color(0xFFFFFFFF)
    val alertColor = Color(0xFF93000A)
    Column {
        IconTextRow(Icons.Default.Search, "Info", textColor, iconColor)
        IconTextRow(Icons.Default.Add, "Solicitar nova chave", textColor, iconColor)
        IconTextRow(Icons.Outlined.Cancel, "Apagar conta", alertColor, alertColor)
    }
}

@Composable
fun IconTextRow(
    icon: ImageVector,
    text: String,
    textColor: Color,
    iconColor: Color,
) {
    val grey = Color(0XFFC2C8BC)

    Row(modifier = Modifier.padding(vertical = 20.dp)) {
        Icon(
            icon, contentDescription = null, tint = iconColor,
            modifier = Modifier
                .padding(start = 16.dp)
        )
        Text(
            text, color = textColor,
            modifier = Modifier
                .padding(start = 16.dp)
        )
    }

    Divider(color = grey, thickness = 2.dp)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KeyzTheme {
        SettingsScreen()
    }
}