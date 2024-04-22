package nl.izkarcos.arcoscompanion

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import nl.izkarcos.arcoscompanion.ui.homepages.HomePage
import nl.izkarcos.arcoscompanion.ui.homepages.MessagesPage
import nl.izkarcos.arcoscompanion.ui.homepages.ServersPage
import nl.izkarcos.arcoscompanion.ui.homepages.SettingsPage
import nl.izkarcos.arcoscompanion.ui.onboarding.OnboardingMainScreen
import nl.izkarcos.arcoscompanion.ui.theme.ArcOSCompanionTheme

enum class NavPages {
    Home,
    Servers,
    Messages,
    Settings,
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            ArcOSCompanionTheme(
                dynamicColor = false
            ) {
                val navController = rememberNavController()
                val modalDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                val navBackStackEntry by navController.currentBackStackEntryAsState()

                val onboarded = false

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ModalNavigationDrawer(
                        drawerState = modalDrawerState,
                        gesturesEnabled = modalDrawerState.isOpen,
                        drawerContent = {
                            ModalDrawerSheet {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        "ArcOS Companion",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    HorizontalDivider()

                                    NavPages.entries.forEach {
                                        NavigationDrawerItem(
                                            label = { Text(it.name) },
                                            icon = {
                                                Icon(
                                                    when (it.name) {
                                                        "Home" -> Icons.Rounded.Home
                                                        "Servers" -> Icons.Rounded.Menu
                                                        "Messages" -> Icons.Rounded.Email
                                                        "Settings" -> Icons.Rounded.Settings
                                                        else -> Icons.Rounded.Warning
                                                    },
                                                    contentDescription = null
                                                )
                                            },
                                            selected = navController.currentDestination?.route.equals(
                                                it.name.lowercase()
                                            ),
                                            onClick = {
                                                scope.launch {
                                                    modalDrawerState.apply {
                                                        close()
                                                    }
                                                }
                                                navController.navigate(
                                                    it.name.lowercase(),
                                                    NavOptions.Builder()
                                                        .setLaunchSingleTop(true)
                                                        .setRestoreState(true)
                                                        .build()
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    ) {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = {
                                        Row {
                                            Image(
                                                painterResource(R.drawable.companion_logo),
                                                contentDescription = null,
                                                modifier = Modifier.size(48.dp)
                                            )
                                            Text(
                                                text = when (navBackStackEntry?.destination?.route) {
                                                    "home" -> "Home"
                                                    "servers" -> "Servers"
                                                    "messages" -> "Messages"
                                                    "settings" -> "Settings"
                                                    else -> "Companion"
                                                },
                                                modifier = Modifier.align(Alignment.CenterVertically)
                                            )
                                        }
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = {
                                            scope.launch {
                                                modalDrawerState.apply {
                                                    open()
                                                }
                                            }
                                        }) {
                                            Icon(
                                                Icons.Rounded.Menu,
                                                contentDescription = "Open menu sidebar"
                                            )
                                        }
                                    }
                                )
                            }
                        ) {
                            NavHost(
                                navController,
                                startDestination = "home",
                                modifier = Modifier.padding(it),
//                                enterTransition = {
//                                    expandHorizontally()
//                                },
//                                exitTransition = {
//                                    shrinkHorizontally()
//                                }
                            ) {
                                composable("onboarding") { OnboardingMainScreen(navController) }
                                composable("home") { HomePage() }
                                composable("servers") { ServersPage() }
                                composable("messages") { MessagesPage() }
                                composable("settings") { SettingsPage() }
                            }

                            // TODO figure out how to set up onboarding screen
                            if (!onboarded) {
                                navController.navigate("onboarding")
                            } else {
                                if (Build.VERSION.SDK_INT >= 33)
                                    shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        }
                    }
                }
            }
        }
    }
}