package com.crane.pdfviewer.render

import android.graphics.Canvas

interface CanvasProvider {
    fun lock(): Canvas?
    fun unlock(canvas: Canvas)
}