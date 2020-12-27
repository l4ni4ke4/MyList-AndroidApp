package com.egeuzma.proje.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.egeuzma.proje.MyCallBack
import com.egeuzma.proje.R
import com.egeuzma.proje.model.Database
import com.egeuzma.proje.model.Liste
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_urun_detayi.*

class UrunDetayi : AppCompatActivity() {
    private lateinit var  db : FirebaseFirestore
    private  var liste : String? = null
    private  var selectedList : String? = null
    var deneme: ArrayList<HashMap<String,Any>> = ArrayList()
    var deneme1: ArrayList<HashMap<String,Any>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_urun_detayi)
        db = FirebaseFirestore.getInstance()
        val intent = intent
        selectedList=intent.getStringExtra("isim")
        var adet=intent.getStringExtra("adet")
        var not = intent.getStringExtra("not")
        liste = intent.getStringExtra("liste")
        textView9.text=selectedList
        textView11.text=adet
        textView12.text=not
    }
    fun addAdet(view: View){
        val alert = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.change_adet,null)
        val textview = view.findViewById<TextView>(R.id.textView22)
        val but = view.findViewById<Button>(R.id.button22)
        val edittext = view.findViewById<EditText>(R.id.editTextNumber)
        alert.setView(view)
        val dialog = alert.create()
        dialog.show()
        but.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                textView11.text=edittext.text.toString()
                dialog.cancel()
            }
        })
    }
    fun addNote(view: View){
        val alert = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.change_not,null)
        val textview = view.findViewById<TextView>(R.id.textView23)
        val but = view.findViewById<Button>(R.id.button23)
        val edittext = view.findViewById<EditText>(R.id.editTextTextMultiLine)
        alert.setView(view)
        val dialog = alert.create()
        dialog.show()
        but.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                textView12.text=edittext.text
                dialog.cancel()
            }
        })
    }
    fun deleteProduct(view: View){
        db.collection("Listeler").whereEqualTo("isim",liste).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage.toString(),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if (snapshot != null) {
                    if (!snapshot.isEmpty) {
                        val documents = snapshot.documents
                        for (document in documents) {
                            deneme = document.get("Urunler") as ArrayList<HashMap<String, Any>>
                            deneme1 = deneme.clone() as ArrayList<HashMap<String, Any>>
                            for (x in deneme){
                                if(x.getValue("UrunAdi").toString()==textView9.text){
                                    deneme1.remove(x)
                                    val listmap = hashMapOf<String,Any>()
                                    listmap.put("isim", liste!!)
                                    listmap.put("Urunler",deneme1)
                                    addDatabase(listmap,liste!!)

                                }
                            }
                        }
                    }
                }

            }
        }
    }
    fun addDatabase(listmap : HashMap<String,Any>,isim:String){
        db.collection("Listeler").document(isim).set(listmap)
        val intent = Intent(applicationContext,
            ListeIcerik::class.java)
        intent.putExtra("info","old")
        intent.putExtra("isim",liste)
        startActivity(intent)
        finish()
    }
    fun kaydet(view: View) {
        var count =0
        var lists :ArrayList<Liste>
        var database = Database()
      db.collection("Listeler").whereEqualTo("isim", liste).get().addOnSuccessListener { documents ->
          for (document in documents) {
              deneme = document.get("Urunler") as ArrayList<HashMap<String, Any>>
              deneme1 = deneme.clone() as ArrayList<HashMap<String, Any>>
              for (x in deneme) {
                  if (x.getValue("UrunAdi").toString() == textView9.text) {
                      x.put("UrunAdi", textView9.text.toString())
                      x.put("UrunAdeti", textView11.text.toString())
                      x.put("UrunNotu", textView12.text.toString())
                      deneme1.set(count, x)
                      val listmap = hashMapOf<String, Any>()
                      listmap.put("Urunler", deneme1)
                      listmap.put("isim", liste!!)
                      println(listmap)
                      addDatabase(listmap, liste!!)
                  }
                  count++
              }
          }
      }
    }
}