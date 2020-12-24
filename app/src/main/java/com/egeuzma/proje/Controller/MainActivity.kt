package com.egeuzma.proje.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.egeuzma.proje.R
import com.egeuzma.proje.RecyclerAdapter
import com.egeuzma.proje.model.Liste
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_liste_icerik.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.change_liste_ismi.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private lateinit var  db : FirebaseFirestore
    var malzeme : ArrayList<HashMap<String,Any>> = ArrayList()
    var listName : ArrayList<String> = ArrayList()
    var lists :ArrayList<Liste> = ArrayList()
    var adapter : RecyclerAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button2.isEnabled= false
        db = FirebaseFirestore.getInstance()
        //var Language = Locale.getDefault().getLanguage()
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
                if(snapshot!=null){
                    if (!snapshot.isEmpty){
                       listName.clear()
                        lists.clear()
                        val documents = snapshot.documents
                        for (document in documents){
                            val isim = document.get("isim") as String
                            val malzeme = document.get("Urunler") as ArrayList<HashMap<String, Any>>
                            var myList = Liste(isim, malzeme)
                            lists.add(myList)
                            listName.add(isim)

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

       val alert = AlertDialog.Builder(this)
       val view = layoutInflater.inflate(R.layout.change_liste_ismi,null)
       val textview = view.findViewById<TextView>(R.id.textView5)
       val but = view.findViewById<Button>(R.id.button7)
       val edittext = view.findViewById<EditText>(R.id.yeniListe)
       alert.setView(view)
       val dialog = alert.create()
       dialog.show()
       but.setOnClickListener(object : View.OnClickListener{
           override fun onClick(v: View?) {
               val map = HashMap<String,Any>()
               map.put("isim",edittext.text.toString())
               map.put("Urunler",malzeme)
               println(edittext.text)
               //db.collection("Listeler").document(edittext.text.toString()).set(map)
               if(listName.contains(edittext.text.toString())){
                   textview.text=resources.getString(R.string.toast1)
               }else{
                   db.collection("Listeler").document(edittext.text.toString()).set(map)
                   val intent = Intent(applicationContext,ListeIcerik::class.java)
                   intent.putExtra("info","new")
                   intent.putExtra("isim",edittext.text.toString())
                   startActivity(intent)
                   dialog.cancel()
               }
           }
       })
   }
}