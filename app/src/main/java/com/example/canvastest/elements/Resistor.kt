package com.example.canvastest.elements

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import kotlin.math.pow

class Resistor(startPoint: Point, endPoint: Point,var resistance : Double = 1.0) : Element(startPoint,endPoint) {
    override fun draw(canvas: Canvas?, paint: Paint?) {
        //super.draw(canvas, paint) //zakomentowany usuwa boxa
        val path = Path()
        val X = 30.0
        val Y = 12.0
        path.moveTo(((-X)*Math.cos(ROTATION)-(Y)*Math.sin(ROTATION) + centerX).toFloat(),((Y)*Math.cos(ROTATION)+(-X)*Math.sin(ROTATION) + centerY).toFloat())
        path.lineTo(((X)*Math.cos(ROTATION)-(Y)*Math.sin(ROTATION) + centerX).toFloat(),((Y)*Math.cos(ROTATION)+(X)*Math.sin(ROTATION) + centerY).toFloat())
        path.lineTo(((X)*Math.cos(ROTATION)-(-Y)*Math.sin(ROTATION) + centerX).toFloat(),((-Y)*Math.cos(ROTATION)+(X)*Math.sin(ROTATION) + centerY).toFloat())
        path.lineTo(((-X)*Math.cos(ROTATION)-(-Y)*Math.sin(ROTATION) + centerX).toFloat(),((-Y)*Math.cos(ROTATION)+(-X)*Math.sin(ROTATION) + centerY).toFloat())
        path.lineTo(((-X)*Math.cos(ROTATION)-(Y)*Math.sin(ROTATION) + centerX).toFloat(),((Y)*Math.cos(ROTATION)+(-X)*Math.sin(ROTATION) + centerY).toFloat())
        canvas?.drawPath(path,paint)
        val vsc = Point(centerX.toInt()-startPoint.x,centerY.toInt()-startPoint.y)
        val s = 1.0 - (X/Math.sqrt(vsc.x.toDouble().pow(2)+vsc.y.toDouble().pow(2)))
        canvas?.drawLine(startPoint.x.toFloat(),startPoint.y.toFloat(),(startPoint.x+(vsc.x*s)).toFloat(),(startPoint.y+(vsc.y*s)).toFloat(),paint)
        canvas?.drawLine(endPoint.x.toFloat(),endPoint.y.toFloat(),(endPoint.x+(-vsc.x*s)).toFloat(),(endPoint.y+(-vsc.y*s)).toFloat(),paint)
    }
}