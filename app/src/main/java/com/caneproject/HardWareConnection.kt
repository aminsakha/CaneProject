package com.caneproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caneproject.utils.MakeConnection
import com.caneproject.utils.saveToFile
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException

var receivedNotes: MutableList<Data>? = null
var adapter: HardWareModeAdaptor? = null
var makeConnection: MakeConnection? = null
class HardWareConnection : AppCompatActivity() {

    private val WRITE_CODE = 40
    private var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hard_ware_connection)
        recyclerView = findViewById(R.id.recView)
        val saveFileButton: FloatingActionButton = findViewById(R.id.saveFileButton)
        initRecyclerView()
        if (makeConnection == null) {
            makeConnection = MakeConnection(Activity(), this)
            makeConnection!!.execute()
        } else if (makeConnection?.isBluetoothOn == true) {
            makeConnection = MakeConnection(Activity(), this)
            makeConnection!!.execute()
        }
        saveFileButton.setOnClickListener { v ->
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            intent.type = "text/plain"
            startActivityForResult(
                intent, WRITE_CODE
            )
        }
    }

    private fun initRecyclerView() {
        receivedNotes = ArrayList()
        adapter = HardWareModeAdaptor(receivedNotes)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView?.addItemDecoration(dividerItemDecoration)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        receivedNotes?.let { saveToFile(this, it, requestCode, resultCode, data) }
        receivedNotes?.clear()
        adapter?.notifyDataSetChanged()
    }
}
fun sendSignal(number: String) {
    if (makeConnection?.socket != null) {
        try {
            makeConnection?.socket!!.outputStream.write(number.toByteArray())
        } catch (ignored: IOException) {
        }
    }
}