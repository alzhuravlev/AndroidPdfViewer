package com.crane.pdfviewer.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import com.crane.pdfviewer.render.BroadcastData
import com.crane.pdfviewer.render.Renderer
import java.io.File
import java.io.InputStream

class PdfRenderer(context: Context) : Renderer<Nothing>(context) {

    private val cache = mutableListOf<Bitmap>()

    private var firstVisiblePageIndex = 0

    private var currentScale: Float = 1.0f
    private var currentOffsetX: Float = 0.0f
    private var currentOffsetY: Float = 0.0f

    private var pdfRenderer: PdfRenderer? = null

    override fun doUpdateSize(w: Float, h: Float) {
        rebuildCache()
    }

    override fun doUpdate(delta: Float) {
    }

    override fun doDraw(canvas: Canvas) {
    }

    override fun doSetState(newState: Int) {
    }

    override fun doProcessBroadcast(broadcastData: BroadcastData) {
        when (broadcastData.type) {
            PdfView.BROADCAST_LOAD_PDF -> loadPdf(broadcastData.arg0 as File)
        }
    }

    private fun rebuildCache() {
        val pr = pdfRenderer
        if (pr == null) {
            cache.clear()
            return
        }

        for (i in 0..2) {
            val page = pr.openPage(i + firstVisiblePageIndex)
        }
    }

    private fun loadPdf(file: File) {
        pdfRenderer =
            PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY))

        firstVisiblePageIndex = 0
        currentOffsetX = 0.0f
        currentOffsetY = 0.0f
        currentScale = 1.0f

        rebuildCache()
    }
}