package impacta.contactless.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import impacta.contactless.R

@Composable
fun EmptyCards() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(Modifier.padding(horizontal = 50.dp, vertical = 20.dp)) {
            Text(
                text = "Você não tem chave ativa",
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        Box(
            Modifier
                .padding(horizontal = 90.dp, vertical = 140.dp)
        ) {
            CardImage(0.dp, -14F)
            CardImage(50.dp, 1.5F)
        }
    }
}

@Composable
fun CardImage(horizontalOffSet: Dp, rotate: Float) {
    Box(
        Modifier
            .width(180.dp)
            .fillMaxHeight()
            .offset(x = horizontalOffSet)
            .rotate(degrees = rotate)

    ) {
        Image(
            painter = rememberAsyncImagePainter(R.drawable.undraw_card),
            contentDescription = null,
            modifier = Modifier.border(2.dp, Color(0xFFF3F3F3))
        )


    }
}