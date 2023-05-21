package com.memo.core.design.utils

import android.graphics.BlurMaskFilter
import android.graphics.BlurMaskFilter.Blur
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.memoShadow(
    color: Color = Color.Black.copy(alpha = 0.4f),
    offsetX: Dp = 8.dp,
    offsetY: Dp = 8.dp,
    blurRadius: Dp = 24.dp,
): Modifier {
    return if (VERSION.SDK_INT >= VERSION_CODES.R) {
        then(
            composed {
                val paint = remember { Paint() }
                drawBehind {
                    drawIntoCanvas { canvas ->
                        val frameworkPaint = paint.asFrameworkPaint()
                        if (blurRadius != 0.dp) {
                            frameworkPaint.maskFilter = (BlurMaskFilter(blurRadius.toPx(), Blur.NORMAL))
                        }
                        frameworkPaint.color = color.toArgb()
                        val leftPixel = offsetX.toPx()
                        val topPixel = offsetY.toPx()
                        val rightPixel = size.width + topPixel
                        val bottomPixel = size.height + leftPixel
                        canvas.drawRect(
                            left = leftPixel,
                            top = topPixel,
                            right = rightPixel,
                            bottom = bottomPixel,
                            paint = paint,
                        )
                    }
                }
            }
        )
    } else {
        this
    }
}
