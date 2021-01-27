package com.crane.pdfviewer.render

import android.content.Context
import android.graphics.Canvas
import android.util.Log
import androidx.core.math.MathUtils
import kotlin.system.measureTimeMillis

/**
 * Created by azhuravlev on 1/19/2018.
 */

open class Renderer<P : Renderer<P>> @JvmOverloads constructor(
    protected val context: Context,
    protected val parent: P? = null,
    protected val manualDrawing: Boolean = false,
    protected val childApproxCount: Int = 10,
    val sync: Boolean = false
) : Broadcaster {

    final override var broadcaster: Broadcaster? = null
    private var children: Array<Renderer<*>?> = arrayOfNulls(childApproxCount)
    private var lastChildIndex: Int = 0

    private var lastTs: Long = 0

    var width: Float = 0.0f
        private set
    var height: Float = 0.0f
        private set

    private var state = -1
    private var pendingState = -1
    var stateTime: Float = 0.0f
        private set

    val isActive: Boolean
        get() = state != STATE_INACTIVE

    val isReady: Boolean
        get() = width != 0.0f && height != 0.0f

    init {

        this.lastChildIndex = -1

        parent?.let {
            parent.addChildRenderer(this)
            broadcaster = parent.broadcaster
        }

        setState(STATE_UNKNOWN)
    }

    fun addChildRenderer(child: Renderer<*>) {
        if (children.size <= lastChildIndex + 1) {
            val a = arrayOfNulls<Renderer<*>>(children.size + childApproxCount)
            System.arraycopy(children, 0, a, 0, children.size)
            children = a
        }
        children[++lastChildIndex] = child
    }

    protected open fun doUpdateSize(w: Float, h: Float) {}

    protected open fun doProcessInput(events: Array<InputController.Event?>, size: Int): Boolean {
        return false
    }

    protected open fun doUpdate(delta: Float) {}

    protected open fun doDraw(canvas: Canvas) {}

    private fun _updateSize(w: Float, h: Float) {
        if (width == w && height == h)
            return

        this.width = w
        this.height = h

        doUpdateSize(w, h)
        for (i in 0..lastChildIndex)
            children[i]?.updateSize(w, h)
    }

    fun updateSize(w: Float, h: Float) {
        if (sync)
            synchronized(this) {
                _updateSize(w, h)
            }
        else
            _updateSize(w, h)
    }

    private fun processInput(events: Array<InputController.Event?>, size: Int): Boolean {
        for (i in 0..lastChildIndex)
            if (children[i]?.processInput(events, size) == true)
                return true
        return doProcessInput(events, size)
    }

    private fun update(delta: Float) {
        if (pendingState != -1) {
            state = pendingState
            pendingState = -1
            stateTime = 0.0f
        }
        doUpdate(delta)
        stateTime += delta
        for (i in 0..lastChildIndex)
            children[i]?.update(delta)
    }

    private fun draw(canvas: Canvas) {
        doDraw(canvas)
        for (i in 0..lastChildIndex)
            if (children[i]?.manualDrawing != true)
                children[i]?.draw(canvas)
    }

    private fun _render(canvas: Canvas, delta: Float, inputController: InputController) {
        inputController.consumeEvents { events: Array<InputController.Event?>, size: Int ->
            processInput(events, size)
        }

        update(delta)

        canvas.drawColor(0xffffffff.toInt())
        canvas.save()
        draw(canvas)
        canvas.restore()
    }

    fun render(canvasProvider: CanvasProvider, inputController: InputController) {

        var allTs = 0L
        var renderTs = 0L
        var drawUnlockTs = 0L
        var delta = 0.0f
        var canvasHA = false

        allTs = measureTimeMillis {

            delta = MathUtils.clamp((System.currentTimeMillis() - lastTs) * 1e-3f, 0.0f, 1.0f)
            lastTs = System.currentTimeMillis()

            val canvas: Canvas? = canvasProvider.lock()

            canvas?.let {

                canvasHA = canvas.isHardwareAccelerated

                if (sync)
                    synchronized(this) {
                        renderTs = measureTimeMillis { _render(canvas, delta, inputController) }
                    }
                else
                    renderTs = measureTimeMillis { _render(canvas, delta, inputController) }

                drawUnlockTs = measureTimeMillis {
                    canvasProvider.unlock(canvas)
                }
            }

        }

        if (DEBUG) {
            Log.d(
                TAG,
                "frame all=$allTs; r=$renderTs; dunlock=$drawUnlockTs delta=$delta; canvasHA=${canvasHA}"
            )
        }
    }

    fun getState(): Int {
        return state
    }

    protected open fun doSetState(newState: Int) {}

    fun setState(newState: Int) {
        doSetState(newState)
        this.pendingState = newState
    }

    protected fun doProcessBroadcast(broadcastData: BroadcastData) {}

    override fun onProcessBroadcast(broadcastData: BroadcastData) {
        for (i in 0..lastChildIndex)
            children[i]?.onProcessBroadcast(broadcastData)
        doProcessBroadcast(broadcastData)
    }

    override fun sendBroadcast(broadcastData: BroadcastData) {
        broadcaster?.sendBroadcast(broadcastData)
    }

    companion object {

        const val STATE_UNKNOWN = 0
        const val STATE_STARTING = 1
        const val STATE_ACTIVE = 2
        const val STATE_FINISHING = 3
        const val STATE_INACTIVE = 4

        const val LAST_STATE_INDEX = STATE_INACTIVE

        private const val DEBUG = false
        private const val TAG = "Renderer"

    }
}