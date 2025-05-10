package ane.elu.healthy

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun Circle(
    modifier: Modifier = Modifier,
    color: Color,
    radius: Dp,
    button: ButtonData,
    iconColor: Color
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(radius * 2)
            .clip(CircleShape)
            .background(color)
    ) {
        AnimatedContent(
            targetState = button.icon,
            label = "Bottom bar circle icon"
        ) { targetIcon ->
            Icon(
                imageVector = targetIcon,
                contentDescription = button.text,
                tint = iconColor
            )
        }
    }
}