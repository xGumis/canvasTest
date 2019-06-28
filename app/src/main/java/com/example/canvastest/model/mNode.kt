package com.example.canvastest.model

class mNode {
    companion object {
        var nodeList = mutableListOf<mNode>()
        fun mergeNodes(): MutableMap<mNode, Pair<mNode, Double>> {
            val tmpNodeList = mutableMapOf<mNode, Pair<mNode, Double>>()
            nodeList.forEach { node ->
                val synapseToDel = mutableListOf<mSynapse>()
                node.synapses.forEach { synapse ->
                    if (synapse.flowSource == null)
                        if (synapse.resistance == 0.0) {
                            var nodeOrNull: mNode?
                            if (synapse.from == node)
                                nodeOrNull = synapse.to
                            else
                                nodeOrNull = synapse.from
                            nodeOrNull?.let { nodeX ->
                                var tens = 0.0
                                synapse.tensionArray.forEach { ts ->
                                    if (ts.from != node)
                                        tens -= ts.tensionValue
                                    else tens += ts.tensionValue
                                }
                                tmpNodeList[nodeX] = Pair(node,tens)
                                nodeX.synapses.forEach {
                                    if (it.from != node && it.to != node) {
                                        if (it.from == nodeX) {
                                            synapse.tensionArray.forEach { ts ->
                                                if (ts.from != node)
                                                    ts.from = it.to
                                                it.tensionArray.add(ts)
                                            }
                                            it.from = node
                                        } else {
                                            synapse.tensionArray.forEach { ts ->
                                                if (ts.from != node)
                                                    ts.from = it.from
                                                it.tensionArray.add(ts)
                                            }
                                            it.to = node
                                        }
                                        node.synapses.add(it)
                                    }
                                }
                            }
                           synapseToDel.add(synapse)
                        }
                }
                synapseToDel.forEach {
                    node.synapses.remove(it)
                }
            }
            tmpNodeList.keys.forEach{
                nodeList.remove(it)
            }
            return tmpNodeList
        }

        fun updatePotential() {
            val loneNodes = mergeNodes()
            var matrix = Array(nodeList.size - 1,{i -> DoubleArray(nodeList.size - 1) })
            var equal = DoubleArray(nodeList.size - 1)

            var rowID = 0
            var columnId: Int
            nodeList.forEach { row ->
                columnId = 0
                if (row != groundedNode) {
                    nodeList.forEach { column ->
                        if (column != groundedNode) {
                            //dodawanie na przekątnych
                            if (column == row) {
                                row.synapses.forEach {
                                    if (it.flowSource == null)
                                        matrix[rowID][columnId] += 1 / it.resistance
                                }
                            } else {
                                //odejmowanie połączonych
                                row.synapses.forEach {
                                    if (it.flowSource == null && (it.from == row && it.to == column) || (it.from == column && it.to == row)) {

                                        matrix[rowID][columnId] -= 1 / it.resistance
                                    }
                                }
                            }
                            columnId++
                        }

                    }
                    //
                    row.synapses.forEach {
                        // jesli płynie od to -
                        if (it.from == row) {
                            equal[rowID] -= it.flow
                        }
                        // jesli płydie do to +
                        else
                            equal[rowID] += it.flow
                    }
                    rowID++
                }
            }

            var result = mSolver.solve(matrix, equal)
            nodeList.forEachIndexed { i, it ->
                if (groundedNode != it)
                    it.potential = result[i]
            }
            groundedNode?.potential = 0.0
            loneNodes.forEach {
                it.key.potential = it.value.first.potential + it.value.second
            }
        }
        var groundedNode: mNode? = null
    }

    init {
        nodeList.add(this)
        if (groundedNode == null)
            groundedNode = this
    }

    var synapses: MutableList<mSynapse> = mutableListOf()

    var potential: Double = 0.0
    fun destroy() {
        nodeList.remove(this)
        if (groundedNode == this) {
            if (nodeList.size > 0)
                groundedNode = nodeList.first()
            else {
                groundedNode = null
            }
        }
    }
}