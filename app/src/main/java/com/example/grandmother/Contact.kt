package com.example.grandmother

import android.graphics.Bitmap
import android.widget.GridView

class Contact(val name: String, val phone: String, val image: Bitmap?, val id: String) {

    override fun toString(): String {
        return "id: $id, name: $name, phone: $phone \n"
    }
}