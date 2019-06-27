package com.example.canvastest

import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
import com.example.canvastest.Elements.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        circuitView.addElement(FlowSource(Point(100, 100), Point(300, 100)))
        circuitView.addElement(Resistor(Point(100, 300), Point(300, 300)))
        circuitView.addElement(TensionSource(Point(100,500),Point(300,500)))

        nav_view.setNavigationItemSelectedListener(this)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_resistor -> {
                circuitView.addElement(Resistor(Point(100, 100), Point(300, 100)))
            }
            R.id.nav_flow -> {
                circuitView.addElement(FlowSource(Point(100, 100), Point(300, 100)))
            }
            R.id.nav_tension -> {
                circuitView.addElement(TensionSource(Point(100,100),Point(300,100)))
            }
        }
        circuitView.invalidate()
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
