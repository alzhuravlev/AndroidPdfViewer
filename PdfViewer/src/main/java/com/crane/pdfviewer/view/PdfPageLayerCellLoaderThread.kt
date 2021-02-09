package com.crane.pdfviewer.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.core.util.Pools
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.LinkedBlockingQueue
import kotlin.random.Random

internal class PdfPageLayerCellLoaderThread : PdfPageLayerCellLoader() {

    private class DrawThread : Thread() {
        val queue = LinkedBlockingQueue<PdfPageLayerCell>()
        private var running = false

        init {
            name = "Cell Drawing Thread"
        }

        override fun toString(): String {
            return super.toString()
        }

        override fun run() {
            while (running) {
                val cell = queue.take()
                val b = cell.bitmap ?: continue
                val c = Canvas(b)
                c.drawColor(0xffffffff.toInt())
                cell.pdfSource.draw(
                    cell.pageIndex,
                    b,
                    cell.bitmapClip,
                    cell.bitmapMatrix
                )
//                            c.drawColor((random.nextInt() or 0xff000000.toInt() and 0x55ffffff.toInt()))
                cell.isBitmapReady = true
            }
        }

        fun startDrawing() {
            running = true
            start()
        }

        fun stopDrawing() {
            running = false
        }
    }

    private val thread = DrawThread()

    init {
        thread.startDrawing()
    }

    override fun load(cell: PdfPageLayerCell) {
        if (!thread.queue.contains(cell) && cell.bitmap == null) {
            cell.isBitmapReady = false
            cell.bitmap = acquire()
            if (cell.bitmap != null)
                thread.queue.offer(cell)
        }
    }

    override fun release(cell: PdfPageLayerCell) {
        thread.queue.remove(cell)
        if (cell.bitmap != null) {
            release(cell.bitmap!!)
            cell.bitmap = null
        }
    }

    override fun close() {
        thread.stopDrawing()
    }
}