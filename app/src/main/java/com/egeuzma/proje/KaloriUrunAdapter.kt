package com.egeuzma.proje

import android.app.Activity
import com.egeuzma.proje.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.egeuzma.proje.Controller.ListeIcerik
import com.egeuzma.proje.Controller.KaloriHesaplayici
import com.egeuzma.proje.Controller.selectedItemsList
import com.egeuzma.proje.Controller.toplamKalori
import kotlinx.android.synthetic.main.activity_kalori_hesaplayici.view.*
import kotlinx.android.synthetic.main.recycler_view_row.view.*
import kotlin.math.round
import android.content.res.Resources
import com.google.rpc.context.AttributeContext

import kotlinx.android.synthetic.main.activity_kalori_hesaplayici.*
import kotlinx.android.synthetic.main.activity_urun_ekleme.*
import java.util.*
import kotlin.collections.ArrayList

class KaloriUrunAdapter (private val productName : ArrayList<String>,private val productUnitCal: ArrayList
                         <Number>,private val context: Context):RecyclerView.Adapter<KaloriUrunAdapter.KaloriUrunHolder>(){

    private lateinit var viewgroup : ViewGroup
    class KaloriUrunHolder(view: View):RecyclerView.ViewHolder(view){
        var recyclerText : TextView? = null
        init {
            recyclerText = view.findViewById(R.id.textView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KaloriUrunHolder {
        viewgroup = parent
        val inflater = LayoutInflater.from(parent.context)
        val view =inflater.inflate(R.layout.recycler_view_row,parent,false)
        return KaloriUrunHolder(view)
    }

    override fun getItemCount(): Int {
        return productName.size
    }

    override fun onBindViewHolder(holder: KaloriUrunHolder, position: Int) {
        holder.recyclerText?.text=productName[position]
        holder.itemView.setOnClickListener {
             println("tıklandı: ${productName[position]}")
            // println(listname[position])

            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.kalori_popup,viewgroup,false)

            val builder = AlertDialog.Builder(context)
            builder.apply{
                val string: String = context.getString(R.string.eklenecekMik)
                setMessage("${productName[position]}\n\n$string")

                //alertDialog.setCancelable(false)
            }
            builder.setView(view)
                .setPositiveButton(R.string.ekle,
                DialogInterface.OnClickListener { dialog, id ->
                    var miktar: String = ""
                    miktar = "${view.findViewById<EditText>(R.id.text_miktar).text}"
                    val miktarD = miktar.toDoubleOrNull()
                    if(miktarD !is Double){
                        Toast.makeText(context,R.string.gecersizSayi,Toast.LENGTH_LONG).show()
                    }
                    else{
                        if(productUnitCal[position] is Long)
                        {
                            Toast.makeText(context,R.string.kaloriBilgisiBulunamadi,Toast.LENGTH_LONG).show()
                        }else{
                            val unitcalorie = productUnitCal[position] as Double
                            var urunCal = miktarD!! * unitcalorie
                            urunCal = round(urunCal)
                            toplamKalori += urunCal
                            val string: String = context.getString(R.string.kaloriYazisi)
                            val textToSend = "${productName[position]} , $miktar, ${string}: $urunCal"

                            val intent = Intent(context, KaloriHesaplayici::class.java)
                            //intent.putExtra("textToSend",textToSend)
                            selectedItemsList.add(textToSend)
                            (context as Activity).finish()
                            context.startActivity(intent)
                        }

                    }


                })
                .setNegativeButton(R.string.iptal,
                DialogInterface.OnClickListener { dialog, id ->

                })



            builder.create().show()



        }

    }


}