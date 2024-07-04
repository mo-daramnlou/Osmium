package com.example.osmium.business.rssiDistanceCalculator

import android.util.Log

fun main() {
    val rssi = -95.0 // Example RSSI value in dBm
    val distance = RssiDistanceCalculator.calculateDistance(rssi)
    println("Estimated distance: $distance meters")
}