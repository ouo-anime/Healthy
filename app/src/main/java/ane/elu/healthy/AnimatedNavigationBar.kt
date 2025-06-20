package ane.elu.healthy

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

@Composable
fun AnimatedNavigationBar(
    buttons: List<ButtonData>,
    selectedIndex: Int,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (buttons.isEmpty()) {
        Box(modifier = modifier)
        return
    }

    val safeSelectedIndex = selectedIndex.coerceIn(0, buttons.size - 1)
    val circleRadius = 25.dp
    var barSize by remember { mutableStateOf<IntSize?>(null) }
    val density = LocalDensity.current

    val offsetStep = remember(barSize) {
        barSize?.let {
            it.width.toFloat() / (buttons.size * 2)
        } ?: 0f
    }

    val offset = offsetStep + safeSelectedIndex * 2 * offsetStep
    val circleRadiusPx = density.run { circleRadius.toPx().toInt() }

    val offsetTransition = updateTransition(offset, label = "offset transition")
    val animation = spring<Float>(dampingRatio = 0.9f, stiffness = Spring.StiffnessLow)

    val cutoutOffset by offsetTransition.animateFloat(
        transitionSpec = {
            if (initialState == 0f) snap() else animation
        },
        label = "cutout offset"
    ) { it }

    val circleOffset by offsetTransition.animateIntOffset(
        transitionSpec = {
            if (initialState == 0f) snap() else spring(animation.dampingRatio, animation.stiffness)
        },
        label = "circle offset"
    ) {
        IntOffset(it.toInt() - circleRadiusPx, -circleRadiusPx)
    }

    val barShape = remember(cutoutOffset.roundToInt()) {
        BarShape(
            offset = cutoutOffset,
            circleRadius = circleRadius,
            cornerRadius = 24.dp
        )
    }

    Box(modifier = modifier) {
        Circle(
            modifier = Modifier
                .offset { circleOffset }
                .zIndex(1f),
            color = MaterialTheme.colorScheme.surfaceTint,
            radius = circleRadius,
            button = buttons[safeSelectedIndex],
            iconColor = MaterialTheme.colorScheme.surface
        )

        Row(
            modifier = Modifier
                .onPlaced {
                    if (barSize == null) barSize = it.size
                }
                .graphicsLayer {
                    shape = barShape
                    clip = true
                }
                .fillMaxWidth()
                .height(56.dp)
                .background(MaterialTheme.colorScheme.onPrimaryContainer),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            buttons.forEachIndexed { index, button ->
                val isSelected = index == safeSelectedIndex
                NoRippleNavItem(
                    selected = isSelected,
                    onClick = { onItemClick(index) },
                    icon = {
                        val iconAlpha by animateFloatAsState(
                            targetValue = if (isSelected) 0f else 0.5f,
                            label = "Navbar item icon"
                        )
                        Icon(
                            imageVector = button.icon,
                            contentDescription = button.text,
                            modifier = Modifier.alpha(iconAlpha)
                        )
                    },
                    label = { Text(button.text, style = TextStyle(fontSize = 12.sp)) },
                    selectedColor = MaterialTheme.colorScheme.surface,
                    unselectedColor = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}

@Composable
fun NoRippleNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    selectedColor: Color,
    unselectedColor: Color
) {
    Column(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CompositionLocalProvider(LocalContentColor provides if (selected) selectedColor else unselectedColor) {
            icon()
            label()
        }
    }
}