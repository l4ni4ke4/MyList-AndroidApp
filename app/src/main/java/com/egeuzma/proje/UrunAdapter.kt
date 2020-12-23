package com.egeuzma.proje

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.egeuzma.proje.Controller.ListeIcerik
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.urun_ekleme_dialog.*

class UrunAdapter (private val productname :ArrayList<String>,private val isim:String): RecyclerView.Adapter<UrunAdapter.UrunHolder>() {


    class UrunHolder(view: View) : RecyclerView.ViewHolder(view) {
        var recyclerText: TextView? = null

        init {
            recyclerText = view.findViewById(R.id.textView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UrunHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_view_row, parent, false)
        return UrunHolder(view)
    }

    override fun getItemCount(): Int {
        return productname.count()
    }

    override fun onBindViewHolder(holder: UrunHolder, position: Int) {
        holder.recyclerText?.text = productname[position]
        holder.itemView.setOnClickListener {
            var dialog = Dialog(holder.recyclerText?.context!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.urun_ekleme_dialog)
            dialog.button30.setOnClickListener {
                val map = HashMap<String,Any>()
                map.put("UrunAdi",productname[position])
                map.put("UrunAdeti",dialog.editTextNumber30.text.toString())
                map.put("UrunNotu",dialog.editTextTextMultiLine30.text.toString())
                map.put("isCheck",false)
                addProductToList(holder.recyclerText?.context!!,map)
                dialog.cancel()
            }
            dialog.show()

        }
    }

    fun addProductToList(context: Context,urunmap:HashMap<String,Any>) {
        var db = FirebaseFirestore.getInstance()
        var products : ArrayList<HashMap<String,Any>> = ArrayList()
        var count =0
        db.collection("Listeler").whereEqualTo("isim",isim).get().addOnSuccessListener { documents ->
            for (document in documents){
                 products = document.get("Urunler") as ArrayList<HashMap<String, Any>>
            }
            for (product in products){
                if (product.getValue("UrunAdi").toString()==urunmap.getValue("UrunAdi").toString()){
                    Toast.makeText(context,"Bu ürün zaten listede var",Toast.LENGTH_SHORT).show()
                    count=1
                }
            }
            if(count==0){
                products.add(urunmap)
                val listmap = hashMapOf<String, Any>()
                listmap.put("Urunler", products)
                listmap.put("isim", isim)
                db.collection("Listeler").document(isim).set(listmap)
                Toast.makeText(context,urunmap.getValue("UrunAdi").toString()+" listeye eklendi",Toast.LENGTH_SHORT).show()
            }

        }

    }
}