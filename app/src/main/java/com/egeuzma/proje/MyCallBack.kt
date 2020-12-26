package com.egeuzma.proje

import com.egeuzma.proje.model.Liste
import com.egeuzma.proje.model.YemekTarif

interface MyCallBack {
    fun onCallback(value: ArrayList<Liste>)
    fun onCallbackYemek(value:ArrayList<YemekTarif>)
}