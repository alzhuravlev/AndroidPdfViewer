package com.crane.pdfviewer.render

import android.graphics.Canvas

class ViewCanvasProvider() : CanvasProvider {

    var canvas: Canvas? = null

    override fun lock(): Canvas? {
        return canvas
    }

    override fun unlock(canvas: Canvas) {
    }
}