package ane.elu.healthy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import ane.elu.healthy.ui.theme.CarbCounterTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
        windowInsetsController.isAppearanceLightNavigationBars = false

        setContent {
            CarbCounterTheme {
                val navController = rememberNavController()
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
                var navigationBarInfo by remember { mutableStateOf<NavigationBarInfo?>(null) }

                window.statusBarColor = MaterialTheme.colorScheme.primary.toArgb()
                window.navigationBarColor = MaterialTheme.colorScheme.onPrimaryContainer.toArgb()

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
                                .align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }
}