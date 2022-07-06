package com.caneproject.utils

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.print.PrintAttributes
import android.provider.MediaStore
import com.wwdablu.soumya.simplypdf.SimplyPdf
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.properties.ImageProperties
import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties
import com.wwdablu.soumya.simplypdf.document.DocumentInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ExportPdf(
    private val context: Context,
) {
    private var simplyPdfDocument: SimplyPdfDocument? = null
    init {
        simplyPdfDocument = SimplyPdf.with(
            context,
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path,
                "a10.pdf"
            )
        )
            .colorMode(DocumentInfo.ColorMode.COLOR).paperSize(PrintAttributes.MediaSize.ISO_A4)
            .firstPageBackgroundColor(Color.WHITE)
            .paperOrientation(DocumentInfo.Orientation.PORTRAIT).build()
        toastShower(context, "Pdf Created")
    }

    private fun sampleText() {
        val properties = TextProperties().apply {
            textColor = "#000000"
        }
        for (i in 1..10) {
            properties.textSize = i * 4
            simplyPdfDocument?.text?.write(
                "The quick brown fox jumps over the hungry lazy dog. [Size: " + i * 4 + "]",
                properties
            )
        }
        simplyPdfDocument?.newPage()
        properties.textSize = 32
        properties.textColor = "#FF0000"
        simplyPdfDocument?.text?.write("Text with red color font", properties)
    }

    fun imageSample(vararg uri: Uri) {
        sampleText()
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri[0])
        simplyPdfDocument!!.image.drawBitmap(bitmap, ImageProperties())
        bitmap.recycle()
        sampleText()
        val bitmap2 = MediaStore.Images.Media.getBitmap(context.contentResolver, uri[1])
        simplyPdfDocument!!.image.drawBitmap(bitmap2, ImageProperties())
        bitmap2.recycle()
        finishDoc()
    }

    private fun finishDoc() {
        CoroutineScope(Dispatchers.IO).launch {
            simplyPdfDocument?.finish()
        }
    }
}
