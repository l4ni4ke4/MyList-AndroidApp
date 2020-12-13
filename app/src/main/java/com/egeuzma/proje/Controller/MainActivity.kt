package com.egeuzma.proje.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.egeuzma.proje.R
import com.egeuzma.proje.RecyclerAdapter
import com.egeuzma.proje.model.Liste
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var  db : FirebaseFirestore
    var listName : ArrayList<String> = ArrayList()
    var lists :ArrayList<Liste> = ArrayList()
    var adapter : RecyclerAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button2.isEnabled= false
        db = FirebaseFirestore.getInstance()

        getData()
        var layoutManager = LinearLayoutManager(this)
        recyclerView3.layoutManager = layoutManager
       // adapter = RecyclerAdapter(listName)
        adapter = RecyclerAdapter(lists)
        recyclerView3.adapter = adapter
    }
    fun getData(){
        db.collection("Listeler").addSnapshotListener { snapshot, exception ->
            if(exception != null){
                Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }else{
                println("Database bağlantısı sağlandı.")
                if(snapshot!=null){
                    if (!snapshot.isEmpty){
                       // listName.clear()
                        lists.clear()
                        val documents = snapshot.documents
                        for (document in documents){
                            val isim = document.get("isim") as String
                            val malzeme = document.get("malzemeler") as ArrayList<String>
                            var myList =
                                Liste(isim, malzeme)
                            lists.add(myList)
                            //val isim = document.get("isim") as String
                            // println(isim)
                            //listName.add(isim)
                            adapter!!.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }
    fun goToYemekTarif(view :View){
        val intent = Intent(applicationContext,
            YemekTarifleri::class.java)
        startActivity(intent)
    }
    fun goToKalori(view: View){
        val intent = Intent(applicationContext,
            KaloriHesaplayici::class.java)
        startActivity(intent)
    }
   fun createNewList(view: View){
     val intent = Intent(applicationContext,ListeIcerik::class.java)
       intent.putExtra("info","new")
       startActivity(intent)
   }

}