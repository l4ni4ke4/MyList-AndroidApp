package com.egeuzma.proje.Controller

import android.R.attr
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egeuzma.proje.R
import com.egeuzma.proje.UrunAdapter
import com.google.firebase.firestore.FirebaseFirestore
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
        return super.onOptionsItemSelected(item)
    }
    fun getProductData(){
        db.collection("Urunler").addSnapshotListener { snapshot, exception ->
            if(exception != null){
                Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }else{
                if(snapshot!=null){
                    if (!snapshot.isEmpty){
                        productsName.clear()
                        products.clear()
                        products1.clear()
                        val documents = snapshot.documents
                        for (document in documents){
                            val isim = document.get("Name") as String
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