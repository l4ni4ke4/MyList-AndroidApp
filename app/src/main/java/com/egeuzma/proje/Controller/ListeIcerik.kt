package com.egeuzma.proje.Controller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
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
import com.egeuzma.proje.MyCallBack
import com.egeuzma.proje.R
import com.egeuzma.proje.model.Database
import com.egeuzma.proje.model.Liste
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_liste_icerik.*
import kotlinx.android.synthetic.main.activity_urun_ekleme.*
import kotlinx.android.synthetic.main.change_liste_ismi.*

class ListeIcerik : AppCompatActivity() {
    private lateinit var  db : FirebaseFirestore
    var productName : ArrayList<String> = ArrayList()
    var productCheck : ArrayList<Boolean> = ArrayList()
    var deneme: ArrayList<HashMap<String,Any>> = ArrayList()
    var adapter : MalzemeAdapter?=null
    var selectedList:String? = null
    var productNumber : ArrayList<String> = ArrayList()
    var productNote : ArrayList<String> = ArrayList()
    var layoutManager: LinearLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liste_icerik)
        db = FirebaseFirestore.getInstance()
        val intent = intent
        val info= intent.getStringExtra("info")
        if(info == "new" || info == "old"){
            selectedList=intent.getStringExtra("isim")
            textView2.text=selectedList
            getSelectedListsProduct(selectedList!!)
        }

        recyclerView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                var imm =  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v!!.getWindowToken(), 0);

                return false;
            }
        })
    }
    fun getSelectedListsProduct(selectedList :String){
        var database=Database()
        database.getSelectedListProducts(object :MyCallBack{
            override fun onCallback(value: ArrayList<Any>) {
            }
            override fun onCallback(
                name: ArrayList<String>,
                number: ArrayList<String>,
                not: ArrayList<String>,
                check: ArrayList<Boolean>
            ) {
                productName=name
                productNumber=number
                productNote=not
                productCheck=check
                checkedProduct(productCheck)

                layoutManager = LinearLayoutManager(this@ListeIcerik)
                recyclerView.layoutManager = layoutManager
                adapter = MalzemeAdapter(productName,productNumber,productNote,selectedList!!,productCheck)
                recyclerView.adapter = adapter
            }

        },selectedList)
    }
    fun checkedProduct(list: ArrayList<Boolean>){
        var count=0
        for (product in list){
            if (product){
                count++
            }
        }
        textView18.text=count.toString()+"/"+list.size.toString()
    }
    fun addProduct(view: View){
        val intent = Intent(applicationContext, UrunEkleme::class.java)
        intent.putExtra("isim",selectedList)
        startActivity(intent)
        finish()
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