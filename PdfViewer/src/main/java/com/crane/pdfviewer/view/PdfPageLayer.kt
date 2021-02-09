package com.crane.pdfviewer.view

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import androidx.core.util.Pools

class PdfPageLayer(
    pdfSource: PdfSource,
    pageIndex: Int,
    positionWorld: RectF,
    matrix: Matrix,
    matrixInv: Matrix
) {

    companion object {
        private const val POOL_SIZE = 1000
        private val pool = Pools.SimplePool<PdfPageLayerCell>(POOL_SIZE)
    }

    val cells = mutableListOf<PdfPageLayerCell>()
    private var isReleasePending = false
    private var stateTime: Float = 0.0f
    var isReadyToRelease = false
        private set

    init {
        val cellSizeScreen = PdfPageLayerCellLoader.BITMAP_SIZE.toFloat()
        val cellSizeWorld = matrixInv.mapRadius(cellSizeScreen)

        val scale = matrix.mapRadius(positionWorld.width()) / positionWorld.width()

        var left = positionWorld.left
        while (left < positionWorld.right) {

            val right = if (left + cellSizeWorld > positionWorld.right)
                positionWorld.right
            else
                left + cellSizeWorld
            val bw = matrix.mapRadius(right - left)

            if (bw < 1.0f) break

            var top = positionWorld.top
            while (top < positionWorld.bottom) {

                val bottom = if (top + cellSizeWorld > positionWorld.bottom)
                    positionWorld.bottom
                else
                    top + cellSizeWorld
                val bh = matrix.mapRadius(bottom - top)

                if (bh < 1.0f) break

                val cellPositionWorld = RectF(left, top, right, bottom)

                val bitmapClip = Rect(0, 0, bw.toInt(), bh.toInt())

                val cellPositionLocal = RectF(cellPositionWorld)
                cellPositionLocal.left -= positionWorld.left
                cellPositionLocal.right -= positionWorld.left
                cellPositionLocal.top -= positionWorld.top
                cellPositionLocal.bottom -= positionWorld.top

                val bitmapMatrix = Matrix()
                bitmapMatrix.postTranslate(
                    -cellPositionLocal.left,
                    -cellPositionLocal.top
                )
                bitmapMatrix.postScale(scale, scale)

                cells += PdfPageLayerCell(
                    pdfSource,
                    pageIndex,
                    cellPositionWorld,
                    bitmapClip,
                    bitmapMatrix
                )

                top += cellSizeWorld
            }
            left += cellSizeWorld
        }

    }

    fun updateLayer(delta: Float, viewRectWorld: RectF) {
        stateTime += delta
        if (isReleasePending && stateTime > 1.0f)
            isReadyToRelease = true

        for (cell in cells)
            cell.updateCell(delta, viewRectWorld)
    }

    fun drawLayer(canvas: Canvas) {
        for (cell in cells)
            cell.drawCell(canvas)
    }

    fun release() {
        for (cell in cells)
            cell.release()
    }

    fun releasePending() {
        isReleasePending = true
        isReadyToRelease = false
        stateTime = 0.0f
    }
}