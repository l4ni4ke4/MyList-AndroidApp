package com.egeuzma.proje.model

import java.io.Serializable

class Liste(name : String , malzemeler : ArrayList<HashMap<String, Any>>) : Serializable{
     var isim :String ? = name
         private set
         get
     var malzeme : ArrayList<HashMap<String, Any>> = malzemeler
         private set
         get
}