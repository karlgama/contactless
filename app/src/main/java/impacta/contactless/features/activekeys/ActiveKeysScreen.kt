package impacta.contactless.features.activekeys

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import impacta.contactless.ui.theme.md_theme_light_secondary
import java.util.UUID

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

//    if (activeKeyState is ActiveKeysUIState.Loading) {
//        CircularProgressIndicator()
//    } else if (activeKeyState is ActiveKeysUIState.Error) {
//        Text("Erro ao recuperar chave")
//    } else if (activeKeyState is ActiveKeysUIState.Success) {
//        if (Objects.isNull(activeKeyState.code))
//            EmptyCards()
//        else
    KeyCard()
//    } else {
//        Text("Não previa cair aqui...")
//    }
}

@Composable
fun KeyCard() {
    Column(
        Modifier
            .padding(horizontal = 30.dp)
    ) {
        Text(
            "Sua chave ativa", fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier
                .background(
                    md_theme_light_secondary,
                    shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp)
                )
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 20.dp)
        ) {
            Column {
                Text(
                    "Chave do João",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    UUID.randomUUID().toString(),
                    fontSize = 14.sp,
                    color = Color.White
                )
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
