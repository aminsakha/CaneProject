//package com.caneproject
//
//import android.os.Bundle
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AppCompatActivity
//import com.otaliastudios.cameraview.CameraLogger
//import com.otaliastudios.cameraview.CameraView
//import com.otaliastudios.cameraview.filter.Filters
//import com.otaliastudios.cameraview.frame.FrameProcessor
//
//class Temp : AppCompatActivity(), View.OnClickListener, OptionView.Callback {
//
//    companion object {
//        private val LOG = CameraLogger.create("DemoApp")
//        private const val USE_FRAME_PROCESSOR = false
//        private const val DECODE_BITMAP = false
//    }
//
//    private val camera: CameraView by lazy { findViewById(R.id.camera) }
//    private val controlPanel: ViewGroup by lazy { findViewById(R.id.controls) }
//    private var captureTime: Long = 0
//
//    private var currentFilter = 0
//    private val allFilters = Filters.values()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_camera)
//        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE)
//        camera.setLifecycleOwner(this)
//        camera.addCameraListener(Listener())
//        if (USE_FRAME_PROCESSOR) {
//            camera.addFrameProcessor(object : FrameProcessor {
//                private var lastTime = System.currentTimeMillis()
//                override fun process(frame: Frame) {
//                    val newTime = frame.time
//                    val delay = newTime - lastTime
//                    lastTime = newTime
//                    LOG.v("Frame delayMillis:", delay, "FPS:", 1000 / delay)
//                    if (DECODE_BITMAP) {
//                        if (frame.format == ImageFormat.NV21
//                            && frame.dataClass == ByteArray::class.java) {
//                            val data = frame.getData<ByteArray>()
//                            val yuvImage = YuvImage(data,
//                                frame.format,
//                                frame.size.width,
//                                frame.size.height,
//                                null)
//                            val jpegStream = ByteArrayOutputStream()
//                            yuvImage.compressToJpeg(Rect(0, 0,
//                                frame.size.width,
//                                frame.size.height), 100, jpegStream)
//                            val jpegByteArray = jpegStream.toByteArray()
//                            val bitmap = BitmapFactory.decodeByteArray(jpegByteArray,
//                                0, jpegByteArray.size)
//                            bitmap.toString()
//                        }
//                    }
//                }
//            })
//        }
//        findViewById<View>(R.id.edit).setOnClickListener(this)
//        findViewById<View>(R.id.capturePicture).setOnClickListener(this)
//        findViewById<View>(R.id.capturePictureSnapshot).setOnClickListener(this)
//    }
//
//
//    private inner class Listener : CameraListener() {
//        override fun onCameraOpened(options: CameraOptions) {
//            val group = controlPanel.getChildAt(0) as ViewGroup
//            for (i in 0 until group.childCount) {
//                val view = group.getChildAt(i) as OptionView<*>
//                view.onCameraOpened(camera, options)
//            }
//        }
//
//        override fun onCameraError(exception: CameraException) {
//            super.onCameraError(exception)
//            message("Got CameraException #" + exception.reason, true)
//        }
//
//        override fun onPictureTaken(result: PictureResult) {
//            super.onPictureTaken(result)
//            if (camera.isTakingVideo) {
//                message("Captured while taking video. Size=" + result.size, false)
//                return
//            }
//
//            // This can happen if picture was taken with a gesture.
//            val callbackTime = System.currentTimeMillis()
//            if (captureTime == 0L) captureTime = callbackTime - 300
//            LOG.w("onPictureTaken called! Launching activity. Delay:", callbackTime - captureTime)
//            PicturePreviewActivity.pictureResult = result
//            val intent = Intent(this@Temp, PicturePreviewActivity::class.java)
//            intent.putExtra("delay", callbackTime - captureTime)
//            startActivity(intent)
//            captureTime = 0
//            LOG.w("onPictureTaken called! Launched activity.")
//        }
//
//        override fun onExposureCorrectionChanged(newValue: Float, bounds: FloatArray, fingers: Array<PointF>?) {
//            super.onExposureCorrectionChanged(newValue, bounds, fingers)
//            message("Exposure correction:$newValue", false)
//        }
//
//        override fun onZoomChanged(newValue: Float, bounds: FloatArray, fingers: Array<PointF>?) {
//            super.onZoomChanged(newValue, bounds, fingers)
//            message("Zoom:$newValue", false)
//        }
//    }
//
//    override fun onClick(view: View) {
//        when (view.id) {
//            R.id.edit -> edit()
//            R.id.capturePicture -> capturePicture()
//            R.id.capturePictureSnapshot -> capturePictureSnapshot()
//            R.id.captureVideo -> captureVideo()
//            R.id.captureVideoSnapshot -> captureVideoSnapshot()
//            R.id.toggleCamera -> toggleCamera()
//            R.id.changeFilter -> changeCurrentFilter()
//        }
//    }
//
//    private fun capturePicture() {
//        if (camera.mode == Mode.VIDEO) return run {
//            message("Can't take HQ pictures while in VIDEO mode.", false)
//        }
//        if (camera.isTakingPicture) return
//        captureTime = System.currentTimeMillis()
//        message("Capturing picture...", false)
//        camera.takePicture()
//    }
//
//    private fun capturePictureSnapshot() {
//        if (camera.isTakingPicture) return
//        if (camera.preview != Preview.GL_SURFACE) return run {
//            message("Picture snapshots are only allowed with the GL_SURFACE preview.", true)
//        }
//        captureTime = System.currentTimeMillis()
//        message("Capturing picture snapshot...", false)
//        camera.takePictureSnapshot()
//    }
//
//
//    override fun <T : Any> onValueChanged(option: Option<T>, value: T, name: String): Boolean {
//        if (option is Option.Width || option is Option.Height) {
//            val preview = camera.preview
//            val wrapContent = value as Int == WRAP_CONTENT
//            if (preview == Preview.SURFACE && !wrapContent) {
//                message("The SurfaceView preview does not support width or height changes. " +
//                        "The view will act as WRAP_CONTENT by default.", true)
//                return false
//            }
//        }
//        option.set(camera, value)
//        BottomSheetBehavior.from(controlPanel).state = BottomSheetBehavior.STATE_HIDDEN
//        message("Changed " + option.name + " to " + name, false)
//        return true
//    }
//}