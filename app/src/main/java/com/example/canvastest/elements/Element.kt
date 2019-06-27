package com.example.canvastest.Elements

import android.graphics.*
import android.graphics.drawable.shapes.Shape
import kotlin.math.pow

open class Element(private var sp: Point,private var ep: Point) : Shape() {
    private val HEIGHT = 70.0
    private val MIN_WIDTH = 200.0
    var startPoint: Point
        get(){
            return sp
        }
        set(value){
            val v = Point((value.x - endPoint.x),(value.y - endPoint.y))
            val l = Math.sqrt(v.x.toDouble().pow(2) + v.y.toDouble().pow(2))
            if(l < MIN_WIDTH)
            {
                val s = MIN_WIDTH/l
                sp = Point((ep.x+(v.x*s)).toInt(),(ep.y+(v.y*s)).toInt())
            }
            else
                sp = value
        }
    var endPoint: Point
        get(){
            return ep
        }
        set(value){
            val v = Point((value.x - startPoint.x),(value.y - startPoint.y))
            val l = Math.sqrt(v.x.toDouble().pow(2) + v.y.toDouble().pow(2))
            if(l < MIN_WIDTH)
            {
                val s = MIN_WIDTH/l
                ep = Point(sp.x+(v.x*s).toInt(),sp.y+(v.y*s).toInt())
            }
            else
                ep = value

        }
    protected var centerX = 0f
        get(){
            return (startPoint.x + ((WIDTH*Math.cos(ROTATION))/2.0)).toFloat()
        }
    protected var centerY = 0f
        get() {
            return (startPoint.y + ((WIDTH*Math.sin(ROTATION))/2.0)).toFloat()
        }
    private var WIDTH = 0.0
        get(){
            return Math.sqrt((startPoint.x - endPoint.x).toDouble().pow(2) + (startPoint.y - endPoint.y).toDouble().pow(2))
        }
    protected var ROTATION = 0.0
        get(){
            return Math.atan2((endPoint.y - startPoint.y).toDouble(),(endPoint.x - startPoint.x).toDouble())
        }
    private var DEGEREES = 0.0
        get(){
            return ROTATION*(180.0/Math.PI)
        }
    private var top_left = Point()
        get(){
            val X = -(WIDTH/2.0)
            val Y = HEIGHT/2.0
            return Point((X*Math.cos(ROTATION)-Y*Math.sin(ROTATION) + centerX).toInt(),(Y*Math.cos(ROTATION)+X*Math.sin(ROTATION) + centerY).toInt())
        }
    private var top_right = Point()
        get(){
            val X = WIDTH/2.0
            val Y = HEIGHT/2.0
            return Point((X*Math.cos(ROTATION)-Y*Math.sin(ROTATION) + centerX).toInt(),(Y*Math.cos(ROTATION)+X*Math.sin(ROTATION) + centerY).toInt())
        }
    private var bottom_left = Point()
        get(){
            val X = -(WIDTH/2.0)
            val Y = -(HEIGHT/2.0)
            return Point((X*Math.cos(ROTATION)-Y*Math.sin(ROTATION) + centerX).toInt() ,(Y*Math.cos(ROTATION)+X*Math.sin(ROTATION) + centerY).toInt())
        }
    private var bottom_right = Point()
        get(){
            val X = WIDTH/2.0
            val Y = -(HEIGHT/2.0)
            return Point((X*Math.cos(ROTATION)-Y*Math.sin(ROTATION) + centerX).toInt(),(Y*Math.cos(ROTATION)+X*Math.sin(ROTATION) + centerY).toInt())
        }
    private val CATCHCAP = 50.0

    enum class Catch {
        NO,
        YES,
        START,
        END
    }

    fun isCatched(x: Float, y: Float): Catch {
        var radCircle = Math.sqrt(((startPoint.x - x).pow(2) + (startPoint.y - y).pow(2)).toDouble())
        if (radCircle < CATCHCAP) {
            return Catch.START
        }
        radCircle = Math.sqrt(((endPoint.x - x).pow(2) + (endPoint.y - y).pow(2)).toDouble())
        if (radCircle < CATCHCAP) {
            return Catch.END
        }
        if (startPoint.x <= endPoint.x && startPoint.y <= endPoint.y){
            if(x >= top_left.x && x <= bottom_right.x && y <= top_right.y && y >= bottom_left.y)
                return Catch.YES
        }
        if (startPoint.x <= endPoint.x && startPoint.y >= endPoint.y){
            if(x <= top_right.x && x >= bottom_left.x && y >= bottom_right.y && y <= top_left.y)
                return Catch.YES
        }
        if (startPoint.x >= endPoint.x && startPoint.y <= endPoint.y){
            if(x <= bottom_left.x && x >= top_right.x && y >= top_left.y && y <= bottom_right.y)
                return Catch.YES
        }
        if (startPoint.x >= endPoint.x && startPoint.y >= endPoint.y){
            if(x <= top_left.x && x >= bottom_right.x && y >= top_right.y && y <= bottom_left.y)
                return Catch.YES
        }
        return Catch.NO
    }

    override fun draw(canvas: Canvas?, paint: Paint?) {
        paint?.strokeWidth = 20f
        paint?.color = Color.RED
        canvas?.drawPoint(startPoint.x.toFloat(),startPoint.y.toFloat(),paint)
        paint?.color = Color.GREEN
        canvas?.drawPoint(endPoint.x.toFloat(),endPoint.y.toFloat(),paint)
        paint?.color = Color.BLUE
        canvas?.drawPoint(centerX,centerY,paint)
        paint?.color = Color.BLACK
        paint?.strokeWidth = 5f
        val path = Path()
        path.moveTo(top_left.x.toFloat(),top_left.y.toFloat())
        path.lineTo(top_right.x.toFloat(),top_right.y.toFloat())
        path.lineTo(bottom_right.x.toFloat(),bottom_right.y.toFloat())
        path.lineTo(bottom_left.x.toFloat(),bottom_left.y.toFloat())
        path.lineTo(top_left.x.toFloat(),top_left.y.toFloat())
        canvas?.drawPath(path,paint)
    }
}