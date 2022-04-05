package com.gesecur.app.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.common.util.CollectionUtils
import java.util.*

object GeoCoderUtil {

    fun execute(
        context: Context,
        latitude: Double,
        longitude: Double,
        callback: (Address, String) -> Unit
    ) {


        try {
            val addresses: MutableList<Address>
            val geocoder = Geocoder(context, Locale.ENGLISH)
            addresses =
                geocoder.getFromLocation(latitude, longitude, 1)
            if (!CollectionUtils.isEmpty(addresses)) {
                val fetchedAddress = addresses[0]


                callback(fetchedAddress, getAdressAsString(fetchedAddress))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAdressAsString(fetchedAddress: Address) : String {
        var address = ""
        var cityName = ""
        var areaName = ""
        var postalCode = ""

        if (fetchedAddress.maxAddressLineIndex > -1) {
            address = fetchedAddress.getAddressLine(0)
            fetchedAddress.locality?.let {
                cityName = it
            }
            fetchedAddress.subLocality?.let {
                areaName = it
            }

            fetchedAddress.postalCode?.let {
                postalCode = it
            }
        }

        return "$address\n$postalCode $areaName"
    }
}