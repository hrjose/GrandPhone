package com.example.grandmother

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException
import java.io.IOException


class MainActivity : AppCompatActivity(){
    private val contacts: MutableList<Contact> = mutableListOf()
    private var gridAdapter: GridAdapter? = null
    private var gridView: GridView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getContacts()

        gridView = findViewById(R.id.gridView)
        gridAdapter = GridAdapter(applicationContext,contacts)
        gridView?.adapter = gridAdapter

        gridView?.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            val contact: Contact = contacts.elementAt(position)

            val phone: String = contact.phone
            val name: String = contact.name

            Toast.makeText(applicationContext,"LLAMANDO: $name",Toast.LENGTH_SHORT).show()

            val vibrator = applicationContext?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(500)
            }

            val i = Intent(Intent.ACTION_CALL)
            i.data = Uri.parse("tel:$phone")
            startActivity(i)
        })
    }

    @SuppressLint("Recycle", "Range")
    fun getContacts() {
        val cr: ContentResolver = contentResolver
        val cur: Cursor? = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        if (cur != null && cur.count > 0) {
            while (cur.moveToNext()) {
                val id: String = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name: String =
                    cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val imageUri: String? =
                    cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                if (name.substring(name.length - 1) == "_") {
                    continue
                }
                var image: Bitmap? = null;
                try {
                    if (imageUri != null) {
                        image =
                            MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(imageUri))
                    }
                } catch (e: FileNotFoundException) {
                    //TODO: HACER COSAS
                } catch (e: IOException) {
                    //TODO: HACER COSA
                }

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur: Cursor? = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (pCur != null && pCur.moveToNext()) {
                        val number: String =
                            pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        Log.i("contact_ID", id)
                        Log.i("contact_NAME", name)
                        Log.i("contact_PHONE", number)
                        val newContact: Contact = Contact(name, number, image, id)
                        contacts.add(newContact)
                        break
                    }
                }
            }
        }
        contacts.sortBy { it.name }
        cur?.close()
        println(contacts)
    }
}