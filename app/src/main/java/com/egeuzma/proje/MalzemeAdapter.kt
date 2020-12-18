package com.egeuzma.proje

import android.content.Intent
import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.egeuzma.proje.Controller.ListeIcerik
import com.egeuzma.proje.Controller.UrunDetayi
import kotlinx.android.synthetic.main.recycler_view_row.view.*

class MalzemeAdapter (private val productName : ArrayList<String>,private val productNumber : ArrayList<Long>,private val productNote : ArrayList<String>):RecyclerView.Adapter<MalzemeAdapter.MalzemeHolder>(){
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
       holder.itemView.setOnClickListener {
           if(holder.recyclerText?.paintFlags== Paint.STRIKE_THRU_TEXT_FLAG){
               holder.recyclerText?.paintFlags=Paint.ANTI_ALIAS_FLAG
              holder.recyclerText?.paint!!.setMaskFilter(null)
           }else{
               holder.recyclerText?.paint!!.setMaskFilter(BlurMaskFilter(10F, BlurMaskFilter.Blur.INNER))
               holder.recyclerText?.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
           }
        }
        holder.itemView.setOnLongClickListener {
            val context=holder.recyclerText?.context
            val intent = Intent( context, UrunDetayi::class.java)
            intent.putExtra("isim",productName[position])
            intent.putExtra("not",productNote[position])
            intent.putExtra("adet",productNumber[position])
            context?.startActivity(intent)
            true
        }
    }
}