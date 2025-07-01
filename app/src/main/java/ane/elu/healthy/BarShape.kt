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
    private val circleGap: Dp = 7.dp,
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
                val cutoutEdgeOffset = cutoutRadius * 1.6f
                val cutoutTopY = (cutoutCenterY - cutoutEdgeOffset).coerceAtLeast(0f)
                val cutoutBottomY = (cutoutCenterY + cutoutEdgeOffset).coerceAtMost(size.height)

                moveTo(size.width, 0f)
                lineTo(size.width, cutoutTopY)
                cubicTo(
                    size.width, cutoutCenterY - cutoutRadius,
                    size.width - cutoutRadius, cutoutCenterY - cutoutRadius,
                    size.width - cutoutRadius, cutoutCenterY
                )
                cubicTo(
                    size.width - cutoutRadius, cutoutCenterY + cutoutRadius,
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
                val cutoutCenterY = offset
                val cutoutTopY = (cutoutCenterY - cutoutEdgeOffset).coerceAtLeast(0f)
                val cutoutBottomY = (cutoutCenterY + cutoutEdgeOffset).coerceAtMost(size.height)

                // Top segment of the right stroke
                moveTo(size.width, 0f)
                lineTo(size.width, cutoutTopY)
                // Cutout curve (top side)
                cubicTo(
                    size.width, cutoutCenterY - cutoutRadius,
                    size.width - cutoutRadius, cutoutCenterY - cutoutRadius,
                    size.width - cutoutRadius, cutoutCenterY
                )
                // Cutout curve (bottom side)
                cubicTo(
                    size.width - cutoutRadius, cutoutCenterY + cutoutRadius,
                    size.width, cutoutCenterY + cutoutRadius,
                    size.width, cutoutBottomY
                )
                // Bottom segment of the right stroke
                lineTo(size.width, size.height)
            }
        }
    }
}