package com.example.grandmother

import android.graphics.Bitmap

class Contact(val name: String, val phone: String, val image: Bitmap?, val id: String) {
    override fun toString(): String {
        return "id: $id, name: $name, phone: $phone \n"
    }
}