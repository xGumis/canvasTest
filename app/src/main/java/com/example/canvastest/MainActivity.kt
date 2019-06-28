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
import kotlinx.android.synthetic.main.circuit_view.*
import kotlinx.android.synthetic.main.main_content.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var buttonsVisibility: Boolean = false
        set(value) {
            field = value
            if (value) {
                fab_delete.show()
                fab_edit.show()
            } else {
                fab_delete.hide()
                fab_edit.hide()
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        circuitView.addElement(FlowSource(Point(100, 100), Point(300, 100)))
        circuitView.addElement(Resistor(Point(100, 300), Point(300, 300)))
        circuitView.addElement(TensionSource(Point(100, 500), Point(300, 500)))

        nav_view.setNavigationItemSelectedListener(this)
        buttonsVisibility = false
        circuitView.onSelectedChange = {
            buttonsVisibility = it
        }
        fab_delete.setOnClickListener {
            circuitView.onDeleteSelectedView()
        }
        fab_edit.setOnClickListener {
            circuitView.onEditSelectedView()
        }


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
            }
            R.id.nav_play -> {
                val synapses = circuitView.findSynapses()
                translateElements(synapses)
                mNode.updatePotential()
            }
        }
        circuitView.invalidate()
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun translateElements(arr: ArrayList<Synapse>) {
        val translateNodeArray: MutableMap<Joint, mNode> = mutableMapOf()
        circuitView.nodes.forEach {
            translateNodeArray.put(it, mNode())
        }
        if (arr.size > 0) {
            arr.forEach {
                if (it.isValid) {
                    val synapse = mSynapse()
                    synapse.from = translateNodeArray[it.from]
                    synapse.to = translateNodeArray[it.to]
                    for (i in 0..it.elements.size - 1) {
                        if (it.elements[i] is Resistor) {
                            val resistor = mResistor()
                            resistor.resistanceValue = (it.elements[i] as Resistor).resistance
                            synapse.resistorArray.add(resistor)
                        } else if (it.elements[i] is FlowSource) {
                            val source = (it.elements[i] as FlowSource)
                            if (i > 0) {
                                if (source.startJoint == it.elements[i - 1].endJoint || source.startJoint == it.elements[i - 1].startJoint) {
                                    val flowSource = mFlowSource(source.flowValue, synapse.from)
                                    synapse.flowSource = flowSource
                                } else {
                                    val flowSource = mFlowSource(source.flowValue, synapse.to)
                                    synapse.flowSource = flowSource
                                }
                            } else {
                                if (source.startJoint == it.from) {
                                    val flowSource = mFlowSource(source.flowValue, synapse.from)
                                    synapse.flowSource = flowSource
                                } else {
                                    val flowSource = mFlowSource(source.flowValue, synapse.to)
                                    synapse.flowSource = flowSource
                                }
                            }
                        } else if(it.elements[i] is TensionSource){
                            val source = (it.elements[i] as TensionSource)
                            if (i > 0) {
                                if (source.startJoint == it.elements[i - 1].endJoint || source.startJoint == it.elements[i - 1].startJoint) {
                                    val tensionSource = mTensionSource(source.tensionValue, synapse.from)
                                    synapse.tensionArray.add(tensionSource)
                                } else {
                                    val tensionSource = mTensionSource(source.tensionValue, synapse.to)
                                    synapse.tensionArray.add(tensionSource)
                                }
                            } else {
                                if (source.startJoint == it.from) {
                                    val tensionSource = mTensionSource(source.tensionValue, synapse.from)
                                    synapse.tensionArray.add(tensionSource)
                                } else {
                                    val tensionSource = mTensionSource(source.tensionValue, synapse.to)
                                    synapse.tensionArray.add(tensionSource)
                                }
                            }
                        }

                    }
                }
            }
        }
        translateNodeArray.forEach{
            mNode.nodeList.add(it.value)
        }
        mNode.groundedNode = mNode.nodeList.firstOrNull()
    }

}
