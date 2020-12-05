package com.egeuzma.proje.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.egeuzma.proje.MalzemeAdapter
import com.egeuzma.proje.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_liste_icerik.*

class ListeIcerik : AppCompatActivity() {
    private lateinit var  db : FirebaseFirestore
    var productName : ArrayList<String> = ArrayList()
    var product : ArrayList<String> = ArrayList()
    var adapter : MalzemeAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liste_icerik)
        db = FirebaseFirestore.getInstance()
        val intent = intent
        val selectedList=intent.getStringExtra("isim")
        textView2.text = selectedList
        getFromFirebase(selectedList!!)

        var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = MalzemeAdapter(productName)
        recyclerView.adapter = adapter

    }
    fun getFromFirebase(selectedList :String){
        db.collection("Listeler").whereEqualTo("isim",selectedList).get().addOnSuccessListener { documents ->
            productName.clear()
            for (document in documents){
                if(product.isEmpty()){
                    product = document.get("malzemeler") as ArrayList<String>
                    for (products in product){
                        productName.add(products)
                        // println(products)
                    }
                }
                adapter!!.notifyDataSetChanged()
            }
        }
    }
    fun addProduct(view: View){
        val intent = Intent(applicationContext, UrunEkleme::class.java)
        startActivity(intent)
    }
}