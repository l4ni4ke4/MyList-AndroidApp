package com.egeuzma.proje

import android.content.Intent
import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.egeuzma.proje.Controller.KaloriHesaplayici
import com.egeuzma.proje.Controller.ListeIcerik
import com.egeuzma.proje.Controller.UrunDetayi
import com.egeuzma.proje.model.Liste

class KalorieAdapter (private val kaloriNameArray :ArrayList<String>): RecyclerView.Adapter<KalorieAdapter.PostHolder>(){
    //, private val kaloriArray :ArrayList<Number>


    class PostHolder(view: View) : RecyclerView.ViewHolder(view){
        var recyclerText : TextView? = null


        init {
            recyclerText = view.findViewById(R.id.textView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =inflater.inflate(R.layout.recycler_view_row,parent,false)
        return PostHolder(view)
    }

    override fun getItemCount(): Int {
        return kaloriNameArray.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.recyclerText?.text =kaloriNameArray[position]



    }
}