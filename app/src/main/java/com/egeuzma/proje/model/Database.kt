package com.egeuzma.proje.model

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egeuzma.proje.Controller.ListeIcerik
import com.egeuzma.proje.Controller.YemekIcerik
import com.egeuzma.proje.MalzemeAdapter
import com.egeuzma.proje.MyCallBack
import com.egeuzma.proje.R
import com.egeuzma.proje.RecyclerAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_liste_icerik.*
import kotlinx.android.synthetic.main.activity_urun_detayi.*
import kotlinx.android.synthetic.main.activity_urun_ekleme.*
import kotlinx.android.synthetic.main.activity_yemek_icerik.*
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
    fun addCheckedProductToList(context: Context, urunmap:HashMap<String,Any>,index: Int,liste:String) {
        var db = FirebaseFirestore.getInstance()

        var products : ArrayList<HashMap<String,Any>> = ArrayList()
        db.collection("Listeler").whereEqualTo("isim",liste).get().addOnSuccessListener { documents->
            for(document in documents){
                products=document.get("Urunler") as ArrayList<HashMap<String, Any>>
            }
            products.set(index,urunmap)
            val listmap = hashMapOf<String, Any>()
            listmap.put("Urunler", products)
            listmap.put("isim", liste)
            db.collection("Listeler").document(liste).set(listmap)
            (context as Activity).recreate()
        }
    }
    fun deleteProduct(liste: String,textView9:String){
        var deneme:ArrayList<HashMap<String,Any>>
        var deneme1: ArrayList<HashMap<String,Any>>
        db.collection("Listeler").whereEqualTo("isim",liste).get().addOnSuccessListener { documents ->
            for (document in documents) {
                deneme = document.get("Urunler") as ArrayList<HashMap<String, Any>>
                deneme1 = deneme.clone() as ArrayList<HashMap<String, Any>>
                for (x in deneme){
                    if(x.getValue("UrunAdi").toString()==textView9){
                        deneme1.remove(x)
                        val listmap = hashMapOf<String,Any>()
                        listmap.put("isim", liste!!)
                        listmap.put("Urunler",deneme1)
                        db.collection("Listeler").document(liste).set(listmap)
                    }
                }
            }
        }
    }
    fun saveProduct(liste:String,textView9:String,textView11:String,textView12:String){
        var count=0
        var deneme:ArrayList<HashMap<String,Any>>
        var deneme1: ArrayList<HashMap<String,Any>>
        db.collection("Listeler").whereEqualTo("isim", liste).get().addOnSuccessListener { documents ->
            for (document in documents) {
                deneme = document.get("Urunler") as ArrayList<HashMap<String, Any>>
                deneme1 = deneme.clone() as ArrayList<HashMap<String, Any>>
                for (x in deneme) {
                    if (x.getValue("UrunAdi").toString() == textView9) {
                        x.put("UrunAdi", textView9)
                        x.put("UrunAdeti", textView11)
                        x.put("UrunNotu", textView12)
                        deneme1.set(count, x)
                        val listmap = hashMapOf<String, Any>()
                        listmap.put("Urunler", deneme1)
                        listmap.put("isim", liste!!)
                        println(listmap)
                        db.collection("Listeler").document(liste).set(listmap)
                    }
                    count++
                }
            }
        }
    }
    fun addProductToList(context: Context,urunmap:HashMap<String,Any>,isim:String) {
        var products : ArrayList<HashMap<String,Any>> = ArrayList()
        var count =0
        db.collection("Listeler").whereEqualTo("isim",isim).get().addOnSuccessListener { documents ->
            for (document in documents){
                products = document.get("Urunler") as ArrayList<HashMap<String, Any>>
            }
            for (product in products){
                if (product.getValue("UrunAdi").toString()==urunmap.getValue("UrunAdi").toString()){
                    Toast.makeText(context, R.string.toast2,Toast.LENGTH_SHORT).show()
                    count=1
                }
            }
            if(count==0){
                products.add(urunmap)
                val listmap = hashMapOf<String, Any>()
                listmap.put("Urunler", products)
                listmap.put("isim", isim)
                db.collection("Listeler").document(isim).set(listmap)
                var toast= context.resources.getString(R.string.toast3)

                Toast.makeText(context,urunmap.getValue("UrunAdi").toString()+" "+toast,Toast.LENGTH_SHORT).show()
            }

        }

    }
    fun getReceptFromFirebase(selectedTarif :String,textView17:TextView,imageView:ImageView){
        db.collection("YemekTarifleri").whereEqualTo("isim",selectedTarif).get().addOnSuccessListener { documents ->
            for (document in documents){
                val tarif = document.get("tarif") as String
                val imageUrl = document.get("url") as String
                textView17.text=tarif
                Picasso.get().load(imageUrl).into(imageView)
            }
        }
    }
}

