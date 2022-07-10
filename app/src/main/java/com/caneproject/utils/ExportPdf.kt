package com.caneproject.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.print.PrintAttributes
import android.provider.MediaStore
import android.text.Layout
import com.caneproject.db.Data
import com.wwdablu.soumya.simplypdf.SimplyPdf
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.composers.properties.ImageProperties
import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties
import com.wwdablu.soumya.simplypdf.document.DocumentInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class ExportPdf(
    private val context: Context,
    private val inputList: MutableList<Data>,
    fileName: String,
) {
    private var simplyPdfDocument: SimplyPdfDocument? = null

    init {
        simplyPdfDocument = SimplyPdf.with(
            context,
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path,
                "${fileName}.pdf".replace("\\s".toRegex(), "").replace("/", ":").replace(":", "-")
            )
        )
            .colorMode(DocumentInfo.ColorMode.COLOR).paperSize(PrintAttributes.MediaSize.ISO_A4)
            .firstPageBackgroundColor(Color.WHITE)
            .paperOrientation(DocumentInfo.Orientation.PORTRAIT).build()
        toastShower(context, "Getting Ready To Send Pdf")
    }

    fun createContent() {
        val properties1 = TextProperties().apply {
            textColor = "#000000"
            textSize = 34
            alignment = Layout.Alignment.ALIGN_CENTER
        }
        val properties2 = TextProperties().apply {
            textColor = "#000000"
            textSize = 29
            alignment = Layout.Alignment.ALIGN_CENTER
        }
        val imageProperties = ImageProperties().apply {
            alignment = Composer.Alignment.CENTER
        }
        inputList.forEach {
            val bitmap =
                MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(it.uriString))
            simplyPdfDocument!!.image.drawBitmap(rotateImage(bitmap), imageProperties)
            bitmap.recycle()
            simplyPdfDocument?.text?.write(
                it.toString(),
                properties1
            )
            simplyPdfDocument?.text?.write(
                it.toStringForSecondPart(),
                properties2
            )
            if (inputList.indexOf(it) != inputList.lastIndex)
                simplyPdfDocument?.newPage()
        }
        finishDoc()
    }

    private fun rotateImage(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(90F)
        return Bitmap.createScaledBitmap(
            Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width,
                bitmap.height, matrix, true
            ), (bitmap.width * 0.7).toInt(),
            (bitmap.height * 0.9).toInt(), false
        )
    }

    private fun finishDoc() {
        CoroutineScope(Dispatchers.IO).launch {
            simplyPdfDocument?.finish()
        }
    }
}
