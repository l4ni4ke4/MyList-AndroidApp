package com.egeuzma.proje.model

import java.io.Serializable

class Liste(name : String , malzemeler : ArrayList<String>) : Serializable{
     var isim :String ? = name
         private set
         get
     var malzeme : ArrayList<String> = malzemeler
         private set
         get
}