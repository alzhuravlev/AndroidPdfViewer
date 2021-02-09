package com.crane.pdfviewer.view

import android.graphics.*
import androidx.core.util.Pools

abstract class PdfSource {
    abstract fun draw(pageIndex: Int, bitmap: Bitmap, bitmapClip: Rect, matrix: Matrix?)
    abstract fun release()
    abstract fun pageSize(pageIndex: Int, out: PointF)
    abstract val pageCount: Int
}