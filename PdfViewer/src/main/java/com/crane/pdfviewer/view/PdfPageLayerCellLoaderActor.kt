package com.crane.pdfviewer.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.core.util.Pools
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.LinkedBlockingQueue
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random


internal class PdfPageLayerCellLoaderActor : PdfPageLayerCellLoader(), CoroutineScope {

    private open class Msg(val cell: PdfPageLayerCell)
    private class DrawMsg(cell: PdfPageLayerCell) : Msg(cell)
    private class ReleaseMsg(cell: PdfPageLayerCell) : Msg(cell)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    private val job = Job()

    @ObsoleteCoroutinesApi
    private val c = actor<Msg>(context = job) {
        for (msg in channel)
            when (msg) {
                is DrawMsg -> {
                }
                is ReleaseMsg -> {
                }
            }
    }

    override fun load(cell: PdfPageLayerCell) {
    }

    override fun release(cell: PdfPageLayerCell) {
    }

    override fun close() {
        job.cancel()
    }
}