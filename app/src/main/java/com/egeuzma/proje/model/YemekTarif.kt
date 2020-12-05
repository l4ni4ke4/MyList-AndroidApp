package com.egeuzma.proje.model

import java.io.Serializable

class YemekTarif(name : String , malzemeler : ArrayList<String>,recept : String) :Serializable{
    var isim :String ? = name
        private set
        get
    var malzeme : ArrayList<String> = malzemeler
        private set
        get
    var recept :String ? = name
        private set
        get
}