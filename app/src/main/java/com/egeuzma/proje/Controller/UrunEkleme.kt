package com.egeuzma.proje.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.egeuzma.proje.R
import com.egeuzma.proje.RecyclerAdapter
import com.egeuzma.proje.UrunAdapter
import com.egeuzma.proje.model.Liste
import com.egeuzma.proje.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_urun_ekleme.*
import java.util.*
import kotlin.collections.ArrayList

class UrunEkleme : AppCompatActivity() {
    private lateinit var  db : FirebaseFirestore
    var productsName : ArrayList<String> = ArrayList()
    var products :ArrayList<String> = ArrayList()
    var products1 :ArrayList<String> = ArrayList()
    var adapter : UrunAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_urun_ekleme)
        db = FirebaseFirestore.getInstance()
        getProductData()
        var layoutManager = LinearLayoutManager(this)
        recyclerViewProduct.layoutManager = layoutManager
        // adapter = RecyclerAdapter(listName)

        adapter = UrunAdapter(productsName)
        recyclerViewProduct.adapter = adapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!!.isNotEmpty()){
                    productsName.clear()
                    val search = newText.toLowerCase(Locale.getDefault())
                    products1.forEach {
                        if(it.toLowerCase(Locale.getDefault()).contains(search)){
                            productsName.add(it)
                        }
                        recyclerViewProduct.adapter!!.notifyDataSetChanged()
                    }
                }else{
                    productsName.clear()
                    productsName.addAll(products1)
                    recyclerViewProduct.adapter!!.notifyDataSetChanged()
                }
                return true
            }

        })

    }
    fun getProductData(){
        db.collection("Urunler").addSnapshotListener { snapshot, exception ->
            if(exception != null){
                Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }else{

                if(snapshot!=null){
                    if (!snapshot.isEmpty){
                        // listName.clear()
                        productsName.clear()
                        products.clear()
                        products1.clear()
                        val documents = snapshot.documents
                        for (document in documents){
                            val isim = document.get("Name") as String
                           /* val unittype = document.get("malzemeler") as String
                            val unitCalorie = document.get("malzemeler") as Float
                            val servingName = document.get("malzemeler") as String
                            val servingSize = document.get("malzemeler") as Float
                            val category = document.get("malzemeler") as String
                            var myProduct = Product(isim, unittype,unitCalorie,servingName,servingSize,category)*/
                            productsName.add(isim)
                            products.add(isim)
                            products1.add(isim)
                            adapter!!.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }
}