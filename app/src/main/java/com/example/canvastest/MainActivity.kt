package com.example.canvastest

import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.canvastest.elements.*
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
import com.example.canvastest.model.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var nodeMap = mutableMapOf<Int, mNode>()
    private var synapses: MutableList<mSynapse> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        circuitView.addElement(FlowSource(Point(100, 100), Point(300, 100)))
        circuitView.addElement(Resistor(Point(100, 300), Point(300, 300)))
        circuitView.addElement(TensionSource(Point(100, 500), Point(300, 500)))

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
                circuitView.addElement(TensionSource(Point(100, 100), Point(300, 100)))
                findSynapses()
            }
        }
        circuitView.invalidate()
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun findSynapses() {
        findNodes()
        var chckd = mutableMapOf<Int, ArrayList<Int>>()
        nodeMap.forEach { chckd[it.key] = arrayListOf() }
        nodeMap.forEach {
            val start = it.key
            circuitView.joints[start].elementsJoined.forEach {
                var ind = circuitView.joints[start].elementsJoined.indexOf(it)
                if (!chckd[start]!!.contains(ind)) {
                    chckd[start]!!.add(ind)
                    var pair = it
                    while (true) {
                        if (pair.second)
                            if (pair.first.endJoint != null) {
                                pair.first.endJoint?.let { joint ->
                                    var i = circuitView.joints.indexOf(joint)
                                    if ( nodeMap.keys.contains(i)) {
                                        ind = circuitView.joints[i].elementsJoined.indexOf(Pair(pair.first, false))
                                        chckd[i]!!.add(ind)
                                        val synapse = mSynapse()
                                        synapse.to = nodeMap[i]
                                        nodeMap[i]!!.synapses.add(synapse)
                                        synapse.from = nodeMap[start]
                                        nodeMap[start]!!.synapses.add(synapse)
                                    } else {
                                        joint.elementsJoined.forEach {
                                            if (pair != it)
                                                pair = it
                                        }
                                    }
                                }
                            } else break
                        else if (pair.first.startJoint != null) {
                            pair.first.startJoint?.let { joint ->
                                var i = circuitView.joints.indexOf(joint)
                                if ( nodeMap.keys.contains(i)) {
                                    ind = circuitView.joints[i].elementsJoined.indexOf(Pair(pair.first, false))
                                    chckd[i]!!.add(ind)
                                    val synapse = mSynapse()
                                    synapse.to = nodeMap[i]
                                    nodeMap[i]!!.synapses.add(synapse)
                                    synapse.from = nodeMap[start]
                                    nodeMap[start]!!.synapses.add(synapse)
                                } else {
                                    joint.elementsJoined.forEach {
                                        if (pair != it)
                                            pair = it
                                    }
                                }
                            }
                        } else break
                    }
                }
            }
        }
    }

    private fun findNodes(){
        for (i in 0..circuitView.joints.count() - 1)
            if (circuitView.joints[i].elementsJoined.count() > 2) {
                nodeMap[i] = mNode()
            }
    }
}
