package com.egeuzma.proje.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.egeuzma.proje.MyCallBack
import com.egeuzma.proje.R
import com.egeuzma.proje.RecyclerAdapter
import com.egeuzma.proje.TarifAdapter
import com.egeuzma.proje.model.Database
import com.egeuzma.proje.model.Liste
import com.egeuzma.proje.model.YemekTarif
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_yemek_tarifleri.*

class YemekTarifleri : AppCompatActivity() {
    private lateinit var  db : FirebaseFirestore
    var tarifler : ArrayList<YemekTarif> = ArrayList()
    var adapter : TarifAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yemek_tarifleri)
        db = FirebaseFirestore.getInstance()
        getData()
    }
    fun getData(){
        var database = Database()
        database.getRecipes(object : MyCallBack {
            override fun onCallback(value: ArrayList<Any>) {
                tarifler.clear()
                tarifler = value as ArrayList<YemekTarif>
                var layoutManager = LinearLayoutManager(this@YemekTarifleri)
                recyclerView2.layoutManager = layoutManager
                adapter = TarifAdapter(tarifler)
                recyclerView2.adapter = adapter
            }
        })
    }
}