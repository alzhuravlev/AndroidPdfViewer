package com.crane.pdfviewer.render

import java.util.ArrayList

/**
 * Created by azhuravlev on 1/22/2018.
 */

class InputController(val sync: Boolean = false) {

    val eventsAccumulator = arrayOfNulls<Event?>(200)
    var eventsAccumulatorIdx = -1

    val eventsPool = ArrayList<Event>(200)

    enum class EventType {
        TAP,
        DBL_TAP,
        LONG_TAP,
        SCROLL,
        FLING,
        SCALE_BEGIN,
        SCALE,
        SCALE_END,
        DOWN,
        UP
    }

    class Event {
        var eventType: EventType? = null
        var x: Float = 0.0f
        var y: Float = 0.0f
        var scaleFactor: Float = 0.0f
        var x1: Float = 0.0f
        var y1: Float = 0.0f
        var eventTime: Long = 0
        var pointerCount: Int = 0
    }

    inline fun _consumeEvents(block: (events: Array<Event?>, size: Int) -> Unit) {
        if (eventsAccumulatorIdx != -1) {
            val size = eventsAccumulatorIdx + 1
            block(eventsAccumulator, size)
            for (i in 0..eventsAccumulatorIdx)
                eventsAccumulator[i]?.let {
                    eventsPool.add(it)
                }
            eventsAccumulatorIdx = -1
        }
    }

    inline fun consumeEvents(block: (events: Array<Event?>, size: Int) -> Unit) {
        if (sync)
            synchronized(this) {
                _consumeEvents(block)
            }
        else
            _consumeEvents(block)
    }

    private fun _appendEvent(e: Event) {
        if (eventsAccumulatorIdx + 1 < eventsAccumulator.size)
            eventsAccumulator[++eventsAccumulatorIdx] = e
    }

    private fun appendEvent(e: Event) {
        if (sync)
            synchronized(this) {
                _appendEvent(e)
            }
        else
            _appendEvent(e)
    }

    private fun obtain(): Event {
        return if (eventsPool.size == 0) Event() else eventsPool.removeAt(eventsPool.size - 1)
    }

    fun postScroll(dx: Float, dy: Float, currentX: Float, currentY: Float, pointerCount: Int) {
        val event = obtain()
        event.eventType = EventType.SCROLL
        event.x = dx
        event.y = dy
        event.x1 = currentX
        event.y1 = currentY
        event.pointerCount = pointerCount
        appendEvent(event)
    }

    fun postFling(x1: Float, y1: Float, vx: Float, vy: Float) {
        val event = obtain()
        event.eventType = EventType.FLING
        event.x = vx
        event.y = vy
        event.x1 = x1
        event.y1 = y1
        appendEvent(event)
    }

    fun postTap(x: Float, y: Float) {
        val event = obtain()
        event.eventType = EventType.TAP
        event.x = x
        event.y = y
        appendEvent(event)
    }

    fun postDoubleTap(x: Float, y: Float) {
        val event = obtain()
        event.eventType = EventType.DBL_TAP
        event.x = x
        event.y = y
        appendEvent(event)
    }

    fun postLongTap(x: Float, y: Float) {
        val event = obtain()
        event.eventType = EventType.LONG_TAP
        event.x = x
        event.y = y
        appendEvent(event)
    }

    fun postScale(x: Float, y: Float, scale: Float) {
        if (scale < 0.0f)
            return
        val event = obtain()
        event.eventType = EventType.SCALE
        event.x = x
        event.y = y
        event.scaleFactor = scale
        appendEvent(event)
    }

    fun postScaleBegin() {
        val event = obtain()
        event.eventType = EventType.SCALE_BEGIN
        appendEvent(event)
    }

    fun postScaleEnd() {
        val event = obtain()
        event.eventType = EventType.SCALE_END
        appendEvent(event)
    }

    fun postDown(x: Float, y: Float, eventTime: Long) {
        val event = obtain()
        event.eventType = EventType.DOWN
        event.x = x
        event.y = y
        event.eventTime = eventTime
        appendEvent(event)
    }

    fun postUp(x: Float, y: Float, eventTime: Long) {
        val event = obtain()
        event.eventType = EventType.UP
        event.x = x
        event.y = y
        event.eventTime = eventTime
        appendEvent(event)
    }
}
