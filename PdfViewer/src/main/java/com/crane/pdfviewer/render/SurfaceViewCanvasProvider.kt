package com.crane.pdfviewer.render

import android.graphics.Canvas
import android.os.Build
import android.view.SurfaceHolder

class SurfaceViewCanvasProvider(val surfaceHolder: SurfaceHolder) : CanvasProvider {

    override fun lock(): Canvas? {
        val canvas = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            surfaceHolder.lockHardwareCanvas()
        } else {
            surfaceHolder.lockCanvas()
        }
        return canvas
    }

    override fun unlock(canvas: Canvas) {
        surfaceHolder.unlockCanvasAndPost(canvas)
    }
}