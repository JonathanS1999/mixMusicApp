package com.example.musicx.equations

class EcLineal {

    fun getPendient(
        x2: Float,
        x1: Float,
        y2: Float,
        y1: Float
    ): Float {
        return (y2 - y1) / (x2 - x1)
    }


    fun getX(x2: Float, x1: Float, y2: Float, y1: Float, y: Float): Float {
        val m = getPendient(x2, x1, y2, y1)
        var x = 0f
        x = (y - y1 + m * x1) / m
        return x
    }


    fun getY(x2: Float, x1: Float, y2: Float, y1: Float, x: Float): Float {
        val m = getPendient(x2, x1, y2, y1)
        var y = 0f
        y = y1 - m * x1 + m * x
        return y
    }


}