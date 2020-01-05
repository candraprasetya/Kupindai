package com.kardusinfo.kupindai

import android.R.attr.key
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.wonderkiln.camerakit.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    lateinit var classifier: Classifier
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewResult.movementMethod = ScrollingMovementMethod()

        showResultButton.setOnClickListener {

            val mantab = textViewResult.text.toString()
            val fixMantab = mantab.split(",")

            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("TITLE", fixMantab[0])
            startActivity(intent)
        }

        var resultDialog = Dialog(this)
        val customWaitingView = LayoutInflater.from(this).inflate(R.layout.waiting_dialog, null)
        resultDialog.setCancelable(false)
        resultDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        resultDialog.setContentView(customWaitingView)

        // The Loader Holder is used due to a bug in the Avi Loader library
        val aviLoaderHolder = customWaitingView.findViewById<View>(R.id.aviLoaderHolderView)


        cameraView.addCameraKitListener(object : CameraKitEventListener {
            override fun onEvent(cameraKitEvent: CameraKitEvent) {}

            override fun onError(cameraKitError: CameraKitError) {}

            override fun onImage(cameraKitImage: CameraKitImage) {

                var bitmap = cameraKitImage.bitmap
                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)

                aviLoaderHolder.visibility = View.GONE
                val results = classifier.recognizeImage(bitmap)
                imageViewResult.setImageBitmap(bitmap)
                textViewResult.text = results.toString()
                resultDialog.setCancelable(false)
                resultDialog.cancel()
                showResultButton.visibility = View.VISIBLE
            }

            override fun onVideo(cameraKitVideo: CameraKitVideo) {}
        })

        btnSwitchCamera.setOnClickListener { cameraView.toggleFacing() }

        btnScanObject.setOnClickListener {
            cameraView.captureImage()
            resultDialog.show()
        }

        resultDialog.setOnDismissListener {
            aviLoaderHolder.visibility = View.VISIBLE
        }

        initTensorFlowAndLoadModel()

    }
    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        cameraView.stop()
        super.onPause()
    }
    override fun onDestroy() {
        super.onDestroy()
        executor.execute { classifier.close() }
    }


    private fun initTensorFlowAndLoadModel() {
        executor.execute {
            try {
                classifier = Classifier.create(
                    assets,
                    MODEL_PATH,
                    LABEL_PATH,
                    INPUT_SIZE)
                makeButtonVisible()
            } catch (e: Exception) {
                throw RuntimeException("Error initializing TensorFlow!", e)
            }
        }
    }


    private fun makeButtonVisible() {
        runOnUiThread { btnScanObject.visibility = View.VISIBLE }
    }
    companion object {
        private const val MODEL_PATH = "mobilenet_quant_v1_224.tflite"
        private const val LABEL_PATH = "labels.txt"
        private const val INPUT_SIZE = 224
    }
}
