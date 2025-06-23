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

    private fun getPath(size: Size, density: Density): Path {
        val cutoutRadius = density.run { (circleRadius + circleGap).toPx() }
        val cornerRadiusPx = density.run { cornerRadius.toPx() }
        val cornerDiameter = cornerRadiusPx * 2

        return Path().apply {
            if (!isVertical) {
                val cutoutCenterX = offset
                val cutoutEdgeOffset = cutoutRadius * 1.6f
                val cutoutLeftX = (cutoutCenterX - cutoutEdgeOffset).coerceAtLeast(0f)
                val cutoutRightX = (cutoutCenterX + cutoutEdgeOffset).coerceAtMost(size.width)

                moveTo(0f, size.height)
                if (cutoutLeftX > cornerRadiusPx) {
                    arcTo(
                        rect = Rect(0f, 0f, cornerDiameter, cornerDiameter),
                        startAngleDegrees = 180f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                } else {
                    arcTo(
                        rect = Rect(0f, 0f, cutoutLeftX * 2, cutoutLeftX * 2),
                        startAngleDegrees = 180f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                }
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
                if (cutoutRightX < size.width - cornerRadiusPx) {
                    arcTo(
                        rect = Rect(size.width - cornerDiameter, 0f, size.width, cornerDiameter),
                        startAngleDegrees = -90f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                } else {
                    arcTo(
                        rect = Rect(
                            size.width - (size.width - cutoutRightX) * 2,
                            0f,
                            size.width,
                            (size.width - cutoutRightX) * 2
                        ),
                        startAngleDegrees = -90f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                }
                lineTo(size.width, size.height)
                close()
            } else {
                val cutoutCenterY = offset
                val cutoutEdgeOffset = cutoutRadius * 1.6f
                val cutoutTopY = (cutoutCenterY - cutoutEdgeOffset).coerceAtLeast(0f)
                val cutoutBottomY = (cutoutCenterY + cutoutEdgeOffset).coerceAtMost(size.height)

                moveTo(size.width, 0f)
                if (cutoutTopY > cornerRadiusPx) {
                    arcTo(
                        rect = Rect(size.width - cornerDiameter, 0f, size.width, cornerDiameter),
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                } else {
                    arcTo(
                        rect = Rect(size.width - cutoutTopY * 2, 0f, size.width, cutoutTopY * 2),
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                }
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
                if (cutoutBottomY < size.height - cornerRadiusPx) {
                    arcTo(
                        rect = Rect(size.width - cornerDiameter, size.height - cornerDiameter, size.width, size.height),
                        startAngleDegrees = 90f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                } else {
                    arcTo(
                        rect = Rect(
                            size.width - (size.height - cutoutBottomY) * 2,
                            size.height - (size.height - cutoutBottomY) * 2,
                            size.width,
                            size.height
                        ),
                        startAngleDegrees = 90f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                }
                lineTo(0f, size.height)
                lineTo(0f, 0f)
                close()
            }
        }
    }
}
