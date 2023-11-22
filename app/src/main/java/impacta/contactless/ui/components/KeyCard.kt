package impacta.contactless.ui.components

import android.app.Activity
import impacta.contactless.infra.utils.findActivity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import impacta.contactless.R
import impacta.contactless.features.activekeys.domain.HCEService
import impacta.contactless.ui.theme.md_theme_light_onPrimary
import impacta.contactless.ui.theme.md_theme_light_secondary
import java.util.UUID

@Composable
fun KeyCard(key: String, onClick: (() -> Unit)? = null) {
    Column(
        Modifier
            .padding(horizontal = 30.dp)
    ) {
        Text(
            stringResource(R.string.active_key), fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Surface(shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp),
            modifier = Modifier
                .clickable {
                    onClick?.let { it() }
                 }
        ) {
            Box(
                modifier = Modifier
                    .background(md_theme_light_secondary)
                    .fillMaxSize()
                    .padding(horizontal = 30.dp, vertical = 20.dp)
            ) {
                Column {
                    Text(
                        stringResource(R.string.key_from),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = md_theme_light_onPrimary
                    )
                    Text(
                        key,
                        fontSize = 14.sp,
                        color = md_theme_light_onPrimary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewKeyCard() {
    Column {
        KeyCard(UUID.randomUUID().toString())
    }
}