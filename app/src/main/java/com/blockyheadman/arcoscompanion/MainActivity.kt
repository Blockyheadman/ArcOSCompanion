package com.blockyheadman.arcoscompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.blockyheadman.arcoscompanion.ui.theme.ArcOSCompanionTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            ArcOSCompanionTheme {
                val navController = rememberNavController()
                val modalDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet {
                                Button(onClick = {
                                    scope.launch {
                                        modalDrawerState.apply {
                                            close()
                                        }
                                        navController.navigate("home")
                                    }
                                }) {
                                    Text("Home")
                                }
                                Button(onClick = {
                                    scope.launch {
                                        modalDrawerState.apply {
                                            close()
                                        }
                                        navController.navigate("servers")
                                    }
                                }) {
                                    Text("Servers")
                                }
                            }
                        },
                        drawerState = modalDrawerState,
                        gesturesEnabled = modalDrawerState.isOpen
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
                                                "Companion",
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
                                modifier = Modifier.padding(it)
                            ) {
                                composable("home") { HomePage() }
                                composable("servers") { ServersPage() }
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun HomePage() {
    Text("Home")
}

@Composable
fun ServersPage() {
    Text("Servers")
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    ArcOSCompanionTheme {
        HomePage()
    }
}