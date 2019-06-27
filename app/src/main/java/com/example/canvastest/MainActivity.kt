package com.example.canvastest

import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.canvastest.Elements.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        circuitView.addElement(FlowSource(Point(100, 100), Point(300, 100)))
        circuitView.addElement(Resistor(Point(100, 300), Point(300, 300)))
    }
}
