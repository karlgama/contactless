package impacta.contactless.features.settings

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import impacta.contactless.R
import impacta.contactless.ui.theme.KeyzTheme
import impacta.contactless.ui.theme.md_theme_dark_errorContainer
import impacta.contactless.ui.theme.md_theme_light_onPrimary
import impacta.contactless.ui.theme.md_theme_light_onSurfaceVariant
import impacta.contactless.ui.theme.md_theme_light_outlineVariant

@Composable
fun SettingsScreen(
    navController: NavController? = null
) {
    Column {
        IconTextRow(
            Icons.Default.Search,
            stringResource(R.string.info),
            md_theme_light_onSurfaceVariant,
            md_theme_light_onSurfaceVariant,
            {}
        )
        IconTextRow(
            Icons.Default.Add,
            stringResource(R.string.new_key_request),
            md_theme_light_onSurfaceVariant,
            md_theme_light_onSurfaceVariant,
            {}
        )
        IconTextRow(
            Icons.Outlined.Cancel,
            stringResource(R.string.delete_account),
            md_theme_dark_errorContainer,
            md_theme_dark_errorContainer,
            {}
        )
    }
}

@Composable
fun IconTextRow(
    icon: ImageVector,
    text: String,
    textColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {

    Row(modifier = Modifier
        .padding(vertical = 20.dp)
        .clickable { }) {
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

    Divider(color = md_theme_light_outlineVariant, thickness = 2.dp)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KeyzTheme {
        SettingsScreen()
    }
}