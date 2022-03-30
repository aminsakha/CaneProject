package com.caneproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caneproject.utils.MakeConnectionToModulo
import com.otaliastudios.cameraview.CameraView


var receivedNotes: MutableList<Data>? = null
var adapter: HardWareModeAdaptor? = null
var makeConnectionToModulo: MakeConnectionToModulo? = null
var recyclerView: RecyclerView? = null
lateinit var camera: CameraView

class HardWareConnection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hard_ware_connection)
        recyclerView = findViewById(R.id.recView)
        camera = findViewById(R.id.camera)
        camera.setLifecycleOwner(this);
        initRecyclerView()
        if (makeConnectionToModulo?.socket == null || (makeConnectionToModulo?.isBluetoothOn == false)) {
            makeConnectionToModulo =
                MakeConnectionToModulo(this, this)
            makeConnectionToModulo!!.execute()
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
}
