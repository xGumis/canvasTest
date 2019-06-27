package com.example.canvastest

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.canvastest.Elements.Element
import kotlin.math.pow

class CircuitView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var elements :MutableList<Element> = mutableListOf()
    var paint :Paint = Paint()
    private var catchedElement : Element? = null
    private var isStartCatched = true
    private var isEdgeCatched = false
    private val PULLCAP = 50.0
    private var mCurX = 0f
    private var mCurY = 0f
    private var mStartX = 0f
    private var mStartY = 0f

    init {
        isFocusable = true
        paint.color = Color.BLACK
        paint.strokeWidth = 5f
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        elements.forEach{it.draw(canvas,paint)}
    }

    private fun actionDown(x: Float, y: Float) {
        mCurX = x
        mCurY = y
        elements.forEach {
            when(it.isCatched(x,y)){
                Element.Catch.YES -> {catchedElement = it;isEdgeCatched=false}
                Element.Catch.START -> {catchedElement = it;isEdgeCatched=true;isStartCatched=true}
                Element.Catch.END -> {catchedElement = it;isEdgeCatched=true;isStartCatched=false}
                else -> {}
            }
        }

    }

    private fun actionMove(x: Float, y: Float) {
        catchedElement?.let{ ce ->
        if(isEdgeCatched){
            if(isStartCatched)
                ce.startPoint = Point(x.toInt(),y.toInt())
            else
                ce.endPoint = Point(x.toInt(),y.toInt())
        }
        else{
            val vec = Point((mCurX-x).toInt(),(mCurY-y).toInt())
            val sx = ce.startPoint.x
            val sy = ce.startPoint.y
            val ex = ce.endPoint.x
            val ey = ce.endPoint.y
            ce.startPoint = Point(sx-vec.x,sy-vec.y)
            ce.endPoint = Point(ex-vec.x,ey-vec.y)
        }
        }
        mCurX = x
        mCurY = y
    }

    private fun actionUp() {
        catchedElement?.let{ ce ->
            val sx = ce.startPoint.x
            val sy = ce.startPoint.y
            val ex = ce.endPoint.x
            val ey = ce.endPoint.y
            elements.forEach {
                if(ce != it){
                    var radCircle = Math.sqrt((it.startPoint.x - sx).toDouble().pow(2) + (it.startPoint.y - sy).toDouble().pow(2))
                    if(radCircle < PULLCAP)
                        ce.startPoint = Point(it.startPoint)
                    radCircle = Math.sqrt((it.startPoint.x - ex).toDouble().pow(2) + (it.startPoint.y - ey).toDouble().pow(2))
                    if(radCircle < PULLCAP)
                        ce.endPoint = Point(it.startPoint)
                    radCircle = Math.sqrt((it.endPoint.x - sx).toDouble().pow(2) + (it.endPoint.y - sy).toDouble().pow(2))
                    if(radCircle < PULLCAP)
                        ce.startPoint = Point(it.endPoint)
                    radCircle = Math.sqrt((it.endPoint.x - ex).toDouble().pow(2) + (it.endPoint.y - ey).toDouble().pow(2))
                    if(radCircle < PULLCAP)
                        ce.endPoint = Point(it.endPoint)
                }
            }
            catchedElement = null
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = x
                mStartY = y
                actionDown(x, y)
                isPressed = true
            }
            MotionEvent.ACTION_MOVE -> actionMove(x, y)
            MotionEvent.ACTION_UP -> {
                actionUp()
                val radCircle = Math.sqrt((mCurX - mStartX).toDouble().pow(2) + (mCurY - mStartY).toDouble().pow(2))
                if(radCircle<5)
                    performClick()
                isPressed = false
            }
        }
        invalidate()
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        println("click")
        return true
    }

    fun addElement(element: Element){
        elements.add(element)
    }

    fun clearCanvas() {
        elements.clear()
        invalidate()
    }
}