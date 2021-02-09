package com.crane.pdfviewer.view

import android.graphics.*
import androidx.core.util.Pools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.random.Random

class PdfPageLayerCell(
    internal val pdfSource: PdfSource,
    internal val pageIndex: Int,
    internal val cellPositionWorld: RectF,
    internal val bitmapClip: Rect,
    internal val bitmapMatrix: Matrix,
) {

    companion object {
        private val paint = Paint()
        private val loader = PdfPageLayerCellLoaderThread()

        init {
            paint.style = Paint.Style.FILL
            paint.color = 0xffffffff.toInt()
        }

    }

    internal var bitmap: Bitmap? = null
    internal var isBitmapReady = false
    internal var isCellOnScreen: Boolean = false
        private set

    fun release() {
        loader.release(this)
    }

    fun updateCell(delta: Float, viewRectWorld: RectF) {
        isCellOnScreen = cellPositionWorld.intersects(
            viewRectWorld.left,
            viewRectWorld.top,
            viewRectWorld.right,
            viewRectWorld.bottom
        )
        if (!isCellOnScreen)
            release()
    }

    fun drawCell(canvas: Canvas) {

        if (!isCellOnScreen)
            return

        PdfRenderer.drawCallCount++

        loader.load(this)

        if (bitmap != null && isBitmapReady) {
            canvas.drawBitmap(bitmap!!, bitmapClip, cellPositionWorld, paint)
            PdfRenderer.drawBitmapCount++
        }
    }
}