package com.example.grandmother

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class GridAdapter(val context: Context, val contacts: MutableList<Contact>) : BaseAdapter() {

    override fun getCount(): Int {
        return contacts.size
    }

    override fun getItem(p0: Int): Any {
        return contacts.elementAt(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, ViewGroup: ViewGroup?): View {

        val view: View = View.inflate(context, R.layout.item_grid, null)

        val name: TextView = view.findViewById(R.id.name)
        val btn: ImageView = view.findViewById(R.id.photo)
        val contact: Contact = contacts.elementAt(position);
        val nameString: String = contact.name;
        val phoneString: String = contact.phone
        name.text = "$nameString ($phoneString)"

        if (contact.image != null){
            btn.setImageBitmap(contact.image)
        }
        return view
    }
}