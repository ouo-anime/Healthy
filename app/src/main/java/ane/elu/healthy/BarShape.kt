package ane.elu.healthy

import androidx.compose.ui.geometry.Rect
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
    private val cornerRadius: Dp,
    private val circleGap: Dp = 5.dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(getPath(size, density))
    }

    private fun getPath(size: Size, density: Density): Path {
        val cutoutRadius = density.run { (circleRadius + circleGap).toPx() }
        val cornerRadiusPx = density.run { cornerRadius.toPx() }
        val cornerDiameter = cornerRadiusPx * 2
        val cutoutCenterX = offset

        return Path().apply {
            val cutoutEdgeOffset = cutoutRadius * 1.5f
            val cutoutLeftX = (cutoutCenterX - cutoutEdgeOffset).coerceAtLeast(0f)
            val cutoutRightX = (cutoutCenterX + cutoutEdgeOffset).coerceAtMost(size.width)

            moveTo(x = 0f, y = size.height)
            if (cutoutLeftX > cornerRadiusPx) {
                arcTo(
                    rect = Rect(
                        left = 0f,
                        top = 0f,
                        right = cornerDiameter,
                        bottom = cornerDiameter
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
            } else {
                arcTo(
                    rect = Rect(
                        left = 0f,
                        top = 0f,
                        right = cutoutLeftX * 2,
                        bottom = cutoutLeftX * 2
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
            }
            lineTo(cutoutLeftX, 0f)
            cubicTo(
                x1 = cutoutCenterX - cutoutRadius,
                y1 = 0f,
                x2 = cutoutCenterX - cutoutRadius,
                y2 = cutoutRadius,
                x3 = cutoutCenterX,
                y3 = cutoutRadius
            )
            cubicTo(
                x1 = cutoutCenterX + cutoutRadius,
                y1 = cutoutRadius,
                x2 = cutoutCenterX + cutoutRadius,
                y2 = 0f,
                x3 = cutoutRightX,
                y3 = 0f
            )
            if (cutoutRightX < size.width - cornerRadiusPx) {
                arcTo(
                    rect = Rect(
                        left = size.width - cornerDiameter,
                        top = 0f,
                        right = size.width,
                        bottom = cornerDiameter
                    ),
                    startAngleDegrees = -90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
            } else {
                arcTo(
                    rect = Rect(
                        left = size.width - (size.width - cutoutRightX) * 2,
                        top = 0f,
                        right = size.width,
                        bottom = (size.width - cutoutRightX) * 2
                    ),
                    startAngleDegrees = -90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
            }
            lineTo(x = size.width, y = size.height)
            close()
        }
    }
}