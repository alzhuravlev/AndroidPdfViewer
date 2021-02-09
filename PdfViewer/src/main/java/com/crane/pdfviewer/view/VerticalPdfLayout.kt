package com.crane.pdfviewer.view

import android.graphics.PointF
import android.graphics.RectF
import kotlin.math.max
import kotlin.math.min

class VerticalPdfLayout(pdfSource: PdfSource) : PdfLayout(pdfSource) {

    companion object {
        val SPACE = 30.0f // space between pages in 1/72" pdf points
    }

    override fun layout() {

        pages.clear()

        var y = SPACE

        for (i in 0 until pdfSource.pageCount) {
            val pageSize = PointF()
            pdfSource.pageSize(i, pageSize)

            val rect = RectF(0.0f, 0.0f, pageSize.x, pageSize.y)

            rect.offset(-rect.width() / 2.0f, y)

            y += rect.height()
            y += SPACE

            contentPositionWorld.left = min(contentPositionWorld.left, rect.left - SPACE)
            contentPositionWorld.right = max(contentPositionWorld.right, rect.right + SPACE)

            pages += PdfPage(i, pageSize, rect)
        }

        contentPositionWorld.top = 0.0f
        contentPositionWorld.bottom = y
    }
}