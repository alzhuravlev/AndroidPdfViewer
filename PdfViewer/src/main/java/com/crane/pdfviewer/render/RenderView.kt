package com.crane.pdfviewer.render

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * Created by azhuravlev on 1/17/2018.
 */
class RenderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr), GestureDetector.OnGestureListener, Broadcaster {

    private val gestureDetector: GestureDetector
    private val gestureDetectorLongPress: GestureDetector

    private var renderer: Renderer<*> = Renderer(context, null, false, 10, false)
    private val inputController = InputController()

    private val viewCanvasProvider: ViewCanvasProvider = ViewCanvasProvider()

    override var broadcaster: Broadcaster?
        get() = this
        set(broadcaster) {}

    init {
        renderer.broadcaster = this

        gestureDetector = GestureDetector(getContext(), this)
        gestureDetector.setIsLongpressEnabled(false)
        gestureDetectorLongPress =
            GestureDetector(getContext(), object : GestureDetector.SimpleOnGestureListener() {
                override fun onLongPress(e: MotionEvent) {
                    this@RenderView.onLongPress(e)
                }

                override fun onDoubleTap(e: MotionEvent): Boolean {
                    inputController.postDoubleTap(e.x, e.y)
                    return true
                }
            })
    }

    fun setRenderer(renderer: Renderer<*>) {
        this.renderer.addChildRenderer(renderer)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.renderer.updateSize(measuredWidth.toFloat(), measuredHeight.toFloat())
        invalidate()
    }

    override fun hasOverlappingRendering(): Boolean {
        return false
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    @SuppressLint("MissingSuperCall")
    override fun draw(canvas: Canvas) {
        viewCanvasProvider.canvas = canvas
        renderer.render(viewCanvasProvider, inputController)
        invalidate()
    }

    protected fun onExtraCanvasDrawn(extraCanvas: Canvas) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        gestureDetectorLongPress.onTouchEvent(event)
        if (event.action == MotionEvent.ACTION_UP)
            inputController.postUp(event.x, event.y, event.eventTime)
        return true
    }

    override fun onDown(e: MotionEvent): Boolean {
        if (DEBUG)
            Log.d(TAG, "onDown")
        inputController.postDown(e.x, e.y, e.eventTime)
        return true
    }

    override fun onShowPress(e: MotionEvent) {
        if (DEBUG)
            Log.d(TAG, "onShowPress")
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        if (DEBUG)
            Log.d(TAG, "onSingleTapUp")
        inputController.postTap(e.x, e.y)
        return false
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if (DEBUG)
            Log.d(TAG, "onScroll $distanceX $distanceY")
        inputController.postScroll(distanceX, distanceY, e2.x, e2.y, e2.pointerCount)
        return true
    }

    override fun onLongPress(e: MotionEvent) {
        if (DEBUG)
            Log.d(TAG, "onLongPress")
        inputController.postLongTap(e.x, e.y)
    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (DEBUG)
            Log.d(TAG, "onFling $velocityX $velocityY")
        if (e2.pointerCount == 1)
            inputController.postFling(e1.x, e1.y, velocityX, velocityY)
        return true
    }

    open fun doProcessBroadcast(broadcastData: BroadcastData) {}

    override fun onProcessBroadcast(broadcastData: BroadcastData) {
        doProcessBroadcast(broadcastData)
        renderer?.onProcessBroadcast(broadcastData)
        BroadcastData.release(broadcastData)
    }

    override fun sendBroadcast(broadcastData: BroadcastData) {
        onProcessBroadcast(broadcastData)
    }

    companion object {
        private const val DEBUG = true
        private const val TAG = "MyRenderView"
    }
}
