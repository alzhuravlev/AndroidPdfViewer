package com.crane.pdfviewer.render

import java.util.HashSet

class BroadcastData private constructor() {
    var type: Int = 0
    var arg0: Any? = null
    var arg1: Any? = null
    var arg2: Any? = null
    var arg3: Any? = null
    var arg4: Any? = null
    var arg5: Any? = null
    var arg6: Any? = null
    var arg7: Any? = null

    companion object {

        private val broadcastDataPool = HashSet<BroadcastData>()

        fun release(broadcastData: BroadcastData) {
            broadcastDataPool.add(broadcastData)
        }

        @JvmOverloads
        fun obtain(
            type: Int,
            arg0: Any? = null,
            arg1: Any? = null,
            arg2: Any? = null,
            arg3: Any? = null,
            arg4: Any? = null,
            arg5: Any? = null,
            arg6: Any? = null,
            arg7: Any? = null
        ): BroadcastData {
            val data: BroadcastData
            if (broadcastDataPool.size == 0)
                data = BroadcastData()
            else {
                val iterator = broadcastDataPool.iterator()
                data = iterator.next()
                iterator.remove()
            }
            data.type = type
            data.arg0 = arg0
            data.arg1 = arg1
            data.arg2 = arg2
            data.arg3 = arg3
            data.arg4 = arg4
            data.arg5 = arg5
            data.arg6 = arg6
            data.arg7 = arg7
            return data
        }
    }
}
