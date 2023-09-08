package impacta.contactless.features.activekeys

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import impacta.contactless.R

// Sempre usar Componentes do Material3
@Composable
fun ActiveKeysScreen(
    navController: NavController? = null,
) {
    // Devido as ações da viewmodel, não vai renderizar preview
    val viewModel: ActiveKeysViewModel = hiltViewModel()
    val keyState by viewModel.key.collectAsStateWithLifecycle()
    val activeKeyState = keyState.activeKeysUIState
    viewModel.getKey("123")
    Log.d("KEYZ", activeKeyState.toString())

    if (activeKeyState is ActiveKeysUIState.Loading) {
        CircularProgressIndicator()
    } else if (activeKeyState is ActiveKeysUIState.Error) {
        Text("Erro ao recuperar chave")
    } else if (activeKeyState is ActiveKeysUIState.Success) {
        EmptyCards()
    } else {
        Text("Não previa cair aqui...")
    }
}

@Composable
private fun EmptyCards() {
    Column(
        Modifier
            .fillMaxHeight()
            .padding(10.dp)
    ) {
        Row(Modifier.padding(horizontal = 50.dp, vertical = 20.dp)) {
            Text(
                text = "Você não tem chave ativa",
                fontSize = 24.sp
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
            .height(500.dp)
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
