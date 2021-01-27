package com.crane.pdfviewer.render

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.util.Log
import android.view.TextureView

class TextureRenderView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    TextureView(context, attrs, defStyleAttr), TextureView.SurfaceTextureListener, Broadcaster {

    private var renderer: Renderer<*> = Renderer(context, null, false, 10, true)
    private var canvasProvider: CanvasProvider? = null
    private var renderThread: RenderThread? = null
    private val inputController = InputController(true)

    init {
        renderer.broadcaster = this
        surfaceTextureListener = this
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        renderer.updateSize(width.toFloat(), height.toFloat())
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        stopDrawing()
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        canvasProvider = TextureViewCanvasProvider(this)
        restartDrawing()
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
        private const val TAG = "TextureRenderView"
    }
}