package com.egeuzma.proje.model

import android.content.Context
import android.widget.Toast
import com.egeuzma.proje.Controller.YemekIcerik
import com.egeuzma.proje.MyCallBack
import com.egeuzma.proje.RecyclerAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_urun_detayi.*
import kotlinx.android.synthetic.main.activity_urun_ekleme.*
import javax.security.auth.callback.Callback

class Database {
    private  var  db = FirebaseFirestore.getInstance()
     fun getListData(callback:MyCallBack){
         var lists = ArrayList<Any>()
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
    fun getSelectedList(callback: MyCallBack,selectedList:String){
        var lists = ArrayList<Any>()
        db.collection("Listeler").whereEqualTo("isim", selectedList).addSnapshotListener { snapshot, exception ->
            if (snapshot != null) {
                if (!snapshot.isEmpty) {
                    val documents = snapshot.documents
                    for (document in documents) {
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
    fun getRecipes(callback: MyCallBack){
        var tarifler =ArrayList<Any>()
        db.collection("YemekTarifleri").addSnapshotListener { snapshot, exception ->
            if(snapshot!=null){
                if(!snapshot.isEmpty){
                    val documents = snapshot.documents
                    for (document in documents){
                        val isim = document.get("isim") as String
                        val malzeme = document.get("malzemeler") as ArrayList<String>
                        val recept = document.get("tarif") as String
                        // val recept2 =document.get("tarif2") as String
                        var myTarif = YemekTarif(isim, malzeme, recept!!)
                        tarifler.add(myTarif)
                    }
                    callback.onCallback(tarifler)
                }
            }
        }
    }
    fun makeSelectedRecipesToList(selectedRecipe:String){
        var products: ArrayList<HashMap<String,Any>> = ArrayList()
        var malzemeler : ArrayList<String> = ArrayList()
        db.collection("YemekTarifleri").whereEqualTo("isim",selectedRecipe).get().addOnSuccessListener { documents ->
            malzemeler.clear()
            for (document in documents){
                malzemeler = document.get("malzemeler") as ArrayList<String>
            }
            for(malzeme in malzemeler){
                val map = HashMap<String,Any>()
                map.put("UrunAdi",malzeme)
                map.put("UrunAdeti","0")
                map.put("UrunNotu","")
                map.put("isCheck",false)
                products.add(map)
            }
            val listmap = hashMapOf<String,Any>()
            val isim=selectedRecipe + " Listesi"
            listmap.put("isim",isim)
            listmap.put("Urunler",products)
            db.collection("Listeler").document(isim).set(listmap)
        }
    }
    fun getProducts(myCallBack: MyCallBack){
        var productsName = ArrayList<Any>()
        db.collection("Urunler").addSnapshotListener { snapshot, exception ->
            if(snapshot!=null){
                if (!snapshot.isEmpty){
                    productsName.clear()
                    val documents = snapshot.documents
                    for (document in documents){
                        val isim = document.get("Name") as String
                        productsName.add(isim)
                    }
                }
                myCallBack.onCallback(productsName)
            }
        }
    }
}

