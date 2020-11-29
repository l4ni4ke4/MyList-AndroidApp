package com.egeuzma.proje

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var  db : FirebaseFirestore
    var listName : ArrayList<String> = ArrayList()

    var adapter : RecyclerAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button2.isEnabled= false
        db = FirebaseFirestore.getInstance()

        getData()
        var layoutManager = LinearLayoutManager(this)
        recyclerView3.layoutManager = layoutManager
        adapter = RecyclerAdapter(listName)
        recyclerView3.adapter = adapter
    }
    fun getData(){
        db.collection("Listeler").addSnapshotListener { snapshot, exception ->
            if(exception != null){
                Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }else{
                if(snapshot!=null){
                    if (!snapshot.isEmpty){
                        listName.clear()
                        val documents = snapshot.documents
                        for (document in documents){
                            val isim = document.get("isim") as String
                           // println(isim)
                            listName.add(isim)
                            adapter!!.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }
    fun goToYemekTarif(view :View){
        val intent = Intent(applicationContext,YemekTarifleri::class.java)
        startActivity(intent)
    }
    fun goToKalori(view: View){
        val intent = Intent(applicationContext,KaloriHesaplayici::class.java)
        startActivity(intent)
    }
   // fun createNewList(view: View){
     // val intent = Intent(applicationContext,ListeIcerik::class.java)
       //startActivity(intent)
   //}

}