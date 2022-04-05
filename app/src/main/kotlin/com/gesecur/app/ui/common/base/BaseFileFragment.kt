package com.gesecur.app.ui.common.base

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import com.gesecur.app.BuildConfig
import com.gesecur.app.utils.writeToFile
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


abstract class BaseFileFragment(@LayoutRes layout: Int = ResourcesCompat.ID_NULL) : BaseFragment(layout) {

    companion object {
        const val FILE_PROVIDER_TEMP_DIRECTORY = "temp/"
    }


    protected var fileSelected: File? = null

    @SuppressLint("MissingPermission")
    private val locationPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            showFileChooser()
        }
        else {

        }
    }

        private val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            showCamera()
        } else { }
    }

    protected fun checkFilesPermission() {
        locationPermissionsLauncher.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
    }

    protected fun checkCameraPermission() {
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if(uri == null) return@registerForActivityResult

        val fileName = uri?.lastPathSegment?.let {
            with(it.lastIndexOf("/")) {
                if(this > -1)
                    it.substring(this + 1)
                else
                    it
            }

        } ?: run {
            "imagefile.jpeg"
        }

        val newFile = createPictureFile(fileName)

        newFile?.let {
            uri.writeToFile(requireContext(), it)

            val pictureFile = reduceFileSize(it)
            fileSelected = pictureFile

            onFileSelected(pictureFile!!)
        }

    }

    private val launchCameraForResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            reduceFileSize(fileSelected)?.let { onFileSelected(it) }
        }
    }

    private fun showFileChooser() {
        startForResult.launch(
            "image/*"
        )
    }

    private fun showCamera() {
        fileSelected = createPictureFile("imageFile${System.currentTimeMillis()}.jpeg")

        val uri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID , fileSelected!!)

        launchCameraForResult.launch(uri)
    }

    private fun createPictureFile(fileName: String): File {
        val outputDir = File(requireContext().filesDir,
            FILE_PROVIDER_TEMP_DIRECTORY
        )

        if (!outputDir.exists()) outputDir.mkdir()

        return File(outputDir, if(!fileName.contains(".")) "$fileName.jpeg" else fileName)
    }

    abstract fun onFileSelected(file: File)

    open fun reduceFileSize(file: File?): File? {
        if(file == null) return null

        return try {

            // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image
            var inputStream = FileInputStream(file)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size we want to scale to
            val REQUIRED_SIZE = 100

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE
            ) {
                scale *= 2
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)
            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()

            // here i override the original image file
            val rotatedBitmap = rotateImageIfNeeded(file, selectedBitmap!!)
            file.createNewFile()
            val outputStream = FileOutputStream(file)
            rotatedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            file
        } catch (e: Exception) {
            null
        }
    }

    private fun rotateImageIfNeeded(file: File, bitmap: Bitmap): Bitmap? {
        val ei = ExifInterface(file)

        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        var rotatedBitmap: Bitmap? = null
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap = rotateImage(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap = rotateImage(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap = rotateImage(bitmap, 270)
            ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = bitmap
            else -> rotatedBitmap = bitmap
        }

        return rotatedBitmap
    }
}