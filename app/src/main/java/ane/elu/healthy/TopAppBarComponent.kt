package ane.elu.healthy

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarComponent(scrollBehavior: TopAppBarScrollBehavior) {
    val rotationAngleOnClick = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var currentRotationTarget by remember { mutableFloatStateOf(0f) }

    fun animateRotation() {
        scope.launch {
            currentRotationTarget += 360f
            rotationAngleOnClick.animateTo(
                targetValue = currentRotationTarget,
                animationSpec = tween(durationMillis = 500)
            )
        }
    }

    TopAppBar(
        title = {
            Text(
                "Healthy",
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            scrolledContainerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            IconButton(onClick = {
                animateRotation()
            }) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "Localized description",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.graphicsLayer {
                        rotationZ = rotationAngleOnClick.value
                    }
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}