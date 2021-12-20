package com.andoresudev.safepassenger.ui.route

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andoresudev.safepassenger.R
import com.andoresudev.safepassenger.adapters.route_custom_adapter
import com.andoresudev.safepassenger.models.busStop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RouteFragment : Fragment() {

    private lateinit var routeFragment: RouteViewModel

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var tblRoute: ListView
    private lateinit var spinner: Spinner

    var routeList = ArrayList<String>()
    var busStopList=ArrayList<busStop>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        routeFragment = ViewModelProvider(this).get(RouteViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_route, container, false)
        val clicToSelect=resources.getString(R.string.click_to_select_route)
        routeList.add(clicToSelect)
        auth = FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference
        tblRoute=root.findViewById(R.id.tblHistory)
        spinner=root.findViewById(R.id.routeSpinner)
        listaRutas()
        val cont = context
        spinner.adapter = cont?.let { ArrayAdapter<String>(it, R.layout.spinner_item_desing,routeList) }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val ruta: String = parent?.getItemAtPosition(position).toString()
                llenarListViewRuta(ruta)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        return root

    }

    private fun llenarListViewRuta(ruta:String){
        dbReference.child("Rutas").child(ruta).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                busStopList.clear()
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val parada=busStop()
                        parada.name=it.child("nombre").value.toString()
                        parada.direction=it.child("direccion").value.toString()
                        parada.id=it.child("id").value.toString()
                        busStopList.add(parada)

                    }
                    val driversAdapter = activity?.let { route_custom_adapter(it, busStopList) }
                    tblRoute.adapter=driversAdapter
                }else{
                    val driversAdapter = activity?.let { route_custom_adapter(it, busStopList) }
                    tblRoute.adapter=driversAdapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun listaRutas() {
        dbReference.child("Rutas").addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    snapshot.children.forEach {
                        routeList.add(it.key.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}