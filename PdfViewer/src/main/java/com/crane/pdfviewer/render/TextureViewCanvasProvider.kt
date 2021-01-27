package com.crane.pdfviewer.render

import android.graphics.Canvas
import android.view.TextureView

class TextureViewCanvasProvider(val textureView: TextureView) : CanvasProvider {

    override fun lock(): Canvas? {
        val canvas = textureView.lockCanvas()
        return canvas
    }

    override fun unlock(canvas: Canvas) {
        textureView.unlockCanvasAndPost(canvas)
    }
}