package com.example.canvastest.elements

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import kotlin.math.pow

class TensionSource(startPoint : Point, endPoint : Point, var tensionValue : Double = 5.0) : Element(startPoint,endPoint){
    override fun draw(canvas: Canvas?, paint: Paint?) {
        //super.draw(canvas, paint)
        val g = 5f
        val vsc = Point(centerX.toInt()-startPoint.x,centerY.toInt()-startPoint.y)
        var l = Math.sqrt(vsc.x.toDouble().pow(2)+vsc.y.toDouble().pow(2))
        val s = 1.0 - (g/l)
        canvas?.drawLine(startPoint.x.toFloat(),startPoint.y.toFloat(),(startPoint.x+(vsc.x*s)).toFloat(),(startPoint.y+(vsc.y*s)).toFloat(),paint)
        canvas?.drawLine(endPoint.x.toFloat(),endPoint.y.toFloat(),(endPoint.x+(-vsc.x*s)).toFloat(),(endPoint.y+(-vsc.y*s)).toFloat(),paint)
        val x = (-vsc.y.toDouble()/vsc.x.toDouble())
        l = Math.sqrt(x.pow(2)+1)
        var ss = 15f/l
        canvas?.drawLine((startPoint.x+(vsc.x*s)-(x*ss)).toFloat(),(startPoint.y+(vsc.y*s)-(ss)).toFloat(),(startPoint.x+(vsc.x*s)+(x*ss)).toFloat(),(startPoint.y+(vsc.y*s)+(ss)).toFloat(),paint)
        ss = 30f/l
        canvas?.drawLine((endPoint.x+(-vsc.x*s)-(x*ss)).toFloat(),(endPoint.y+(-vsc.y*s)-(ss)).toFloat(),(endPoint.x+(-vsc.x*s)+(x*ss)).toFloat(),(endPoint.y+(-vsc.y*s)+(ss)).toFloat(),paint)
    }
}