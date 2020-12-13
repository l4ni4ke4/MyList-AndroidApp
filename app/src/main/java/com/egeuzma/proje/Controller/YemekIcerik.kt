package com.egeuzma.proje.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.egeuzma.proje.R
import com.egeuzma.proje.model.YemekTarif
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_yemek_icerik.*

class YemekIcerik : AppCompatActivity() {
    private  lateinit var db : FirebaseFirestore
    var selectedTarif : String? =null

    var malzemeler : ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yemek_icerik)
        db = FirebaseFirestore.getInstance()

        val intent = intent
        val selectedRecept = intent.getSerializableExtra("isim") as YemekTarif
        selectedTarif = selectedRecept.isim
        getReceptFromFirebase(selectedTarif!!)
    }
    fun getReceptFromFirebase(selectedTarif :String){
        db.collection("YemekTarifleri").whereEqualTo("isim",selectedTarif).get().addOnSuccessListener { documents ->
            for (document in documents){
                val tarif = document.get("tarif") as String
                textView17.text=tarif
            }
        }
    }
    fun addMalzeme(view: View){
        db.collection("YemekTarifleri").whereEqualTo("isim",selectedTarif).get().addOnSuccessListener { documents ->
            malzemeler.clear()
            for (document in documents){
                    malzemeler = document.get("malzemeler") as ArrayList<String>
            }
            val listmap = hashMapOf<String,Any>()
            val isim = selectedTarif!! +" Listesi "+(documents.size())
            listmap.put("isim",isim)
            listmap.put("malzemeler",malzemeler)
            addListToDatabase(listmap)
        }
    }
    fun  addListToDatabase (listmap : HashMap<String,Any>){
        db.collection("Listeler").add(listmap).addOnCompleteListener { task ->
            if(task.isComplete&&task.isSuccessful){
                val intent = Intent(applicationContext,
                    MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage.toString(),Toast.LENGTH_LONG).show()
        }
    }
}