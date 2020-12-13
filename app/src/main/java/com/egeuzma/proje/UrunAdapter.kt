package com.egeuzma.proje

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UrunAdapter (private val productname :ArrayList<String>): RecyclerView.Adapter<UrunAdapter.UrunHolder>(){


    class UrunHolder(view : View) : RecyclerView.ViewHolder(view){
        var recyclerText : TextView? = null

        init {
            recyclerText = view.findViewById(R.id.textView)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UrunHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =inflater.inflate(R.layout.recycler_view_row,parent,false)
        return UrunHolder(view)
    }

    override fun getItemCount(): Int {
        return productname.count()
    }

    override fun onBindViewHolder(holder:UrunHolder, position: Int) {
        holder.recyclerText?.text=productname[position]
    }
    fun filter(text: String) {
        var text = text
        //println(productname)
        var itemsCopy = productname.clone() as ArrayList<String>
        productname.clear()
        //println(itemsCopy)
        if (text.isEmpty()) {
            println("filter1")
            productname.addAll(itemsCopy)
        } else {
            println("filter2")
            text = text.toLowerCase()
            for (item in itemsCopy) {
                println("item : "+ item)
                println("text :"+ text)
                if (item==text) {
                    println("filter3")
                    productname.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }
}