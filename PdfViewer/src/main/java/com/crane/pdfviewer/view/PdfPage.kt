package com.crane.pdfviewer.view

import android.graphics.*

class PdfPage(
    val pageIndex: Int,
    val pageSize: PointF,
    val positionWorld: RectF
) {
    private val paint = Paint()
    val layers = mutableListOf<PdfPageLayer>()
    private var isPageOnScreen: Boolean = false

//    private val uiScope = CoroutineScope(Dispatchers.Main)
//    private val IO = Dispatchers.IO

    init {
        paint.style = Paint.Style.FILL
        paint.color = 0xffffffff.toInt()
        paint.setShadowLayer(10.0f, 0.0f, 0.0f, 0x33000000.toInt())
    }

    fun updatePage(delta: Float, viewRectWorld: RectF) {

        layers.removeAll {
            if (it.isReadyToRelease)
                it.release()
            it.isReadyToRelease
        }

        isPageOnScreen = positionWorld.intersects(
            viewRectWorld.left,
            viewRectWorld.top,
            viewRectWorld.right,
            viewRectWorld.bottom
        )

        for (layer in layers)
            layer.updateLayer(delta, viewRectWorld)
    }

    fun drawPage(canvas: Canvas) {
        if (!isPageOnScreen)
            return

        canvas.drawRect(positionWorld, paint)

        for (layer in layers)
            layer.drawLayer(canvas)
    }

    fun scaleChanged(pdfSource: PdfSource, matrix: Matrix, matrixInv: Matrix) {
        if (isPageOnScreen) {
            for (layer in layers)
                layer.releasePending()
        } else {
            for (layer in layers)
                layer.release()
            layers.clear()
        }
        layers += PdfPageLayer(pdfSource, pageIndex, positionWorld, matrix, matrixInv)
    }
}
