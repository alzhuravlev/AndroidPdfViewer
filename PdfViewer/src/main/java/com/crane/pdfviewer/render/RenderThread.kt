package com.crane.pdfviewer.render

class RenderThread(
    val canvasProvider: CanvasProvider,
    val renderer: Renderer<*>,
    val inputController: InputController
) :
    Thread("RenderThread-${this.hashCode()}") {

    @get:Synchronized
    @set:Synchronized
    private var _running: Boolean = true

    var running: Boolean
        get() = _running
        set(v) {
            _running = v
        }

    override fun run() {
        while (_running) {
            renderer.render(canvasProvider, inputController)
        }
    }

    companion object {
        private const val DEBUG = true
        private const val TAG = "MyRenderThread"
    }
}