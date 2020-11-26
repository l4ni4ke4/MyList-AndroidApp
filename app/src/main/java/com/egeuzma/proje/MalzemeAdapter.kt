package com.egeuzma.proje

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_view_row.view.*

class MalzemeAdapter (private val productName : ArrayList<String>):RecyclerView.Adapter<MalzemeAdapter.MalzemeHolder>(){
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
    }
}