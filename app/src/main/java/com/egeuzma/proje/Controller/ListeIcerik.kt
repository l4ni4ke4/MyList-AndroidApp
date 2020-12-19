package com.egeuzma.proje.Controller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.egeuzma.proje.MalzemeAdapter
import com.egeuzma.proje.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_liste_icerik.*
import kotlinx.android.synthetic.main.activity_urun_ekleme.*
import kotlinx.android.synthetic.main.change_liste_ismi.*

class ListeIcerik : AppCompatActivity() {
    private lateinit var  db : FirebaseFirestore
    var productName : ArrayList<String> = ArrayList()
    var product : ArrayList<String> = ArrayList()
    var deneme: ArrayList<HashMap<String,Any>> = ArrayList()
    var adapter : MalzemeAdapter?=null
    var selectedList:String? = null
    var productNumber : ArrayList<String> = ArrayList()
    var productNote : ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liste_icerik)
        db = FirebaseFirestore.getInstance()
        val intent = intent
        val info= intent.getStringExtra("info")

        if(info == "new" || info == "old"){
            selectedList=intent.getStringExtra("isim")
            textView2.text=selectedList
            getFromFirebase(selectedList!!)
        }

        var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = MalzemeAdapter(productName,productNumber,productNote,selectedList!!)
        recyclerView.adapter = adapter
        recyclerView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                var imm =  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v!!.getWindowToken(), 0);

                return false;
            }
        });

    }
    fun getFromFirebase(selectedList :String) {
        db.collection("Listeler").whereEqualTo("isim", selectedList)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Toast.makeText(
                        applicationContext,
                        exception.localizedMessage.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {
                            productName.clear()
                            productNote.clear()
                            productNumber.clear()
                            val documents = snapshot.documents
                            for (document in documents) {
                                if (deneme.isEmpty()) {
                                   // product = document.get("malzemeler") as ArrayList<String>
                                    deneme = document.get("Urunler") as ArrayList<HashMap<String, Any>>
                                    for (x in deneme){
                                        productName.add(x.getValue("UrunAdi") as String)
                                        productNumber.add(x.getValue("UrunAdeti")as String)
                                        productNote.add(x.getValue("UrunNotu")as String)
                                    }
                                }
                                adapter!!.notifyDataSetChanged()
                            }
                        }
                    }

                }
            }
    }
    fun addProduct(view: View){
        val intent = Intent(applicationContext, UrunEkleme::class.java)
        intent.putExtra("isim",selectedList)
        startActivity(intent)
    }
    fun deleteList(view: View){
        var list =db.collection("Listeler").document(selectedList!!)
        list.delete()
        val intent = Intent(applicationContext,
            MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}