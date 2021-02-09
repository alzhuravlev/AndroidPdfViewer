package com.crane.pdfviewer.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.crane.pdfviewer.view.PdfView
import java.io.File

class MainActivity : AppCompatActivity() {

    lateinit var pdfView: PdfView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pdfView = findViewById(R.id.pdf_view)

        pdfView.loadPdf(
            File(
                applicationContext.getExternalFilesDir(null),
//                "A17_FlightPlan.pdf"
                "1r41ai10801601_fong.pdf"
//                "file-example_PDF_500_kB.pdf"
//                "pdf-test.pdf"
            )
        )
    }
}