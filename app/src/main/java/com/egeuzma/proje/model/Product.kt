package com.egeuzma.proje.model

class Product(name : String , unitType : String,unitCalorie : Float,servingName :String,servingSize :String,category :String) {
    var isim :String ? = name
        private set
        get
    var type :String ? = unitType
        private set
        get
    var calorie :Float ? = unitCalorie
        private set
        get
    var serving :String ? = servingName
        private set
        get
    var servingsize :String ? = servingSize
        private set
        get
    var kategori :String ? = category
        private set
        get
}