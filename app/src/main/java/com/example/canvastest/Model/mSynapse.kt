package com.example.canvastest.Model
class mSynapse
{
    var from :mNode? = null
    var to :mNode? = null
    var flowSource :mFlowSource? = null

    var resistorArray = mutableListOf<mResistor>()
    var tensionArray = mutableListOf<mTensionSoure>()
    var resistance :Double = 0.0
        get() {
            var res = 0.0
            resistorArray.forEach{
                res += it.resistanceValue
            }
            return res
        }

    var tension :Double = 0.0
        get() {
            var tens = 0.0
            tensionArray.forEach{
                if(it.from == from)
                tens += it.tensionValue
                else tens -= it.tensionValue
            }
            return tens
        }
    var flow :Double = 0.0
        get() {
            flowSource?.let {
                    if(it.from == from)
                        return it.flowValue
                    return -it.flowValue
                }
            return tension/resistance
        }
}