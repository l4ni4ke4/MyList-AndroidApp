package com.egeuzma.proje

import com.egeuzma.proje.model.Liste
import com.egeuzma.proje.model.YemekTarif

interface MyCallBack {
    fun onCallback(value: ArrayList<Any>)
    fun onCallback(name: ArrayList<String>, number:ArrayList<String>,not:ArrayList<String>,check:ArrayList<Boolean>)
}