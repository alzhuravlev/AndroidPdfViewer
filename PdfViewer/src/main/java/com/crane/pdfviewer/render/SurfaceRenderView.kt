package com.crane.pdfviewer.render

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

class SurfaceRenderView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    SurfaceView(context, attrs, defStyleAttr), Broadcaster {

    private var renderer: Renderer<*> = Renderer(context, null, false, 10, true)
    private var canvasProvider: CanvasProvider? = null
    private var renderThread: RenderThread? = null
    private val inputController = InputController(true)

    init {
        renderer.broadcaster = this
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                renderer.updateSize(width.toFloat(), height.toFloat())
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                stopDrawing()
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                canvasProvider = SurfaceViewCanvasProvider(holder)
                restartDrawing()
            }
        })
    }

    override var broadcaster: Broadcaster?
        get() = this
        set(value) {}

    open fun doProcessBroadcast(broadcastData: BroadcastData) {}

    override fun onProcessBroadcast(broadcastData: BroadcastData) {
        doProcessBroadcast(broadcastData)
        renderer.onProcessBroadcast(broadcastData)
        BroadcastData.release(broadcastData)
    }

    override fun sendBroadcast(broadcastData: BroadcastData) {
        onProcessBroadcast(broadcastData)
    }

    fun setRenderer(renderer: Renderer<*>) {
        this.renderer.addChildRenderer(renderer)
        restartDrawing()
    }

    fun restartDrawing() {

        stopDrawing()

        val canvasProvider = canvasProvider
        canvasProvider ?: return

        renderer.updateSize(width.toFloat(), height.toFloat())

        renderThread = RenderThread(canvasProvider, renderer, inputController)
        renderThread?.start()
    }

    fun stopDrawing() {
        renderThread?.let {
            it.running = false
            try {
                it.join()
            } catch (e: InterruptedException) {
            }
        }
        renderThread = null
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d(TAG, "onAttachedToWindow")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d(TAG, "onDetachedFromWindow")
    }

    companion object {
        private const val TAG = "SurfaceRenderView"
    }
}