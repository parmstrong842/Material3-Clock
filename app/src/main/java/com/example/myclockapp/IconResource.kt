package com.example.myclockapp

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource

class IconResource private constructor(
    @DrawableRes private val resID: Int?,
    private val imageVectorFilled: ImageVector?,
    private val imageVectorOutlined: ImageVector?
) {

    @Composable
    fun asPainterResource(filled: Boolean = false): Painter {
        resID?.let {
            return painterResource(id = resID)
        }
        return rememberVectorPainter(image = if (filled) imageVectorFilled!! else imageVectorOutlined!!)
    }

    companion object {
        fun fromDrawableResource(@DrawableRes resID: Int): IconResource {
            return IconResource(resID, null, null)
        }

        fun fromImageVector(imageVectorFilled: ImageVector?, imageVectorOutlined: ImageVector?): IconResource {
            return IconResource(null, imageVectorFilled, imageVectorOutlined)
        }
    }
}