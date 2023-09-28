package impacta.contactless.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
public fun TopKeyzAppBar() {
    androidx.compose.material.TopAppBar(backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        title = {
            Text(
                "Keyz",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        })
}