package com.example.osmium.business.circularLateration

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder
import org.apache.commons.math3.fitting.leastsquares.MultivariateJacobianFunction
import org.apache.commons.math3.linear.Array2DRowRealMatrix
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.util.Pair

fun main() {

    val positions =
        arrayOf(
            doubleArrayOf(35.0326033, 50.3635483),
            doubleArrayOf(35.0326033, 50.3635483),
            doubleArrayOf(35.0326033, 50.3635483)
        )

    val distances = doubleArrayOf(
        1883.6490894898002,
        1883.6490894898002,
        1883.6490894898002
    )

    val estimatedPosition = estimatePosition(positions, distances)
    println("Estimated Position: x = ${estimatedPosition[0]}, y = ${estimatedPosition[1]}")
}

fun estimatePosition(positions: Array<DoubleArray>, distances: DoubleArray): DoubleArray {
    val n = positions.size

    val model = MultivariateJacobianFunction { point ->
        val values = DoubleArray(n)
        val jacobian = Array(n) { DoubleArray(2) }

        for (i in positions.indices) {
            val dx = point.toArray()[0] - positions[i][0]
            val dy = point.toArray()[1] - positions[i][1]
            values[i] = dx * dx + dy * dy - distances[i] * distances[i]
            jacobian[i][0] = 2 * dx
            jacobian[i][1] = 2 * dy
        }

        Pair(ArrayRealVector(values), Array2DRowRealMatrix(jacobian))
    }

    val target = ArrayRealVector(n)

    val problem = LeastSquaresBuilder()
        .start(doubleArrayOf(0.0, 0.0))
        .model(model)
        .target(target)
        .lazyEvaluation(false)
        .maxEvaluations(1000)
        .maxIterations(1000)
        .build()

    val optimum = org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer()
        .optimize(problem)
    return optimum.point.toArray()
}
