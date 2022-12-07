package com.str.foundation.camerax

import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission_group.CAMERA
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.str.foundation.views.BaseFragment
import com.str.foundation.views.BaseScreen
import com.str.foundation.views.screenViewModel
import com.str.wardrobe.R
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress
import java.io.File
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

typealias CornersListener = () -> Unit

class CameraXFragment : BaseFragment() {

    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
//    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null

    private lateinit var safeContext: Context

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var viewFinder: PreviewView
    private lateinit var cameraCaptureButton: Button

    class Screen (
        val namedDress: NamedDress
    ): BaseScreen

    override val viewModel by screenViewModel<CameraXViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera_x, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewFinder = view.findViewById(R.id.viewFinder)
        cameraCaptureButton = view.findViewById(R.id.camera_capture_button)

        // Setup the listener for take photo button
        cameraCaptureButton.setOnClickListener { takePhoto() }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
//        OpenCVLoader.initDebug()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            preview = Preview.Builder().build()

            imageCapture = ImageCapture.Builder().build()

//            imageAnalyzer = ImageAnalysis.Builder().build().apply {
//                setAnalyzer(Executors.newSingleThreadExecutor(), CornerAnalyzer {
//                    val bitmap = viewFinder.bitmap
//                    val img = Mat()
//                    Utils.bitmapToMat(bitmap, img)
//                    bitmap?.recycle()
//                    // Do image analysis here if you need bitmap
//                })
//            }
            // Select back camera
            val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                preview?.setSurfaceProvider(viewFinder.surfaceProvider)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(safeContext))

    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create timestamped output file to hold the image
        val photoFile = viewModel.getPhotoFile(viewModel.currentDress)

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Setup image capture listener which is triggered after photo has
        // been taken
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(safeContext), object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                val msg = "Photo capture succeeded: $savedUri"
                Toast.makeText(safeContext, msg, Toast.LENGTH_SHORT).show()
                Log.d(TAG, msg)
                val fm = requireActivity().supportFragmentManager
                fm.popBackStack()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        isOffline = true
    }

    override fun onResume() {
        super.onResume()
        isOffline = false
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else activity?.filesDir!!
    }


    companion object {
        val TAG = "CameraXFragment"
        internal const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
        var isOffline = false // prevent app crash when goes offline
    }

}