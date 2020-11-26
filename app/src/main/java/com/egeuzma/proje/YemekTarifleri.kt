package com.egeuzma.proje

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_yemek_tarifleri.*

class YemekTarifleri : AppCompatActivity() {
    private lateinit var  db : FirebaseFirestore
    var tarifName : ArrayList<String> = ArrayList()
    var adapter : TarifAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yemek_tarifleri)
        db = FirebaseFirestore.getInstance()
        getReceptFromDatabase()

        var layoutManager = LinearLayoutManager(this)
        recyclerView2.layoutManager = layoutManager
        adapter = TarifAdapter(tarifName)
        recyclerView2.adapter = adapter


    }
    fun getReceptFromDatabase(){
      db.collection("YemekTarifleri").addSnapshotListener { snapshot, exception ->
          if(exception!=null){
              Toast.makeText(applicationContext,exception.localizedMessage.toString(),Toast.LENGTH_LONG).show()
          }else{
              if(snapshot!=null){
                  if(!snapshot.isEmpty){
                      tarifName.clear()
                      val documents = snapshot.documents
                      for (document in documents){
                          val isim = document.get("isim") as String
                          tarifName.add(isim)
                          adapter!!.notifyDataSetChanged()
                      }
                  }
              }
          }
      }
    }
    fun goToRecept(view: View){
        val intent = Intent(applicationContext,YemekIcerik::class.java)
        startActivity(intent)
    }
}