package com.example.osmium.business.circularLateration

import org.junit.Assert.assertArrayEquals
import org.junit.Test

class CircularLaterationTest {

    @Test
    fun testEstimatePositionWithFourPoints() {
        val positions = arrayOf(
            doubleArrayOf(0.0, 0.0),
            doubleArrayOf(1.0, 0.0),
            doubleArrayOf(0.0, 1.0),
            doubleArrayOf(1.0, 1.0)
        )

        val distances = doubleArrayOf(
            0.7071,
            0.7071,
            0.7071,
            0.7071
        )

        val expectedPosition = doubleArrayOf(0.5, 0.5)

        val estimatedPosition = estimatePosition(positions, distances)
        assertArrayEquals(expectedPosition, estimatedPosition, 0.1)
    }

    @Test
    fun testEstimatePositionWithDifferentDistances() {
        val positions = arrayOf(
            doubleArrayOf(0.0, 0.0),
            doubleArrayOf(2.0, 0.0),
            doubleArrayOf(0.0, 2.0)
        )

        val distances = doubleArrayOf(
            1.414,
            1.414,
            1.414
        )

        val expectedPosition = doubleArrayOf(1.0, 1.0)

        val estimatedPosition = estimatePosition(positions, distances)
        assertArrayEquals(expectedPosition, estimatedPosition, 0.1)
    }
}