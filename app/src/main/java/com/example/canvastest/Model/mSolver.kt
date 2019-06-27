package com.example.canvastest.Model
class mSolver() {
    companion object {


        private val EPSILON = 1e-10
        fun lsolve(A: Array<DoubleArray>, b: DoubleArray): DoubleArray {
            val n = b.size

            val eps:Double  = EPSILON +1
            println(eps)
            for (p in 0 until n) {

                // find pivot row and swap
                var max = p
                for (i in p + 1 until n) {
                    if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                        max = i
                    }
                }
                val temp = A[p]
                A[p] = A[max]
                A[max] = temp
                val t = b[p]
                b[p] = b[max]
                b[max] = t

                // singular or nearly singular
               // if (Math.abs(A[p][p]) <= EPSILON) {
               //     throw ArithmeticException("Matrix is singular or nearly singular")
               // }

                // pivot within A and b
                for (i in p + 1 until n) {
                    val alpha = A[i][p] / A[p][p]
                    b[i] -= alpha * b[p]
                    for (j in p until n) {
                        A[i][j] -= alpha * A[p][j]
                    }
                }
            }

            // back substitution
            val x = DoubleArray(n)
            for (i in n - 1 downTo 0) {
                var sum = 0.0
                for (j in i + 1 until n) {
                    sum += A[i][j] * x[j]
                }
                x[i] = (b[i] - sum) / A[i][i]
            }
            return x
        }
    }
}
