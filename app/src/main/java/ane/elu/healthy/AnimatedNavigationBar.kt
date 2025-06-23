package ane.elu.healthy

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
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
import kotlin.math.abs
import kotlin.math.roundToInt

@SuppressLint("UnusedTransitionTargetStateParameter")
@OptIn(ExperimentalAnimationApi::class)
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
        barSize?.let { it.width.toFloat() / (buttons.size * 2) } ?: 0f
    }

    val targetOffset = offsetStep + safeSelectedIndex * 2 * offsetStep
    val circleRadiusPx = with(density) { circleRadius.toPx().toInt() }

    var prevSelectedIndex by remember { mutableIntStateOf(safeSelectedIndex) }
    LaunchedEffect(safeSelectedIndex) {
        prevSelectedIndex = safeSelectedIndex
    }
    val indexDistance = abs(safeSelectedIndex - prevSelectedIndex)

    val transition = updateTransition(targetState = targetOffset, label = "NavigationBarTransition")
    val animatedOffset by transition.animateFloat(
        transitionSpec = {
            val stiffness = when (indexDistance) {
                0 -> Spring.StiffnessLow
                1 -> Spring.StiffnessMedium
                2 -> Spring.StiffnessMediumLow
                else -> Spring.StiffnessVeryLow
            }
            val damping = when (indexDistance) {
                0 -> 1.5f
                1 -> 1.2f
                2 -> 0.9f
                else -> 0.75f
            }
            spring(dampingRatio = damping, stiffness = stiffness)
        },
        label = "AnimatedOffset"
    ) { it }

    val circleOffset = IntOffset(animatedOffset.roundToInt() - circleRadiusPx, -circleRadiusPx)

    val barShape = remember(animatedOffset.roundToInt()) {
        BarShape(
            offset = animatedOffset,
            circleRadius = circleRadius,
            cornerRadius = 0.dp
        )
    }

    Box(modifier = modifier) {
        val iconScale by transition.animateFloat(
            transitionSpec = { spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessMedium) },
            label = "IconScale"
        ) { 1f }

        Circle(
            modifier = Modifier
                .offset { circleOffset }
                .zIndex(1f)
                .graphicsLayer {
                    scaleX = iconScale
                    scaleY = iconScale
                },
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
                .background(MaterialTheme.colorScheme.primary),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            buttons.forEachIndexed { index, button ->
                val isSelected = index == safeSelectedIndex
                val iconAlpha by animateFloatAsState(
                    targetValue = if (isSelected) 0f else 0.5f,
                    animationSpec = tween(durationMillis = 350)
                )
                val labelAlpha by animateFloatAsState(
                    targetValue = if (isSelected) 0f else 1f,
                    animationSpec = tween(durationMillis = 350)
                )

                NoRippleNavItem(
                    selected = isSelected,
                    onClick = { onItemClick(index) },
                    icon = {
                        Icon(
                            imageVector = button.icon,
                            contentDescription = button.text,
                            modifier = Modifier.alpha(iconAlpha)
                        )
                    },
                    label = {
                        Text(
                            button.text,
                            style = TextStyle(fontSize = 12.sp),
                            modifier = Modifier.alpha(labelAlpha)
                        )
                    },
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
            Spacer(modifier = Modifier.height(2.9.dp))
            label()
        }
    }
}