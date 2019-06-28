package com.example.canvastest.elements

class Synapse {
    var elements = mutableListOf<Element>()
    var from : Joint? = null
    var to : Joint? = null
    var isValid = true
    get(){
        var count = 0
        elements.forEach {
            if(it is FlowSource)
                if(++count > 1)return false
        }
        return true
    }
}