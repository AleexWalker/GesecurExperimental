package com.gesecur.app.utils

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.gesecur.app.R
import com.gesecur.app.domain.models.GesecurError
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Context extensions
 */
fun Context.openPlayStore(applicationId: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$applicationId")))
    } catch (anfe: ActivityNotFoundException) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$applicationId")))
    }
}

fun Context.toast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Context.toast(message: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Context.toast(error: GesecurError, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, error.getMessageError(this), length).show()
}

/**
 * Fragment extensions
 */
fun Fragment.finishActivity() {
    activity?.finish()
}

fun Fragment.openPlayStore(applicationId: String) {
    context?.openPlayStore(applicationId)
}

fun Fragment.toast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    context?.toast(message, length)
}

fun Fragment.toast(message: Int, length: Int = Toast.LENGTH_SHORT) {
    context?.toast(message, length)
}

fun Fragment.toast(error: GesecurError, length: Int = Toast.LENGTH_SHORT) {
    context?.toast(error, length)
}

fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches();
}

fun Fragment.showConfirm(
        @StringRes text: Int, listener: DialogInterface.OnClickListener, @StringRes buttonPositive: Int = R.string.UTILS_YES, @StringRes buttonNegative: Int = R.string.UTILS_NO
) {
    if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
        requireActivity().showConfirm(text, listener, buttonPositive, buttonNegative)
    }
}

fun Activity.showConfirm(@StringRes text: Int, listener: DialogInterface.OnClickListener, @StringRes buttonPositive: Int = R.string.UTILS_YES, @StringRes buttonNegative: Int = R.string.UTILS_NO) {
    MaterialAlertDialogBuilder(this, R.style.dialogTheme)
            .setCancelable(false)
            .setTitle("")
            .setMessage(text)
            .setPositiveButton(buttonPositive, listener)
            .setNegativeButton(buttonNegative, listener)
            .show()
}

fun Fragment.showAlert(@StringRes text: Int, listener: DialogInterface.OnClickListener, @StringRes buttonPositive: Int = R.string.UTILS_ACCEPT) {
    MaterialAlertDialogBuilder(requireContext(), R.style.dialogTheme)
        .setCancelable(false)
        .setTitle("")
        .setMessage(text)
        .setPositiveButton(buttonPositive, listener)
        .show()
}

fun Activity.getCurrentLocation(unit : (location: Location?) -> Unit) {
    // checking location permission
    if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return
    }

    LocationServices.getFusedLocationProviderClient(this).lastLocation
            .addOnCompleteListener { task ->
                if(task.result == null) {
                    requestLocationUpdate(unit)
                }
                else
                    unit.invoke(task.result)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed on getting current location",
                        Toast.LENGTH_SHORT).show()
            }
}

@SuppressLint("MissingPermission")
private fun Activity.requestLocationUpdate(unit : (location: Location?) -> Unit) {
    with(LocationRequest.create()) {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 0
        fastestInterval = 0
        numUpdates = 2

        LocationServices.getFusedLocationProviderClient(this@requestLocationUpdate).requestLocationUpdates(this, object: LocationCallback() {

            override fun onLocationResult(p0: LocationResult) {
                unit.invoke(p0.lastLocation)
            }

            override fun onLocationAvailability(p0: LocationAvailability) { super.onLocationAvailability(p0) }
        }, Looper.myLooper())
    }
}


/** Date extensions **/
fun LocalDate.getDayNameAndNumber(): Pair<String, String> {
    return Pair(
            format(DateTimeFormatter.ofPattern("E", Locale.forLanguageTag("es"))),
            format(DateTimeFormatter.ofPattern("d", Locale.forLanguageTag("es")))
    )
}

fun FloatingActionButton.openOptions(options: List<ExtendedFloatingActionButton>) {
    animate().setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                }
            })
            .rotation(135f);

    options.forEach { it.showOut() }
}

fun FloatingActionButton.closeOptions(options: List<ExtendedFloatingActionButton>) {
    animate().setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                }
            })
            .rotation(0f)

    options.forEach { it.hide() }
}

fun ExtendedFloatingActionButton.showOut() {
    visibility = View.VISIBLE
    alpha = 0f
    translationY = height.toFloat()
    animate()
            .setDuration(200)
            .translationY(0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                }
            })
            .alpha(1f)
            .start()
}

fun ExtendedFloatingActionButton.hide() {
    visibility = View.VISIBLE
    alpha = 1f
    translationY = 0f
    animate()
            .setDuration(200)
            .translationY(height.toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    visibility = View.GONE
                    super.onAnimationEnd(animation)
                }
            }).alpha(0f)
            .start()
}


fun ExtendedFloatingActionButton.initOption() {
    visibility = View.GONE
    translationY = height.toFloat()
    alpha = 0f
}

fun EditText.getIntValue(defaultInt: Int = 0): Int {
    return if(text.isNullOrEmpty()) defaultInt else text.toString().toInt()
}

fun VectorDrawable.toBitmapDescriptor(): BitmapDescriptor? {
    return this.let {
        it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)
        it.draw(Canvas(bitmap))
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}

fun Uri.writeToFile(context: Context, outputFile: File) {
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(this)
        val outputStream = FileOutputStream(outputFile)
        if (inputStream != null) {
            var n: Int
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            while (-1 != inputStream.read(buffer).also { n = it }) {
                outputStream.write(buffer, 0, n)
            }
            inputStream.close()
        }
        outputStream.close()
    } catch (e: Exception) {
        Timber.e(e)
    }
}

fun String.isImageExtension(): Boolean {
    return endsWith("jpg") || endsWith("png") || endsWith("jpeg") || endsWith("bmp") || endsWith("svg")
}
