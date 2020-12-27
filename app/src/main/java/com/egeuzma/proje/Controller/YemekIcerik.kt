package com.egeuzma.proje.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.egeuzma.proje.R
import com.egeuzma.proje.model.Database
import com.egeuzma.proje.model.YemekTarif
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_yemek_icerik.*

class YemekIcerik : AppCompatActivity() {
    private  lateinit var db : FirebaseFirestore
    var selectedTarif : String? =null
    var products: ArrayList<HashMap<String,Any>> = ArrayList()
    var malzemeler : ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yemek_icerik)
        db = FirebaseFirestore.getInstance()
        val intent = intent
        val selectedRecept = intent.getSerializableExtra("isim") as YemekTarif
        selectedTarif = selectedRecept.isim
        var database=Database()
        database.getReceptFromFirebase(selectedTarif!!,textView17,imageView)
    }
    fun addMalzeme(view: View){
        var database = Database()
        database.makeSelectedRecipesToList(selectedTarif!!)
        val intent = Intent(applicationContext,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}