package com.crane.pdfviewer.view

import android.content.Context
import android.graphics.*
import android.util.Log
import com.crane.pdfviewer.BuildConfig
import com.crane.pdfviewer.render.BroadcastData
import com.crane.pdfviewer.render.InputController
import com.crane.pdfviewer.render.Renderer
import com.crane.pdfviewer.render.math.Mat4
import com.crane.pdfviewer.render.math.Vec4
import java.io.File
import kotlin.math.min

class PdfRenderer(context: Context, val pdfFile: File) : Renderer<Nothing>(context) {

    companion object {
        var drawPdfIntoBitmapCount = 0
        var drawBitmapCount = 0
        var drawCallCount = 0
        private val DEBUG = BuildConfig.DEBUG
        private val TAG = "PdfRenderer"
    }

    private val paint = Paint()

    init {
        paint.setColor(0xff331122.toInt())
    }

    private val currentMat = Mat4()
    private val targetMat = Mat4()

    private val matrix = Matrix()
    private val matrixInv = Matrix()

    private val viewRectScreen = RectF()
    private val viewRectWorld = RectF()

    private val currentVelocity = PointF()

    private val friction = 1.1f

    private lateinit var pdfLayout: PdfLayout

    private fun calcMatrix(contentRect: RectF) {
        val viewM = Mat4.lookAt(
            Vec4(
                0.0f, 0.0f,
                0.5f * contentRect.width() / (width / height) / Mat4.tan(3.1415926535f / 3f / 2f)
            ),
            Vec4(0.0f, 0.0f, 0.0f),
            Vec4(0.0f, 1.0f, 0.0f)
        )

        val projM = Mat4.perspectiveFov(
            3.1415926535f / 3f,
            width / height,
            1.0f,
            10.0f
        )
        val vpM = Mat4.dot(projM, viewM)
        //
        val screenM = Mat4()
        screenM.scale(width * 0.5f, height * 0.5f, 1.0f)
        screenM.translate(width * 0.5f, 0.0f, 0.0f)
        //
        Mat4.dot(screenM, vpM, currentMat)
        adjustBounds(currentMat)
        targetMat.set(currentMat)
        updateMatrix(0.0f)
    }

    private fun fling(vx: Float, vy: Float) {
        currentVelocity.x = vx * 0.1f
        currentVelocity.y = vy * 0.2f
    }

    private fun translate(dx: Float, dy: Float) {
        currentVelocity.set(0.0f, 0.0f)
        targetMat.translate(dx, dy, 0.0f)
    }

    private fun scale(factor: Float, pivotX: Float, pivotY: Float) {
        currentVelocity.set(0.0f, 0.0f)
        targetMat.translate(-pivotX, -pivotY, 0.0f)
        targetMat.scale(factor, factor, 1.0f)
        targetMat.translate(pivotX, pivotY, 0.0f)
    }

    private fun adjustBounds(mat: Mat4) {
        mat.mapRect(pdfLayout.contentPositionScreen, pdfLayout.contentPositionWorld)

        var dx = 0.0f
        var dy = 0.0f

        if (pdfLayout.contentPositionScreen.width() <= width) {
            dx = width * 0.5f - pdfLayout.contentPositionScreen.centerX()
        }
        if (pdfLayout.contentPositionScreen.width() > width) {
            if (pdfLayout.contentPositionScreen.right < width)
                dx = width - pdfLayout.contentPositionScreen.right
            if (pdfLayout.contentPositionScreen.left > 0.0f)
                dx = -pdfLayout.contentPositionScreen.left
        }
        if (pdfLayout.contentPositionScreen.height() <= height) {
            dy = height * 0.5f - pdfLayout.contentPositionScreen.centerY()
        }
        if (pdfLayout.contentPositionScreen.height() > height) {
            if (pdfLayout.contentPositionScreen.bottom < height)
                dy = height - pdfLayout.contentPositionScreen.bottom
            if (pdfLayout.contentPositionScreen.top > 0.0f)
                dy = -pdfLayout.contentPositionScreen.top
        }

        mat.translate(dx, dy, 0.0f)
    }

    private fun updateMatrix(delta: Float) {
        targetMat.translate(currentVelocity.x * delta, currentVelocity.y * delta, 0.0f)
        currentVelocity.x *= 1.0f / friction
        currentVelocity.y *= 1.0f / friction

        adjustBounds(targetMat)

        currentMat.interpolate(currentMat, targetMat, delta * 5.0f)
        currentMat.toMatrix(matrix)

        matrix.invert(matrixInv)
        matrixInv.mapRect(viewRectWorld, viewRectScreen)
    }

    override fun doUpdateSize(w: Float, h: Float) {
        pdfLayout = VerticalPdfLayout(DefaultPdfSource(pdfFile))
        pdfLayout.layout()
        viewRectScreen.set(0.0f, 0.0f, width, height)
        calcMatrix(pdfLayout.contentPositionWorld)
        scaleChanged()
    }

    override fun doProcessInput(events: Array<InputController.Event?>, size: Int): Boolean {
        for (i in 0 until size) {
            val e = events[i] ?: continue
            when (e.eventType) {
                InputController.EventType.SCROLL -> translate(-e.x, -e.y)
                InputController.EventType.SCALE -> scale(
                    e.scaleFactor + 0.6f * (1.0f - e.scaleFactor),
                    e.x,
                    e.y
                )
                InputController.EventType.SCALE_END -> scaleChanged()
                InputController.EventType.FLING -> fling(e.x, e.y)
                InputController.EventType.DOWN -> fling(0.0f, 0.0f)
            }
        }
        return true
    }

    private fun scaleChanged() {
        for (p in pdfLayout.pages)
            p.scaleChanged(pdfLayout.pdfSource, matrix, matrixInv)
    }

    override fun doUpdate(delta: Float) {
        updateMatrix(min(delta * 10.0f, 1.0f))
        for (p in pdfLayout.pages)
            p.updatePage(delta, viewRectWorld)
    }

    override fun doDraw(canvas: Canvas) {
        if (DEBUG) {
            drawCallCount = 0
            drawPdfIntoBitmapCount = 0
            drawBitmapCount = 0
        }

        canvas.concat(matrix)

//        canvas.drawRect(pdfLayout.contentRect, paint)

        for (p in pdfLayout.pages)
            p.drawPage(canvas)

        if (DEBUG) {
//            Log.d(
//                TAG,
//                "Draw drawCallCount=$drawCallCount; drawBitmapCount=$drawBitmapCount; drawPdfIntoBitmapCount=$drawPdfIntoBitmapCount"
//            )
//            Log.d(
//                TAG,
//                "Draw PdfPageLayerCell.bitmapCounter=${PdfPageLayerCell.bitmapCounter}; bitmaps=${pdfLayout.pages.sumBy { it.layers.sumBy { it.cells.count { it.bitmap != null } } }}"
//            )
        }
    }

    override fun doSetState(newState: Int) {
    }

    override fun doProcessBroadcast(broadcastData: BroadcastData) {
    }
}