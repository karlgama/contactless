package impacta.contactless

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import impacta.contactless.features.activekeys.ActiveKeysScreen
import impacta.contactless.features.signin.SignInScreen
import impacta.contactless.features.signin.SignInScreenViewModel
import impacta.contactless.features.settings.SettingsScreen
import impacta.contactless.infra.navigation.Screen
import impacta.contactless.ui.GoogleAuthUiClient
import impacta.contactless.ui.activities.SignInActivity
import impacta.contactless.ui.theme.KeyzTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val items = listOf(
                Screen.ActiveKeys, Screen.Settings
            )
            KeyzTheme {
                Scaffold(topBar = {
                    TopAppBar()
                }, bottomBar = {
                    BottomNavigationBar(navController, items)
                }) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = Screen.ActiveKeys.route,
                        Modifier.padding(innerPadding)
                    ) {

                        composable(Screen.ActiveKeys.route) { ActiveKeysScreen(navController) }
                        composable(Screen.Settings.route) { SettingsScreen(navController) }
//                        composable("sign_in") { SignInScreen(
//                            navController = navController,
//                            intent = intent
//                        )}
                    }
                }
            }
        }
    }

    @Composable
    private fun BottomNavigationBar(
        navController: NavHostController,
        items: List<Screen>
    ) {
        BottomNavigation(
            modifier = Modifier.height(Dp(64.0f)),
            backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                Dp(0.0f)
            )
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            items.forEach { screen ->
                val isActiveRoute =
                    currentDestination?.hierarchy?.any { it.route == screen.route } == true
                BottomNavigationItem(icon = {
                    Icon(
                        screen.icon,
                        contentDescription = null,
                        modifier = Modifier
                            .background(
                                color = if (isActiveRoute) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                                shape = Shapes().extraLarge
                            )
                            .height(Dp(32.0f))
                            .width(Dp(56.0f))
                            .padding(Dp(2.0f))
                    )
                },
                    label = { Text(stringResource(id = screen.resourceId)) },
                    selected = isActiveRoute,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            }
        }
    }

    @Composable
    private fun TopAppBar() {
        TopAppBar(backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            title = {
                Text(
                    "Keyz",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            })
    }
}