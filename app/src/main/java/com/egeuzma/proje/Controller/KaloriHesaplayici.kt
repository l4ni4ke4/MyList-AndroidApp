package com.egeuzma.proje.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.egeuzma.proje.KaloriUrunAdapter
import com.egeuzma.proje.R
import com.google.api.Distribution
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_kalori_hesaplayici.*
import kotlinx.android.synthetic.main.activity_urun_ekleme.*
import java.util.*
import kotlin.collections.ArrayList

var selectedItemsList:ArrayList<String> = ArrayList()
var toplamKalori = 0.0

class KaloriHesaplayici : AppCompatActivity() {

    private lateinit var  db : FirebaseFirestore

    var productsName : ArrayList<String> = ArrayList()
    var productsCalorie :ArrayList<Number> = ArrayList()
    var products1 :ArrayList<String> = ArrayList()
    var calorie1 :ArrayList<Number> = ArrayList()
    var adapter : KaloriUrunAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kalori_hesaplayici)


        db = FirebaseFirestore.getInstance()

        verileriFirestoredanCekme()

        var layoutManager = LinearLayoutManager(this)
        searchResultRecyclerView.layoutManager = layoutManager

        adapter = KaloriUrunAdapter(productsName,productsCalorie,this)
        searchResultRecyclerView.adapter =adapter

        val refreshBtn = findViewById<Button>(R.id.calRefresh)

        refreshBtn.setOnClickListener{
            refreshProducts()
            val intent = intent
            finish()
            startActivity(intent)
        }
        val anasayfabtn = findViewById<Button>(R.id.goHome)
        anasayfabtn.setOnClickListener{
            finish()
            goToHome()
        }

       // val itemViewText = intent.getStringExtra("textToSend")
        var itemsLayout : LinearLayout = findViewById(R.id.kalori_item_layout)

        createDisplayItems(itemsLayout)

      //  createDisplayItems(itemViewText?:"",itemsLayout)


        println(toplamKalori)
        textView23.text = "Toplam Kalori = $toplamKalori"

        searchView2.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!!.isNotEmpty()){
                    productsName.clear()
                    productsCalorie.clear()
                    val search = newText.toLowerCase(Locale.getDefault())
                    products1.forEach {
                        if(it.toLowerCase(Locale.getDefault()).contains(search)){

                            productsCalorie.add(calorie1.get(products1.indexOf(it)))
                            productsName.add(it)

                        }
                        searchResultRecyclerView.adapter!!.notifyDataSetChanged()
                    }

                }else{
                    productsName.clear()
                    productsName.addAll(products1)
                    productsCalorie.clear()
                    productsCalorie.addAll(calorie1)

                    searchResultRecyclerView.adapter!!.notifyDataSetChanged()
                }
                return true
            }

        })
    }


    fun createDisplayItems(itemsLayout:LinearLayout)
    {
        for (str in selectedItemsList){
            createDisplayItem(str?:"",itemsLayout)
        }
    }

     fun createDisplayItem(inputText : String,itemsLayout:LinearLayout)
    {
        val itemRowView = layoutInflater.inflate(R.layout.kalori_items,null)
        val itemRowText: TextView = itemRowView.findViewById(R.id.kal_item_row)
        itemRowText.text = inputText
        itemsLayout.addView(itemRowView)

    }

    fun refreshProducts() {
        selectedItemsList.clear()
        toplamKalori =0.0
    }

    fun goToHome()
    {
        val intent = Intent(applicationContext,
            MainActivity::class.java)

        startActivity(intent)

    }


    fun verileriFirestoredanCekme(){
        db.collection("Urunler").addSnapshotListener { snapshot, exception ->
            if(exception!=null){
                Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }else{
                if(snapshot!=null){
                    if(!snapshot.isEmpty){

                        productsName.clear()

                        products1.clear()

                        val documents = snapshot.documents
                        for (document in documents){
                            //val Category = document.get("Category") as String
                            val Name = document.get("Name") as String
                            val Unit_calorie = document.get("Unit_calorie")?: 0.0
                            /*val Serving_calorie = document.get("Serving_calorie") as String
                            val Serving_name = document.get("Serving_name") as String
                            val Serving_size = document.get("Serving_size") as String
                            val Unit_calorie = document.get("Unit_calorie") as String
                            val Unit_type = document.get("Unit_type") as String*/

                           // println(Name)
                            productsName.add(Name)
                            products1.add(Name)
                            productsCalorie.add(Unit_calorie as Number)
                            calorie1.add(Unit_calorie as Number)

                            //productsCalorie.add(Unit_calorie)

                            //adapter!!.notifyDataSetChanged()

                            /* var myTarif = YemekTarif(
                                isim,
                                malzeme,
                                recept
                            )
                            //tarifName.add(isim)
                            tarifler.add(myTarif)*/
                            adapter!!.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }
}