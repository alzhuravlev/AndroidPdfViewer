package com.crane.pdfviewer.example

import android.app.Application
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { thread, ex ->
            ex.printStackTrace(System.err)
            val dir = applicationContext.getExternalFilesDir(null)
            if (dir != null) {
                val fileName =
                    "error_" + SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS")
                        .format(Date()) + ".txt"
                val file = File(dir, fileName)
                try {
                    val writer =
                        PrintWriter(FileWriter(file))
                    ex.printStackTrace(writer)
                    writer.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}