package com.walenkamp.spotdeal.Activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.*
import android.hardware.camera2.params.StreamConfigurationMap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.activity_scan.*
import java.util.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.walenkamp.spotdeal.BLL.CustomerLogic
import com.walenkamp.spotdeal.BLL.DealLogic
import com.walenkamp.spotdeal.BLL.OrderLogic
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackCustomer
import com.walenkamp.spotdeal.Interface.ICallbackDeal
import com.walenkamp.spotdeal.Interface.ICallbackOrder

class ScanActivity : AppCompatActivity() {
    // SupplierId
    private var supplierId: String = ""

    // Permission request code
    private val REQUEST_CAMERA_PERMISSION_RESULT = 0

    // CameraDevice instance
    private var cameraDevice: CameraDevice? = null

    // cameraId instance
    private var cameraId: String? = null

    // List of orientation possibilities
    private val orientations: SparseIntArray = SparseIntArray()

    // DealLogic instance
    private val dealLogic = DealLogic()

    // CustomerLogic instance
    private val customerLogic = CustomerLogic()

    // OrderLogic instance
    private val orderLogic = OrderLogic()

    // The size of the camera preview
    private lateinit var previewSize: Size

    // Capture request builder instance
    private lateinit var captureRequestBuilder: CaptureRequest.Builder

    // Background handler thread instance
    private var backgroundHandlerThread: HandlerThread? = null

    // Background handler instance
    private var backgroundHandler: Handler? = null

    // Specifies the options used for the barcode detector (chooses QR code format)
    private val options = FirebaseVisionBarcodeDetectorOptions.Builder().setBarcodeFormats(
        FirebaseVisionBarcode.FORMAT_QR_CODE
    ).build()

