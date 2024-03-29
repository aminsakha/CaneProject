package com.caneproject.adaptors

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.caneproject.R
import com.caneproject.utils.db
import com.caneproject.db.Data
import com.caneproject.utils.loadImageForRecView
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

lateinit var myContext: Context
lateinit var myDataLIst: List<Data>

/**
 * this is the adaptor for analytic class
 */
class AdaptorForAnalytic(val dataList: List<Data>, val context: Context) :
    RecyclerView.Adapter<AdaptorForAnalytic.ViewHolder>() {
    init {
        myContext = context
        myDataLIst = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.data_structure_in_rec_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            if (dataList[position].trueColor.isNotEmpty())
                holder.fab.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor(dataList[position].trueColor))
            else
                holder.fab.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#FFFFE6"))
            holder.firstPartTV.text = dataList[position].toString()
            holder.secondPartTV.text =
                dataList[position].toStringForSecondPart()
            val textContent = position + 1
            holder.countTextBox.text = textContent.toString()
            loadImageForRecView(
                context,
                Uri.parse(dataList[position].uriString),
                holder.imageView
            )
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var firstPartTV: TextView = itemView.findViewById(R.id.firstPart)
        var countTextBox: TextView = itemView.findViewById(R.id.countTXTbox)
        var fab: FloatingActionButton = itemView.findViewById(R.id.selectColorBTN)
        var secondPartTV: TextView = itemView.findViewById(R.id.secondPart)
        var imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            fab.setOnClickListener {
                MaterialColorPickerDialog
                    .Builder(myContext)
                    .setTitle("Pick Theme")
                    .setColorShape(ColorShape.CIRCLE)
                    .setColors(arrayListOf("#FF0000", "#00FF00", "#0000FF", "#d0d0d9"))
                    .setColorSwatch(ColorSwatch._300)
                    .setTitle("choose the real color")
                    .setColorListener { _, colorHex ->
                        runBlocking {
                            db.dataDao()
                                .updateColor(colorHex, myDataLIst[adapterPosition].uriString)
                        }
                        fab.backgroundTintList =
                            ColorStateList.valueOf(Color.parseColor(colorHex))
                    }
                    .show()
            }
        }
    }
}
