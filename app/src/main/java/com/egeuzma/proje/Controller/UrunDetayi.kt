package com.egeuzma.proje.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.egeuzma.proje.R
import kotlinx.android.synthetic.main.activity_urun_detayi.*

class UrunDetayi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_urun_detayi)
        val intent = intent
        var selectedList=intent.getStringExtra("isim")
        var adet=intent.getIntExtra("adet",0)
        var not = intent.getStringExtra("not")
        textView9.text=selectedList
        textView11.text=adet.toString()
        textView12.text=not
    }
}