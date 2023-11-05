package impacta.contactless.features.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Rect
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Dock
import androidx.compose.material.icons.outlined.DoorBack
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import impacta.contactless.BuildConfig
import impacta.contactless.R
import impacta.contactless.features.activekeys.domain.ValidateURLUseCase
import impacta.contactless.infra.Constants
import impacta.contactless.ui.theme.KeyzTheme
import impacta.contactless.ui.theme.md_theme_dark_errorContainer
import impacta.contactless.ui.theme.md_theme_light_error
import impacta.contactless.ui.theme.md_theme_light_onPrimary
import impacta.contactless.ui.theme.md_theme_light_onSurfaceVariant
import impacta.contactless.ui.theme.md_theme_light_outlineVariant
import impacta.contactless.ui.theme.md_theme_light_primary

enum class Keyboard {
    Opened, Closed
}

@Composable
fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                Keyboard.Opened
            } else {
                Keyboard.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(
    navController: NavController? = null
) {
    val currentUser = Firebase.auth.currentUser
    val context = LocalContext.current
    val isDialogVisible = rememberSaveable{ mutableStateOf(false) }
    val isContingencyDialogVisible = rememberSaveable { mutableStateOf(false) }
    val contingencyURL = rememberSaveable { mutableStateOf("") }
    val isEditingContingencyURL = rememberSaveable { mutableStateOf(false) }
    val errorMessage = rememberSaveable { mutableStateOf("") }
    val isError = rememberSaveable { mutableStateOf(false) }
    val isKeyboardOpen by keyboardAsState()
    val contingencyBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            it != SheetValue.PartiallyExpanded
        }
    )
    Column {
        IconTextRow(
            Icons.Default.Person,
            currentUser?.displayName ?: "",
            md_theme_light_onSurfaceVariant,
            md_theme_light_onSurfaceVariant,
            {}
        )
        IconTextRow(
            Icons.Default.Email,
            currentUser?.providerData?.get(0)?.email ?: "",
            md_theme_light_onSurfaceVariant,
            md_theme_light_onSurfaceVariant,
            {}
        )
        IconTextRow(
            Icons.Outlined.ContentCopy,
            "Código: ${currentUser?.uid}",
            md_theme_light_onSurfaceVariant,
            md_theme_light_onSurfaceVariant
        ) {
            val textToCopy = currentUser?.uid ?: ""
            val clipboardManager =
                getSystemService(context, ClipboardManager::class.java) as ClipboardManager
            val clipData = ClipData.newPlainText("text", textToCopy)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context, "Texto copiado para a área de transferência", Toast.LENGTH_LONG).show()
        }
        IconTextRow(
            Icons.Outlined.WifiOff,
            "Modo de Contingência",
            md_theme_light_onSurfaceVariant,
            md_theme_light_onSurfaceVariant
        ) {
            isContingencyDialogVisible.value = true
        }
        IconTextRow(
            Icons.Outlined.ArrowBack,
            "Desconectar",
            md_theme_light_onSurfaceVariant,
            md_theme_light_onSurfaceVariant
        ) {
            Firebase.auth.signOut().also {
                navController?.navigate("login")
            }
        }
        IconTextRow(
            Icons.Outlined.Cancel,
            stringResource(R.string.delete_account),
            md_theme_light_error,
            md_theme_light_error
        ) {
            isDialogVisible.value = true
        }
        if (isDialogVisible.value) {
            val svg = Icons.Default.Warning
            ModalBottomSheet(onDismissRequest = { isDialogVisible.value = false })
            {
                Column(modifier = Modifier
                    .height(180.dp)
                    .padding(16.dp)
                    .fillMaxWidth()
                ) {
                    Image(
                        imageVector = svg,
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(48.dp),
                        colorFilter = ColorFilter.tint(md_theme_light_error)
                    )
                    Text(
                        "Esta ação é irreversível",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Row(horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        OutlinedButton(onClick = { isDialogVisible.value = false }) {
                            Text("Voltar")
                        }
                        Button(onClick = {
                            currentUser?.delete()
                                ?.addOnCompleteListener { task ->
                                    isDialogVisible.value = false
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Conta apagada com sucesso", Toast.LENGTH_LONG).show()
                                        Firebase.auth.signOut().also {
                                            navController?.navigate("login")
                                        }
                                    } else {
                                        task.exception?.let { exception ->
                                            Toast.makeText(context, "${exception.localizedMessage}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                        }, colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_error)) {
                            Text("Excluir Conta")
                        }
                    }
                }
            }
        }
        if (isContingencyDialogVisible.value) {
            ModalBottomSheet(
                sheetState = contingencyBottomSheetState,
                onDismissRequest = {
                if (isEditingContingencyURL.value) {
                    contingencyURL.value = ""
                }
                isContingencyDialogVisible.value = false
                isEditingContingencyURL.value = false
            }) {
                Column(modifier = Modifier
                    .height(if (isKeyboardOpen == Keyboard.Closed) 200.dp else 800.dp)
                    .padding(16.dp)
                    .fillMaxWidth()
                ) {
                    Text(
                        "Modo de Contingência",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = contingencyURL.value,
                        onValueChange = { it: String ->
                            isEditingContingencyURL.value = true
                            contingencyURL.value = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("URL ou IP de Contingência")
                        },
                        supportingText = {
                             if (errorMessage.value.isNotEmpty()) {
                                 Text(errorMessage.value)
                             }
                        },
                        isError = isError.value,
                        placeholder = {
                            Text("http://192.168.0.32/")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        val isURLValid = ValidateURLUseCase().invoke(contingencyURL.value)
                        if (isURLValid) {
                            Constants.kBaseURL = contingencyURL.value
                            isEditingContingencyURL.value = false
                            isContingencyDialogVisible.value = false
                            isError.value = false
                            errorMessage.value = ""
                        } else {
                            Constants.kBaseURL = BuildConfig.AWS_BASE_URL
                            isError.value = true
                            errorMessage.value = "Digite uma URL válida."
                        }
                    }) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}

@Composable
fun IconTextRow(
    icon: ImageVector?,
    text: String,
    textColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {

    Row(modifier = Modifier
        .padding(vertical = 20.dp)
        .clickable { onClick() }) {
        icon?.let {
            Icon(
                it, contentDescription = null, tint = iconColor,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }
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