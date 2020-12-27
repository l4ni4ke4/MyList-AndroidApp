package com.egeuzma.proje.Controller

import android.R.attr
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egeuzma.proje.MyCallBack
import com.egeuzma.proje.R
import com.egeuzma.proje.RecyclerAdapter
import com.egeuzma.proje.UrunAdapter
import com.egeuzma.proje.model.Database
import com.egeuzma.proje.model.Liste
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_urun_ekleme.*
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.ArrayList


class UrunEkleme : AppCompatActivity() {
    private lateinit var  db : FirebaseFirestore
    var productsName : ArrayList<String> = ArrayList()
    var products :ArrayList<String> = ArrayList()
    var products1 :ArrayList<String> = ArrayList()
    var adapter : UrunAdapter?=null
    var productskategori :ArrayList<String> = ArrayList()
    var isim: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_urun_ekleme)
        db = FirebaseFirestore.getInstance()
        val intent = intent
        isim= intent.getStringExtra("isim")
        getProducts()
        recyclerViewProduct.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
               var imm =  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v!!.getWindowToken(), 0);

                return false;
            }
        });

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.category,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       if(item.title.toString() == "Tüm Ürünler" || item.title.toString() == "All Products"){
           textView6.visibility=View.INVISIBLE
           searchView.visibility=View.VISIBLE
           getProducts()
       }else{
           //Kategorileri Ingilizce Eklemeyi unutmamak lazim
           textView6.visibility=View.VISIBLE
           textView6.text=item.title
           searchView.visibility=View.INVISIBLE
           getCategory(item.title.toString())
       }
        return super.onOptionsItemSelected(item)
    }
    fun getCategory(kategori:String) {
        db.collection("Urunler").whereEqualTo("Category", kategori).addSnapshotListener { snapshot, exception ->
            if (snapshot != null) {
                if (!snapshot.isEmpty) {
                    productsName.clear()
                    val documents = snapshot.documents
                    for (document in documents) {
                        val isim = document.get("Name") as String
                        productsName.add(isim)
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    fun getProducts(){
        var database = Database()
        database.getProducts(object : MyCallBack {
            override fun onCallback(value: ArrayList<Any>) {
                productsName.clear()
                products.clear()
                products1.clear()
                productsName = value as ArrayList<String>
                for (productsname in productsName) {
                    products.add(productsname)
                    products1.add(productsname)
                }
                var layoutManager = LinearLayoutManager(this@UrunEkleme)
                recyclerViewProduct.layoutManager = layoutManager
                adapter = UrunAdapter(productsName,isim!!)
                recyclerViewProduct.adapter = adapter
            }
        })
    }

}