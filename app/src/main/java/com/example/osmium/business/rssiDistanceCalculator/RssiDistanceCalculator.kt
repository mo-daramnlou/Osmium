package com.example.osmium.business.rssiDistanceCalculator

import kotlin.math.pow

object RssiDistanceCalculator {
    // Reference distance (typically 1 meter)
    private const val REFERENCE_DISTANCE = 1.0

    // Path loss at reference distance (d0), in dB (depends on the environment)
    private const val PATH_LOSS_AT_REFERENCE_DISTANCE = 40.0

    // Path loss exponent (depends on the environment)
    private const val PATH_LOSS_EXPONENT = 4.0

    /**
     * Calculate the distance based on the RSSI value.
     *
     * @param rssi the received signal strength indicator (in dBm)
     * @return the estimated distance (in meters)
     */
    fun calculateDistance(rssi: Double): Double {
        return REFERENCE_DISTANCE * 10.0.pow((PATH_LOSS_AT_REFERENCE_DISTANCE - rssi) / (10 * PATH_LOSS_EXPONENT))
    }
}

