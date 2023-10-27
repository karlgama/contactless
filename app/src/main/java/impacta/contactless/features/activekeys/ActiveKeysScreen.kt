package impacta.contactless.features.activekeys

import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import android.util.Log
import androidx.annotation.Dimension.Companion.DP
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dock
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.outlined.Dock
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import impacta.contactless.features.activekeys.domain.HCEService
import impacta.contactless.infra.utils.findActivity
import impacta.contactless.ui.components.DialogWithImage
import impacta.contactless.ui.components.EmptyCards
import impacta.contactless.ui.components.KeyCard
import impacta.contactless.ui.theme.md_theme_light_onPrimary
import impacta.contactless.ui.theme.md_theme_light_primary
import impacta.contactless.ui.theme.md_theme_light_primaryContainer
import impacta.contactless.ui.theme.md_theme_light_secondary
import impacta.contactless.ui.theme.md_theme_light_tertiary
import java.util.Objects

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ActiveKeysScreen(
    navController: NavController? = null,
) {
    // Devido as ações da viewmodel, não vai renderizar preview
    val viewModel: ActiveKeysViewModel = hiltViewModel()
    val keyState by viewModel.key.collectAsStateWithLifecycle()
    val activeKeyState = keyState.activeKeysUIState
    val mNfcAdapter: NfcAdapter? = null
    val pullRefreshState = rememberPullRefreshState(
        refreshing = activeKeyState is ActiveKeysUIState.Loading,
        onRefresh = { viewModel.getKey("01H14G3SP74WAPZSFX4BSSHPH0") }
    )
    val isDialogVisible = rememberSaveable { mutableStateOf(false) }
    val activity: Activity = LocalContext.current.findActivity()

    LaunchedEffect(Unit) {
        viewModel.getKey("01H14G3SP74WAPZSFX4BSSHPH0")
        Log.d("KEYZ", activeKeyState.toString())
    }
    


    if (activeKeyState is ActiveKeysUIState.Loading) {
    Column(modifier = Modifier
        .fillMaxSize()
        .pullRefresh(pullRefreshState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
    }
    else if (activeKeyState is ActiveKeysUIState.Error) {
        var isLoading = activeKeyState is ActiveKeysUIState.Loading
        Text("Erro ao recuperar chave. Tente novamente",
            modifier = Modifier
                .padding(horizontal = Dp(16F))
                .clickable {
                    viewModel.getKey("01H14G3SP74WAPZSFX4BSSHPH0")
                    Log.d("KEYZ", activeKeyState.toString())
                }
        )
        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
        )
    } else if (activeKeyState is ActiveKeysUIState.Success) {
        if (Objects.isNull(activeKeyState.code))
            EmptyCards()
        else
            KeyCard(key = activeKeyState.code!!, onClick = {
                val intent = Intent(activity, HCEService::class.java)
                intent.putExtra("ndefMessage", activeKeyState.code!!)
                activity.startService(intent)
                isDialogVisible.value = true
            })
        if (isDialogVisible.value) {
            val svg = Icons.Outlined.Dock
            ModalBottomSheet(onDismissRequest = { isDialogVisible.value = false })
            {
                Column(modifier = Modifier
                                    .height(130.dp)
                                    .padding(16.dp)
                                    .fillMaxWidth()
                ) {
                    Image(
                        imageVector = svg,
                        contentDescription = "",
                        modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .size(48.dp),
                        colorFilter = ColorFilter.tint(md_theme_light_primary)
                    )
                    Text(
                        "Aproxime seu celular do leitor",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

    } else {
        Text("Não previa cair aqui...")
    }
}


