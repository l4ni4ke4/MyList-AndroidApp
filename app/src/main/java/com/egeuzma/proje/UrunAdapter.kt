package com.egeuzma.proje

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
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
import com.egeuzma.proje.model.Database
import com.egeuzma.proje.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.rpc.context.AttributeContext
import kotlinx.android.synthetic.main.urun_ekleme_dialog.*

class UrunAdapter (private val product :ArrayList<String>,private val isim:String): RecyclerView.Adapter<UrunAdapter.UrunHolder>() {


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
        return product.count()
    }

    override fun onBindViewHolder(holder: UrunHolder, position: Int) {
        holder.recyclerText?.text = product[position]
        var database=Database()
        holder.itemView.setOnClickListener {
            var dialog = Dialog(holder.recyclerText?.context!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.urun_ekleme_dialog)
            dialog.button30.setOnClickListener {
                val map = HashMap<String,Any>()
                map.put("UrunAdi",product[position])
                map.put("UrunAdeti",dialog.editTextNumber30.text.toString())
                map.put("UrunNotu",dialog.editTextTextMultiLine30.text.toString())
                map.put("isCheck",false)
                database.addProductToList(holder.recyclerText?.context!!,map,isim)
                dialog.cancel()
            }
            dialog.show()

        }
    }
}