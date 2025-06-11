package ane.elu.healthy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.compose.rememberNavController
import ane.elu.healthy.ui.theme.CarbCounterTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarbCounterTheme {
                val navController = rememberNavController()
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
                var navigationBarInfo by remember { mutableStateOf<NavigationBarInfo?>(null) }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopAppBarComponent(scrollBehavior = scrollBehavior)
                    },
                    bottomBar = {
                        navigationBarInfo?.let { info ->
                            AnimatedNavigationBar(
                                buttons = info.buttons,
                                selectedIndex = info.selectedIndex,
                                onItemClick = info.onItemClick
                            )
                        }
                    }
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
            }
        }
    }
}