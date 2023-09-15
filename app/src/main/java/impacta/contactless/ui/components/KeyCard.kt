package impacta.contactless.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import impacta.contactless.R
import impacta.contactless.ui.theme.md_theme_light_onPrimary
import impacta.contactless.ui.theme.md_theme_light_secondary
import java.util.UUID

@Composable
fun KeyCard() {
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
        Surface(shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp)) {
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
                        UUID.randomUUID().toString(),
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
        KeyCard()
    }
}