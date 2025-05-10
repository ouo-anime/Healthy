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

@Composable
fun AnimatedNavigationBar(
    buttons: List<ButtonData>,
    selectedIndex: Int,
    onItemClick: (Int) -> Unit,
    barColor: Color,
    circleColor: Color,
    selectedColor: Color,
    unselectedColor: Color,
    modifier: Modifier = Modifier
) {
    if (buttons.isEmpty()) {
        Box(modifier = modifier)
        return
    }

    val safeSelectedIndex = selectedIndex.coerceIn(0, buttons.size - 1)
    val circleRadius = 26.dp
    var barSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    val offsetStep = remember(barSize) {
        barSize.width.toFloat() / (buttons.size * 2)
    }

    val offset = offsetStep + safeSelectedIndex * 2 * offsetStep
    val circleRadiusPx = density.run { circleRadius.toPx().toInt() }

    val offsetTransition = updateTransition(offset, label = "offset transition")
    val animation = spring<Float>(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow)

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

    val barShape = remember(cutoutOffset) {
        BarShape(
            offset = cutoutOffset,
            circleRadius = circleRadius,
            cornerRadius = 25.dp
        )
    }

    Box(modifier = modifier) {
        Circle(
            modifier = Modifier
                .offset { circleOffset }
                .zIndex(1f),
            color = circleColor,
            radius = circleRadius,
            button = buttons[safeSelectedIndex],
            iconColor = selectedColor
        )

        Row(
            modifier = Modifier
                .onPlaced { barSize = it.size }
                .graphicsLayer {
                    shape = barShape
                    clip = true
                }
                .fillMaxWidth()
                .height(56.dp)
                .background(barColor),
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
                            targetValue = if (isSelected) 0f else 1f,
                            label = "Navbar item icon"
                        )
                        Icon(
                            imageVector = button.icon,
                            contentDescription = button.text,
                            modifier = Modifier.alpha(iconAlpha)
                        )
                    },
                    label = { Text(button.text, style = TextStyle(fontSize = 12.sp)) },
                    selectedColor = selectedColor,
                    unselectedColor = unselectedColor
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