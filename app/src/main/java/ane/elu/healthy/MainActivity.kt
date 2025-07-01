package ane.elu.healthy

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import ane.elu.healthy.ui.theme.CarbCounterTheme

class MainActivity : ComponentActivity() {
    private var keepSplashScreen = true

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        splash.setKeepOnScreenCondition { keepSplashScreen }

        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
        windowInsetsController.isAppearanceLightNavigationBars = false

        setContent {
            CarbCounterTheme {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    SplashScreen(
                        onSplashFinished = {
                            showSplash = false
                            keepSplashScreen = false
                        }
                    )
                } else {
                    val navController = rememberNavController()
                    val scrollBehavior =
                        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
                    var navigationBarInfo by remember { mutableStateOf<NavigationBarInfo?>(null) }
                    val isNavBarCollapsed = remember { mutableStateOf(false) }

                    @Suppress("DEPRECATION")
                    window.statusBarColor = MaterialTheme.colorScheme.primary.toArgb()
                    @Suppress("DEPRECATION")
                    window.navigationBarColor = MaterialTheme.colorScheme.primary.toArgb()

                    if (isTablet()) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            navigationBarInfo?.let { info ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(120.dp),
                                ) {
                                    AnimatedNavigationBar(
                                        buttons = info.buttons,
                                        selectedIndex = info.selectedIndex,
                                        onItemClick = info.onItemClick,
                                        modifier = Modifier.fillMaxSize(),
                                        isVertical = true,
                                    )
                                }
                            }

                            Scaffold(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                                topBar = {
                                    TopAppBarComponent(scrollBehavior = scrollBehavior)
                                },
                                containerColor = MaterialTheme.colorScheme.background
                            ) { innerPadding ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                    color = MaterialTheme.colorScheme.background
                                ) {
                                    MainContentAndNavLogic(
                                        navController = navController,
                                        onProvideNavigationBarInfo = { info ->
                                            navigationBarInfo = info
                                        },
                                        onScrollChange = { isScrollingDown ->
                                            isNavBarCollapsed.value = isScrollingDown
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Scaffold(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                                topBar = {
                                    TopAppBarComponent(scrollBehavior = scrollBehavior)
                                },
                                containerColor = MaterialTheme.colorScheme.background
                            ) { innerPadding ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                    color = MaterialTheme.colorScheme.background
                                ) {
                                    MainContentAndNavLogic(
                                        navController = navController,
                                        onProvideNavigationBarInfo = { info ->
                                            navigationBarInfo = info
                                        },
                                        onScrollChange = { isScrollingDown ->
                                            isNavBarCollapsed.value = isScrollingDown
                                        }
                                    )
                                }
                            }

                            navigationBarInfo?.let { info ->
                                AnimatedNavigationBar(
                                    buttons = info.buttons,
                                    selectedIndex = info.selectedIndex,
                                    onItemClick = info.onItemClick,
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .fillMaxWidth()
                                        .navigationBarsPadding(),
                                    isVertical = false,
                                    strokeColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                    strokeWidth = 2.5.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun isTablet(): Boolean {
    val config = LocalConfiguration.current
    return config.screenWidthDp >= 600
}
