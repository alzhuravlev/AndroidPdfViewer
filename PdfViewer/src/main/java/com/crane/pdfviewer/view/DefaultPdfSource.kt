package com.crane.pdfviewer.view

import android.graphics.*
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import java.io.File

class DefaultPdfSource(file: File) : PdfSource() {

    private var currentPage: PdfRenderer.Page? = null

    private var pdfRenderer =
        PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY))

    private fun openPage(pageIndex: Int) {
        if (currentPage?.index != pageIndex) {
            currentPage?.close()
            currentPage = pdfRenderer.openPage(pageIndex)
        }
    }

    override fun draw(pageIndex: Int, bitmap: Bitmap, bitmapClip: Rect, matrix: Matrix?) {
        openPage(pageIndex)
        val page = currentPage ?: return
        page.render(
            bitmap,
            bitmapClip,
            matrix,
            PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
        )
    }

    override fun release() {
        currentPage?.close()
        currentPage = null
    }

    override fun pageSize(pageIndex: Int, out: PointF) {
        openPage(pageIndex)
        val page = currentPage ?: return
        out.x = page.width.toFloat()
        out.y = page.height.toFloat()
    }

    override val pageCount: Int
        get() = pdfRenderer.pageCount
}