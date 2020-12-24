package com.egeuzma.proje

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.egeuzma.proje.Controller.ListeIcerik
import com.egeuzma.proje.Controller.MainActivity
import com.egeuzma.proje.Controller.UrunDetayi
import com.egeuzma.proje.model.Liste
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.urun_ekleme_dialog.*


class MalzemeAdapter (private val productName : ArrayList<String>,private val productNumber : ArrayList<String>,private val productNote : ArrayList<String>,private val liste:String,private val productCheck : ArrayList<Boolean>):RecyclerView.Adapter<MalzemeAdapter.MalzemeHolder>(){
    class MalzemeHolder(view: View):RecyclerView.ViewHolder(view){
        var recyclerText : TextView? = null

        init {
            recyclerText = view.findViewById(R.id.textView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MalzemeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =inflater.inflate(R.layout.recycler_view_row,parent,false)
        return MalzemeHolder(view)
    }

    override fun getItemCount(): Int {
        return productName.size
    }

    override fun onBindViewHolder(holder: MalzemeHolder, position: Int) {
        holder.recyclerText?.text=productName[position]
        if(!productCheck[position]){
            holder.recyclerText?.paintFlags=Paint.ANTI_ALIAS_FLAG
            holder.recyclerText?.paint!!.setMaskFilter(null)
        }else{
            holder.recyclerText?.paint!!.setMaskFilter(BlurMaskFilter(10F, BlurMaskFilter.Blur.INNER))
            holder.recyclerText?.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
        }
        val map = HashMap<String,Any>()
        map.put("UrunAdi",productName[position])
        map.put("UrunAdeti",productNumber[position])
        map.put("UrunNotu",productNote[position])
       holder.itemView.setOnClickListener {
           if(!productCheck[position]){
              productCheck[position]=true
              holder.recyclerText?.paint!!.setMaskFilter(BlurMaskFilter(10F, BlurMaskFilter.Blur.INNER))
              holder.recyclerText?.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
               map.put("isCheck",productCheck[position])
               addProductToList(holder.recyclerText?.context!!,map,position)
           }else{
               productCheck[position]=false
               holder.recyclerText?.paintFlags=Paint.ANTI_ALIAS_FLAG
               holder.recyclerText?.paint!!.setMaskFilter(null)
               map.put("isCheck",productCheck[position])
               addProductToList(holder.recyclerText?.context!!,map,position)
           }
        }
        holder.itemView.setOnLongClickListener {
            val context=holder.recyclerText?.context
            val intent = Intent( context, UrunDetayi::class.java)
            intent.putExtra("isim",productName[position])
            intent.putExtra("not",productNote[position])
            intent.putExtra("adet",productNumber[position])
            intent.putExtra("liste",liste)
            context?.startActivity(intent)
            (context as Activity).finish()
            true
        }
    }
    fun addProductToList(context: Context, urunmap:HashMap<String,Any>,index: Int) {
        var db = FirebaseFirestore.getInstance()

        var products : ArrayList<HashMap<String,Any>> = ArrayList()
        db.collection("Listeler").whereEqualTo("isim",liste).get().addOnSuccessListener { documents->
            for(document in documents){
                products=document.get("Urunler") as ArrayList<HashMap<String, Any>>
            }
            products.set(index,urunmap)
            val listmap = hashMapOf<String, Any>()
            listmap.put("Urunler", products)
            listmap.put("isim", liste)
            db.collection("Listeler").document(liste).set(listmap)
            (context as Activity).recreate()
        }
    }
}