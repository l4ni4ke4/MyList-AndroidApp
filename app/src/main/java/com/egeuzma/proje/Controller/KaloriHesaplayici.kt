package com.egeuzma.proje.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.egeuzma.proje.KaloriUrunAdapter
import com.egeuzma.proje.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_kalori_hesaplayici.*
import kotlinx.android.synthetic.main.activity_urun_ekleme.*

class KaloriHesaplayici : AppCompatActivity() {

    private lateinit var  db : FirebaseFirestore
   // private lateinit var binding: ActivityMainBinding



    var productsName : ArrayList<String> = ArrayList()
    var productsCalorie :ArrayList<Number> = ArrayList()

    var adapter : KaloriUrunAdapter? = null

    var toplamKalori = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kalori_hesaplayici)


        db = FirebaseFirestore.getInstance()

        verileriFirestoredanCekme()

        var layoutManager = LinearLayoutManager(this)
        searchResultRecyclerView.layoutManager = layoutManager

        adapter = KaloriUrunAdapter(productsName,this)
        searchResultRecyclerView.adapter =adapter

        val itemViewText = intent.getStringExtra("textToSend")
        createDisplayItems(itemViewText?:"null")

        toplamKalori = 15
        println(toplamKalori)
        textView23.text = "Toplam Kalori = $toplamKalori"
    }

     fun createDisplayItems(inputText : String)
    {
        var itemview = findViewById<TextView>(R.id.kal_item_row2)
        itemview.text = inputText

    }


    fun verileriFirestoredanCekme(){
        db.collection("Urunler").addSnapshotListener { snapshot, exception ->
            if(exception!=null){
                Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }else{
                if(snapshot!=null){
                    if(!snapshot.isEmpty){

                        val documents = snapshot.documents
                        for (document in documents){
                            //val Category = document.get("Category") as String
                            val Name = document.get("Name") as String
                            /*val Serving_calorie = document.get("Serving_calorie") as String
                            val Serving_name = document.get("Serving_name") as String
                            val Serving_size = document.get("Serving_size") as String
                            val Unit_calorie = document.get("Unit_calorie") as String
                            val Unit_type = document.get("Unit_type") as String*/

                            println(Name)
                            productsName.add(Name)
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