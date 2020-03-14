package com.example.chatapp

import Contacts
import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.*
import com.example.chatapp.ui.main.Adaptor.ContactAdapter
import com.example.chatapp.ui.main.Control.OnLoadMoreListener
import com.example.chatapp.ui.main.Control.RecyclerViewLoadMoreScroll
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_numbers.*
import kotlin.collections.ArrayList


class NumbersActivity : AppCompatActivity() {
    val REQUEST_PERMISSION = 1
    lateinit var adapter: ContactAdapter
    var loadMoreItemsCells: ArrayList<Contacts> = arrayListOf()
    var loadSearchItems: ArrayList<Contacts> = arrayListOf()
    var fullList: ArrayList<Contacts> = arrayListOf()
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private lateinit var mLayoutManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numbers)
        val fab_search: FloatingActionButton = findViewById(R.id.fab_search)
        val recyclerView: RecyclerView = findViewById(R.id.contact_recycler_view)
        contact_search.visibility = View.GONE
        fab_search.setOnClickListener {
            if (contact_search.visibility == View.GONE) {
                contact_search.visibility = View.VISIBLE
            } else {
                contact_search.visibility = View.GONE
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_PERMISSION
            )
        } else {

            if (loadMoreItemsCells.isEmpty()) {
                adapter = ContactAdapter(getContact(1, 10))
            } else {
                adapter = ContactAdapter(loadMoreItemsCells)
            }
            adapter.notifyDataSetChanged()
            contact_recycler_view.adapter = adapter
            if (fullList.size == 0) {
                fullList = getFullContactList()
            }

            setRVLayoutManager()

            setRVScrollListener()

        }

        var word: String = ""
        contact_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                loadSearchItems.clear()
                fullList.forEach {
                    if (it.name.toLowerCase().contains(s.toString().toLowerCase())) {
                        loadSearchItems.add(it)
                    }
                }
                if (word == "") {
                    adapter = ContactAdapter(getContact(1, 10))
                    adapter.notifyDataSetChanged()
                    contact_recycler_view.adapter = adapter
                }
                if (loadSearchItems.size != 0 && count > 1) {
                    adapter = ContactAdapter(loadSearchItems)
                    adapter.notifyDataSetChanged()
                    contact_recycler_view.adapter = adapter
                }
                if (loadSearchItems.size == 0 && count > 4) {
                    loadSearchItems.clear()
                    adapter = ContactAdapter(loadSearchItems)
                    adapter.notifyDataSetChanged()
                    contact_recycler_view.adapter = adapter
                    Toast.makeText(
                        applicationContext,
                        "Eşleşen bir kayıt bulunamadı",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })

    }

    private fun LoadMoreData() {
        adapter.addLoadingView()
        //Create the loadMoreItemsCells Arraylist
        loadMoreItemsCells = ArrayList()
        //Get the number of the current Items of the main Arraylist
        val start = adapter.itemCount
        //Load 16 more items
        val end = start + 10
        //Use Handler if the items are loading too fast.
        //If you remove it, the data will load so fast that you can't even see the LoadingView
        Handler().postDelayed({
            loadMoreItemsCells = getContact(start, end)
            //Remove the Loading View
            adapter.removeLoadingView()
            //We adding the data to our main ArrayList
            adapter.addData(loadMoreItemsCells)
            //Change the boolean isLoading to false
            scrollListener.setLoaded()
            //Update the recyclerView in the main thread
            contact_recycler_view.post {
                adapter.notifyDataSetChanged()
            }
        }, 500)

    }

    //Get Contact List Here
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) getContact(1, 10)
    }

    fun getContact(min: Int, max: Int): ArrayList<Contacts> {
        return getContactData(min, max)

    }

    fun getContactData(minValue: Int, maxValue: Int): ArrayList<Contacts> {
        val contactList: ArrayList<Contacts> = arrayListOf()
        var i = minValue
        val cursor =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if ((cursor?.count ?: 0) > 0) {
            while (cursor != null && cursor.moveToPosition(i) && i < maxValue) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                var number = ""
                i++
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                        arrayOf<String>(id),
                        ""
                    )
                    while (phoneCursor != null && phoneCursor.moveToNext()) {
                        number += phoneCursor.getString(
                            phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        ) + "\n"
                    }
                    phoneCursor?.close()
                }
                contactList.add(Contacts(name, number))
            }
        }
        cursor?.close()
        return contactList
    }

    fun getFullContactList(): ArrayList<Contacts> {
        val contactList: ArrayList<Contacts> = arrayListOf()
        val cursor =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if ((cursor?.count ?: 0) > 0) {
            while (cursor != null && cursor.moveToNext()) {

                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                var number = ""
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                        arrayOf<String>(id),
                        ""
                    )
                    while (phoneCursor != null && phoneCursor.moveToNext()) {
                        number += phoneCursor.getString(
                            phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        ) + "\n"
                    }
                    phoneCursor?.close()
                }
                contactList.add(Contacts(name, number))
            }
        }
        cursor?.close()
        return contactList
    }

    private fun setRVLayoutManager() {
        mLayoutManager = LinearLayoutManager(this)
        contact_recycler_view.layoutManager = mLayoutManager
        contact_recycler_view.setHasFixedSize(true)
    }

    private fun setRVScrollListener() {
        mLayoutManager = LinearLayoutManager(this)
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                LoadMoreData()
            }
        })
        contact_recycler_view.addOnScrollListener(scrollListener)
    }
}
