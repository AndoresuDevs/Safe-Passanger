package com.andoresudev.safepassenger.ui.history

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import com.andoresudev.safepassenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.widget.*
import com.andoresudev.safepassenger.adapters.history_detail_custom_adapter
import com.andoresudev.safepassenger.adapters.route_custom_adapter
import com.andoresudev.safepassenger.models.travels

class HistoryFragment : Fragment() {

    private lateinit var viewModel: HistoryViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var tblFechas: ListView
    private lateinit var spFecha:Spinner
    var dateList = ArrayList<String>()
    var detailsList=ArrayList<travels>()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel =
                ViewModelProvider(this).get(HistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_history, container, false)
        val clicToSelect=resources.getString(R.string.click_to_select_day)
        dateList.add(clicToSelect)
        auth = FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference
        tblFechas=root.findViewById(R.id.tblHistory)
        spFecha=root.findViewById(R.id.routeSpinner2)
        listaFechas()
        val cont = context
        spFecha.adapter = cont?.let { ArrayAdapter<String>(it, R.layout.spinner_item_desing,dateList) }

        spFecha.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val fecha: String = parent?.getItemAtPosition(position).toString()
                llenarTablaFechas(fecha)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        return root

    }

    private fun llenarTablaFechas(ruta:String){
        dbReference.child("Usuarios").child(auth.currentUser?.uid.toString()).child("userTravels").child(ruta).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                detailsList.clear()
                if (snapshot.exists()) {

                    snapshot.children.forEach {
                        val t= travels()
                        t.inicio=it.child("inicio").value.toString()
                        t.fin=it.child("fin").value.toString()
                        t.ruta=it.child("ruta").value.toString()
                        t.camion=it.child("camion").value.toString()
                        detailsList.add(t)

                    }
                    val driversAdapter = activity?.let { history_detail_custom_adapter(it, detailsList) }
                    tblFechas.adapter=driversAdapter
                }else{
                    detailsList.clear()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    private fun listaFechas(){
        dbReference.child("Usuarios").child(auth.currentUser?.uid.toString()).child("userTravels").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        dateList.add(it.key.toString())
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}