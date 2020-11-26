package com.egeuzma.proje

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val listname :ArrayList<String>): RecyclerView.Adapter<RecyclerAdapter.PostHolder>(){



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
        return listname.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.recyclerText?.text =listname[position]
        holder.itemView.setOnClickListener {
           // println("tıklandı")
           // println(listname[position])
            val context=holder.recyclerText?.context
            val intent = Intent( context,ListeIcerik::class.java)
            intent.putExtra("isim",listname[position])
            context?.startActivity(intent)
        }

    }
}