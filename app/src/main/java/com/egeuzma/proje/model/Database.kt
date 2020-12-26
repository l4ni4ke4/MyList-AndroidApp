package com.egeuzma.proje.model

import android.content.Context
import android.widget.Toast
import com.egeuzma.proje.MyCallBack
import com.egeuzma.proje.RecyclerAdapter
import com.google.firebase.firestore.FirebaseFirestore
import javax.security.auth.callback.Callback

class Database {
    private  var  db = FirebaseFirestore.getInstance()
     fun getListData(callback:MyCallBack){
         var lists = ArrayList<Liste>()
         db.collection("Listeler").addSnapshotListener { snapshot, exception ->
                 if(snapshot!=null){
                     if (!snapshot.isEmpty){
                         val documents = snapshot.documents
                         for (document in documents){
                             val isim = document.get("isim") as String
                             val malzeme = document.get("Urunler") as ArrayList<HashMap<String, Any>>
                             var myList = Liste(isim, malzeme)
                             lists.add(myList)
                         }
                         callback.onCallback(lists)
                     }
                 }
         }
    }
}