    // FirebaseVision instance of a barcodeDetector
    private val barcodeDetector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)


    init {
        orientations.append(Surface.ROTATION_0, 0)
        orientations.append(Surface.ROTATION_90, 90)
        orientations.append(Surface.ROTATION_180, 180)
        orientations.append(Surface.ROTATION_270, 270)
    }

    // Is called when CameraDevice changes state
    private var stateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            camera_view
            startPreview()
        }

        override fun onClosed(camera: CameraDevice) {
            super.onClosed(camera)
            camera.close()
            cameraDevice = null
        }

        override fun onDisconnected(camera: CameraDevice) {
            camera.close()
            cameraDevice = null        }

        override fun onError(camera: CameraDevice, error: Int) {
            camera.close()
            cameraDevice = null        }
    }

    // Handles lifecycle events on a TextureView
    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
            val originalBitmap = camera_view.bitmap

            val heightToCrop = top_view.height
            val widthToCrop = left_view.width

            val croppedBitmap1 = Bitmap.createBitmap(originalBitmap, widthToCrop, heightToCrop, border.width + widthToCrop, border.height + heightToCrop)

            val matrix = Matrix()
            matrix.preRotate(180F)

            val rotatedBitmap = Bitmap.createBitmap(croppedBitmap1, 0, 0, croppedBitmap1.width, croppedBitmap1.height, matrix, true)
            val croppedBitmap2 = Bitmap.createBitmap(rotatedBitmap, widthToCrop, heightToCrop, border.width, border.height, matrix, true)

            scan(croppedBitmap2)
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            return false
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            setUpCamera(width, height)
            connectCamera()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        supplierId = intent.getSerializableExtra(SUPPLIER_ID) as String

        fadeOutAndHide()
    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()
        if(camera_view.isAvailable) {
            setUpCamera(camera_view.width, camera_view.height)
            connectCamera()
            camera_view.surfaceTextureListener = surfaceTextureListener
        } else {
            camera_view.surfaceTextureListener = surfaceTextureListener
        }
    }

    override fun onPause() {
        closeCamera()
        stopBackgroundThread()
        camera_view.surfaceTextureListener = null
        super.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION_RESULT) {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(scan_view, R.string.camera_access_needed, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    // Connects the camera
    private fun connectCamera(){
        val cameraManager : CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraManager.openCamera(cameraId!!, stateCallback, backgroundHandler)

                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        Snackbar.make(scan_view, R.string.camera_access_needed, Snackbar.LENGTH_LONG).show()
                    }
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION_RESULT)
                }
            } else {
                cameraManager.openCamera(cameraId!!, stateCallback, backgroundHandler)
            }
        } catch (e : CameraAccessException) {
            e.printStackTrace()
        }
    }

    // Sets up the camera to the rear facing camera
    private fun setUpCamera(width: Int, height: Int) {
        val cameraManager : CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            for (id in cameraManager.cameraIdList) {
                val cameraCharacteristics = cameraManager.getCameraCharacteristics(id)
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue
                }
                // Holds the orientation of the device
                val deviceOrientation = windowManager.defaultDisplay.rotation

                // Holds the rotation of the sensor and device combined
                val totalRotation = sensorToDeviceRotation(cameraCharacteristics, deviceOrientation)

                // Checks if the rotation is set to landscape and is true if so
                val swapRotation: Boolean = totalRotation == 90 || totalRotation == 270

                // Holds the rotated width
                var rotatedWidth = width

                // Holds the rotated width
                var rotatedHeight = height

                // Swaps width and height if in landscape
                if (swapRotation) {
                    rotatedWidth = height
                    rotatedHeight = width
                }
                val map: StreamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) ?: continue
                previewSize = chooseOptimalSize(map.getOutputSizes(ImageFormat.YUV_420_888), rotatedWidth, rotatedHeight)
                cameraId = id
                return
            }
        } catch (e : CameraAccessException) {
            e.printStackTrace()
        }
    }

    // Closes the cameraDevice
    private fun closeCamera() {
        if (cameraDevice != null) {
            cameraDevice?.close()
            cameraDevice = null
        }
    }

    // Starts background thread
    private fun startBackgroundThread() {
        backgroundHandlerThread = HandlerThread("CameraBackground").also { it.start()
            backgroundHandler = Handler(it.looper)}
    }

    // Stops background thread
    private fun stopBackgroundThread() {
        backgroundHandlerThread?.quitSafely()
        try {
            backgroundHandlerThread?.join()
            backgroundHandlerThread = null
            backgroundHandler = null
        } catch (e : InterruptedException) {
            e.printStackTrace()
        }
    }

    // Chooses the optimal size for the camera that matches where it shows
    private fun chooseOptimalSize(choices: Array<Size>, width: Int, height: Int ): Size {
        val aspectTolerance = 0.1
        val targetRatio = height / width

        var optimalSize: Size? = null
        var minDiff = Int.MAX_VALUE


        for (size in choices) {
            val ratio = size.width / size.height
            if (Math.abs(ratio - targetRatio) > aspectTolerance) continue
            if (Math.abs(size.height - height) < minDiff) {
                optimalSize = size
                minDiff = Math.abs(size.height - height)
            }
        }

        if (optimalSize == null) {
            minDiff = Int.MAX_VALUE
            for (size in choices) {
                if (Math.abs(size.height - height.toDouble()) < minDiff) {
                    optimalSize = size
                    minDiff = Math.abs(size.height - height)
                }
            }
        }
        return optimalSize!!
    }

    // Calculates how much the rotation combined is redundant
    private fun sensorToDeviceRotation(cameraCharacteristics: CameraCharacteristics, deviceOrientation: Int): Int {
        val sensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)
        val orientation = orientations.get(deviceOrientation)
        return (sensorOrientation!! + orientation + 360) %360
    }

    // Starts the preview
    private fun startPreview() {
        val surfaceTexture = camera_view.surfaceTexture
        surfaceTexture.setDefaultBufferSize(previewSize.width, previewSize.height)
        val previewSurface = Surface(surfaceTexture)

        try {
            captureRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder.addTarget(previewSurface)

            cameraDevice?.createCaptureSession(Arrays.asList(previewSurface), object : CameraCaptureSession.StateCallback(){
                override fun onConfigureFailed(session: CameraCaptureSession) {
                    Snackbar.make(camera_view, R.string.setup_failed, Snackbar.LENGTH_LONG).show()
                }

                override fun onConfigured(session: CameraCaptureSession) {
                    try {
                        session.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler)
                    } catch (e : CameraAccessException) {
                        e.printStackTrace()
                    }
                }

            }, null)
        } catch (e : CameraAccessException) {
            e.printStackTrace()
        }
    }

    // Creates fade out animation to animate the qr code
    private fun fadeOutAndHide() {
        val fade = AlphaAnimation(1f, 0f)
        fade.interpolator = AccelerateInterpolator() //and this
        fade.startOffset = 1000
        fade.duration = 1000

        fade.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                qr_img_view.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
        qr_img_view.animation = fade
    }

    // Uses the barcode detector to if the bitmap from the parameter contains a QR code (only processes the first result)
    private fun scan(bitmap: Bitmap?) {
        val image = FirebaseVisionImage.fromBitmap(bitmap!!)
        barcodeDetector.detectInImage(image)
            .addOnSuccessListener { result ->
                if(result.isNotEmpty()) {
                    processResult(result[0])
                }
            }
            .addOnFailureListener{ e -> Toast.makeText(baseContext, e.message, Toast.LENGTH_LONG).show() }
    }

    // Decodes the QR code to a string and uses that string to get an order
    // Return if that order is not supplied by the current supplier
    private fun processResult(result: FirebaseVisionBarcode) {
            val raw_value = result.rawValue
            val value_type = result.valueType

            when(value_type) {
                FirebaseVisionBarcode.TYPE_TEXT -> {
                    orderLogic.getOrderById(raw_value.toString(), object : ICallbackOrder {
                        override fun onFinishOrder(order: Order?) {
                            if(order == null || order.supplierId != supplierId) {
                                return
                            } else {
                                closeCamera()
                                stopBackgroundThread()
                                scan_progress.visibility = View.VISIBLE
                                dealLogic.getDealById(order.dealId, object : ICallbackDeal {
                                    override fun onFinishDeal(deal: Deal?) {
                                        customerLogic.getCustomerById(order.customerId, object : ICallbackCustomer {
                                            override fun onFinishCustomer(customer: Customer?) {
                                                val intent = Intent(scan_view.context, DealActivity::class.java).putExtra(
                                                    DEAL, deal).putExtra(CUSTOMER, customer).putExtra(SHOW_INVALID, order.valid)
                                                scan_view.context.startActivity(intent)
                                                finish()
                                            }
                                        })
                                    }
                                })
                            }
                        }
                    })
                }
            }
    }
}
