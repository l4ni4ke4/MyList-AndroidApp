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
        var database = Database()
        database.deleteProduct(liste!!,textView9.text.toString())
        val intent = Intent(applicationContext,
            ListeIcerik::class.java)
        intent.putExtra("info","old")
        intent.putExtra("isim",liste)
        startActivity(intent)
        finish()
    }
    fun kaydet(view: View) {
        var database = Database()
        database.saveProduct(liste!!,textView9.text.toString(),textView11.text.toString(),textView12.text.toString())
        val intent = Intent(applicationContext,
            ListeIcerik::class.java)
        intent.putExtra("info","old")
        intent.putExtra("isim",liste)
        startActivity(intent)
        finish()
    }
}