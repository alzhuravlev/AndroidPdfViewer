package com.crane.pdfviewer.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.crane.pdfviewer.render.BroadcastData
import com.crane.pdfviewer.render.RenderView
import java.io.File
import java.io.InputStream
import java.lang.IllegalStateException

class PdfView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val renderView = RenderView(context)

    init {
        addView(
            renderView,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        )
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) =
        if (childCount > 0)
            throw IllegalStateException("Unable to add views to this group! If you want to add some views above this put this view into FrameLayout.")
        else
            super.addView(child, index, params)

    override fun removeView(view: View?) =
        throw IllegalStateException("Unable to remove views from this group!")

    override fun removeAllViews() =
        throw IllegalStateException("Unable to remove views from this group!")

    fun loadPdf(file: File, defaultPageIndexToView: Int = -1) {
        renderView.setRenderer(PdfRenderer(context, file))
    }

    companion object {
        const val BROADCAST_LOAD_PDF = 0
    }
}