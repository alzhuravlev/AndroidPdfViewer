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

internal abstract class PdfPageLayerCellLoader {


    companion object {

        private val DEBUG = true
        private val TAG = "PdfPageLayerCellLoader"

        const val BITMAP_SIZE = 1000

        private val POOL_SIZE = 50
        private val pool = Pools.SimplePool<Bitmap>(POOL_SIZE)
        private var bitmapCounter = 0

        private val random = Random(1)

        fun acquire(): Bitmap? {
            if (DEBUG)
                Log.d(TAG, "bitmapCounter=$bitmapCounter")

            if (bitmapCounter >= POOL_SIZE)
                return null
            bitmapCounter++
            return pool.acquire() ?: Bitmap.createBitmap(
                BITMAP_SIZE,
                BITMAP_SIZE,
                Bitmap.Config.ARGB_8888
            )
        }

        fun release(bitmap: Bitmap) {
            bitmapCounter--
            pool.release(bitmap)
        }
    }

    abstract fun load(cell: PdfPageLayerCell)
    abstract fun release(cell: PdfPageLayerCell)
    abstract fun close()
}