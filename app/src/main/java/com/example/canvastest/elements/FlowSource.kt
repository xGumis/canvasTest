package com.example.canvastest.elements

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import kotlin.math.pow

class FlowSource(startPoint: Point, endPoint:Point, var flowValue : Double = 5.0):Element(startPoint,endPoint) {
    override fun draw(canvas: Canvas?, paint: Paint?) {
        //super.draw(canvas, paint)
        val r = 30f
        canvas?.drawCircle(centerX,centerY,r,paint)
        val vsc = Point(centerX.toInt()-startPoint.x,centerY.toInt()-startPoint.y)
        var l = Math.sqrt(vsc.x.toDouble().pow(2)+vsc.y.toDouble().pow(2))
        var s = 1.0 - (r/l)
        canvas?.drawLine(startPoint.x.toFloat(),startPoint.y.toFloat(),(startPoint.x+(vsc.x*s)).toFloat(),(startPoint.y+(vsc.y*s)).toFloat(),paint)
        canvas?.drawLine(endPoint.x.toFloat(),endPoint.y.toFloat(),(endPoint.x+(-vsc.x*s)).toFloat(),(endPoint.y+(-vsc.y*s)).toFloat(),paint)
        s = (20.0/l)
        canvas?.drawLine((centerX-(vsc.x*s)).toFloat(),(centerY-(vsc.y*s)).toFloat(),(centerX+(vsc.x*s)).toFloat(),(centerY+(vsc.y*s)).toFloat(),paint)
        val x = (-vsc.y.toDouble()/vsc.x.toDouble())
        l = Math.sqrt(x.pow(2)+1)
        val ss = (15.0/l)
        canvas?.drawLine((centerX-(x*ss)).toFloat(),(centerY-(ss)).toFloat(),(centerX+(vsc.x*s)).toFloat(),(centerY+(vsc.y*s)).toFloat(),paint)
        canvas?.drawLine((centerX+(x*ss)).toFloat(),(centerY+(ss)).toFloat(),(centerX+(vsc.x*s)).toFloat(),(centerY+(vsc.y*s)).toFloat(),paint)
    }
}