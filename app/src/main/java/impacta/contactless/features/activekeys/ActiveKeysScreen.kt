package impacta.contactless.features.activekeys

import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import impacta.contactless.ui.theme.KeyzTheme

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
    } else if (activeKeyState is ActiveKeysUIState.Success){
        Text(
            activeKeyState.code ?: "Parece que vocên não tem chave ativa"
        )
    } else {
        Text("Não previa cair aqui...")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KeyzTheme {
        ActiveKeysScreen()
    }
}