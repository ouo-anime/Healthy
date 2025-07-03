package ane.elu.healthy

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class BarShape(
    private val offset: Float,
    private val circleRadius: Dp,
    private val circleGap: Dp = 8.dp,
    private val isVertical: Boolean = false
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(getPath(size, density))
    }

    fun getPath(size: Size, density: Density): Path {
        val cutoutRadius = density.run { (circleRadius + circleGap).toPx() }
        val cutoutCenterX = offset
        val cutoutEdgeOffset = cutoutRadius * 1.6f
        val cutoutLeftX = (cutoutCenterX - cutoutEdgeOffset).coerceAtLeast(0f)
        val cutoutRightX = (cutoutCenterX + cutoutEdgeOffset).coerceAtMost(size.width)

        return Path().apply {
            if (!isVertical) {
                moveTo(0f, size.height)
                lineTo(0f, 0f)
                lineTo(cutoutLeftX, 0f)
                cubicTo(
                    cutoutCenterX - cutoutRadius, 0f,
                    cutoutCenterX - cutoutRadius, cutoutRadius,
                    cutoutCenterX, cutoutRadius
                )
                cubicTo(
                    cutoutCenterX + cutoutRadius, cutoutRadius,
                    cutoutCenterX + cutoutRadius, 0f,
                    cutoutRightX, 0f
                )
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                close()
            } else {
                val cutoutCenterY = offset
                val cutoutEdgeOffset = cutoutRadius
                val cutoutTopY = (cutoutCenterY - cutoutEdgeOffset).coerceAtLeast(0f)
                val cutoutBottomY = (cutoutCenterY + cutoutEdgeOffset).coerceAtMost(size.height)

                moveTo(size.width, 0f)
                lineTo(size.width, cutoutTopY)
                cubicTo(
                    size.width, cutoutCenterY - cutoutRadius,
                    size.width - cutoutRadius * 0.8f, cutoutCenterY - cutoutRadius,
                    size.width - cutoutRadius * 0.8f, cutoutCenterY
                )
                cubicTo(
                    size.width - cutoutRadius * 0.8f, cutoutCenterY + cutoutRadius,
                    size.width, cutoutCenterY + cutoutRadius,
                    size.width, cutoutBottomY
                )
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                lineTo(0f, 0f)
                close()
            }
        }
    }

    fun getStrokePath(size: Size, density: Density): Path {
        val cutoutRadius = density.run { (circleRadius + circleGap).toPx() }
        val cutoutEdgeOffset = cutoutRadius * 1.6f

        return Path().apply {
            if (!isVertical) {
                val cutoutCenterX = offset
                val cutoutLeftX = (cutoutCenterX - cutoutEdgeOffset).coerceAtLeast(0f)
                val cutoutRightX = (cutoutCenterX + cutoutEdgeOffset).coerceAtMost(size.width)

                // Left segment of the top stroke
                moveTo(0f, 0f)
                lineTo(cutoutLeftX, 0f)
                // Cutout curve (left side)
                cubicTo(
                    cutoutCenterX - cutoutRadius, 0f,
                    cutoutCenterX - cutoutRadius, cutoutRadius,
                    cutoutCenterX, cutoutRadius
                )
                // Cutout curve (right side)
                cubicTo(
                    cutoutCenterX + cutoutRadius, cutoutRadius,
                    cutoutCenterX + cutoutRadius, 0f,
                    cutoutRightX, 0f
                )
                // Right segment of the top stroke
                lineTo(size.width, 0f)
            } else {
                // remove stroke
            }
        }
    }
}