package com.crane.pdfviewer.view

import android.graphics.*
import java.util.*

abstract class PdfLayout(
    val pdfSource: PdfSource
) {

    val contentPositionWorld = RectF()
    val contentPositionScreen = RectF()

    val pages = mutableListOf<PdfPage>()

    abstract fun layout()
}