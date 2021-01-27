package com.crane.pdfviewer.render

interface Broadcaster {

    var broadcaster: Broadcaster?

    fun onProcessBroadcast(broadcastData: BroadcastData)

    fun sendBroadcast(broadcastData: BroadcastData)
}
