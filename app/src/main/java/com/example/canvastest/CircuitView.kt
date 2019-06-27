package com.example.canvastest

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.canvastest.elements.*
import kotlin.math.pow

class CircuitView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var elements: MutableList<Element> = mutableListOf()
    var joints = mutableListOf<Joint>()
    private var paint: Paint = Paint()
    private var catchedElement: Element? = null
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
        elements.forEach { it.draw(canvas, paint) }
    }

    private fun actionDown(x: Float, y: Float) {
        mCurX = x
        mCurY = y
        elements.forEach {
            when (it.isCatched(x, y)) {
                Element.Catch.YES -> {
                    catchedElement = it
                    isEdgeCatched = false
                    unjoinElement(Pair(it, true))
                    unjoinElement(Pair(it, false))
                }
                Element.Catch.START -> {
                    catchedElement = it
                    isEdgeCatched = true
                    isStartCatched = true
                    unjoinElement(Pair(it, true))
                }
                Element.Catch.END -> {
                    catchedElement = it
                    isEdgeCatched = true
                    isStartCatched = false
                    unjoinElement(Pair(it, false))
                }
                else -> {
                }
            }
            println(it.isCatched(x,y))
        }
    }

    private fun actionMove(x: Float, y: Float) {
        catchedElement?.let { ce ->
            if (isEdgeCatched) {
                if (isStartCatched)
                    ce.startPoint = Point(x.toInt(), y.toInt())
                else
                    ce.endPoint = Point(x.toInt(), y.toInt())
            } else {
                val vec = Point((mCurX - x).toInt(), (mCurY - y).toInt())
                val sx = ce.startPoint.x
                val sy = ce.startPoint.y
                val ex = ce.endPoint.x
                val ey = ce.endPoint.y
                ce.startPoint = Point(sx - vec.x, sy - vec.y)
                ce.endPoint = Point(ex - vec.x, ey - vec.y)
            }
        }
        mCurX = x
        mCurY = y
    }

    private fun actionUp() {
        catchedElement?.let { ce ->
            val sx = ce.startPoint.x
            val sy = ce.startPoint.y
            val ex = ce.endPoint.x
            val ey = ce.endPoint.y
            if (!isEdgeCatched)
                run breaker@{
                    elements.forEach {
                        if (ce != it) {
                            var radCircle = Math.sqrt(
                                (it.startPoint.x - sx).toDouble().pow(2) + (it.startPoint.y - sy).toDouble().pow(2)
                            )
                            if (radCircle < PULLCAP) {
                                ce.startPoint = Point(it.startPoint)
                                val newEl = Pair(ce, true)
                                val oldEl = Pair(it, true)
                                joinElements(newEl, oldEl)
                                return@breaker
                            }
                            radCircle = Math.sqrt(
                                (it.startPoint.x - ex).toDouble().pow(2) + (it.startPoint.y - ey).toDouble().pow(2)
                            )
                            if (radCircle < PULLCAP) {
                                ce.endPoint = Point(it.startPoint)
                                val newEl = Pair(ce, false)
                                val oldEl = Pair(it, true)
                                joinElements(newEl, oldEl)
                                return@breaker
                            }
                            radCircle =
                                Math.sqrt((it.endPoint.x - sx).toDouble().pow(2) + (it.endPoint.y - sy).toDouble().pow(2))
                            if (radCircle < PULLCAP) {
                                ce.startPoint = Point(it.endPoint)
                                val newEl = Pair(ce, true)
                                val oldEl = Pair(it, false)
                                joinElements(newEl, oldEl)
                                return@breaker
                            }
                            radCircle =
                                Math.sqrt((it.endPoint.x - ex).toDouble().pow(2) + (it.endPoint.y - ey).toDouble().pow(2))
                            if (radCircle < PULLCAP) {
                                ce.endPoint = Point(it.endPoint)
                                val newEl = Pair(ce, false)
                                val oldEl = Pair(it, false)
                                joinElements(newEl, oldEl)
                                return@breaker
                            }
                        }
                    }
                }
            else run breaker@{
                elements.forEach {
                    if (ce != it) {
                        if (isStartCatched) {
                            var radCircle = Math.sqrt(
                                (it.startPoint.x - sx).toDouble().pow(2) + (it.startPoint.y - sy).toDouble().pow(2)
                            )
                            if (radCircle < PULLCAP) {
                                ce.startPoint = Point(it.startPoint)
                                val newEl = Pair(ce, true)
                                val oldEl = Pair(it, true)
                                joinElements(newEl, oldEl)
                                return@breaker
                            }
                            radCircle =
                                Math.sqrt((it.endPoint.x - sx).toDouble().pow(2) + (it.endPoint.y - sy).toDouble().pow(2))
                            if (radCircle < PULLCAP) {
                                ce.startPoint = Point(it.endPoint)
                                val newEl = Pair(ce, true)
                                val oldEl = Pair(it, false)
                                joinElements(newEl, oldEl)
                                return@breaker
                            }
                        } else {
                            var radCircle = Math.sqrt(
                                (it.startPoint.x - ex).toDouble().pow(2) + (it.startPoint.y - ey).toDouble().pow(2)
                            )
                            if (radCircle < PULLCAP) {
                                ce.endPoint = Point(it.startPoint)
                                val newEl = Pair(ce, false)
                                val oldEl = Pair(it, true)
                                joinElements(newEl, oldEl)
                                return@breaker
                            }
                            radCircle =
                                Math.sqrt((it.endPoint.x - ex).toDouble().pow(2) + (it.endPoint.y - ey).toDouble().pow(2))
                            if (radCircle < PULLCAP) {
                                ce.endPoint = Point(it.endPoint)
                                val newEl = Pair(ce, false)
                                val oldEl = Pair(it, false)
                                joinElements(newEl, oldEl)
                                return@breaker
                            }
                        }
                    }
                }
            }
        }
        catchedElement = null
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
                if (radCircle < 5)
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

    fun addElement(element: Element) {
        elements.add(element)
    }

    private fun joinElements(newEl: Pair<Element, Boolean>, oldEl: Pair<Element, Boolean>) {
        joints.forEach {
            if (it.elementsJoined.contains(oldEl)) {
                it.elementsJoined.add(newEl)
                if(newEl.second)
                    newEl.first.startJoint = it
                else
                    newEl.first.endJoint = it
                return
            }
        }
        val joint = Joint()
        joint.elementsJoined.add(newEl)
        joint.elementsJoined.add(oldEl)
        joints.add(joint)
        if(newEl.second)
            newEl.first.startJoint = joint
        else
            newEl.first.endJoint = joint
        if(oldEl.second)
            oldEl.first.startJoint = joint
        else
            oldEl.first.endJoint = joint
        
    }

    private fun unjoinElement(el: Pair<Element, Boolean>) {
        if(el.second)
            el.first.startJoint = null
        else
            el.first.endJoint = null
        var emptyJoint: Joint? = null
        joints.forEach {
            if (it.elementsJoined.contains(el)) {
                it.elementsJoined.remove(el)
                if (it.elementsJoined.count() < 2)
                    emptyJoint = it
            }
        }
        emptyJoint?.let {
            if(it.elementsJoined.first().second)
                it.elementsJoined.first().first.startJoint = null
            else
                it.elementsJoined.first().first.endJoint = null
            joints.remove(it)
        }
    }

    fun clearCanvas() {
        elements.clear()
        joints.clear()
        invalidate()
    }
}