package com.andoresudev.safepassenger.ui.buslist

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andoresudev.safepassenger.R
import com.andoresudev.safepassenger.adapters.Buslist_custom_adapter
import com.andoresudev.safepassenger.adapters.driverlist_custom_adapter
import com.andoresudev.safepassenger.models.bus
import com.andoresudev.safepassenger.models.user
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.concurrent.timerTask

class BuslistFragment: Fragment() {
    private lateinit var buslistViewModel: BuslistViewModel

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var tblBuses: ListView
    private lateinit var fabAddBus: FloatingActionButton
    var busList= ArrayList<bus>()
    var driversList= ArrayList<user>()
    var routeList= ArrayList<user>()

    var routeListSt= ArrayList<String>()
    var driversListSt= ArrayList<String>()



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        buslistViewModel =
                ViewModelProvider(this).get(BuslistViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_buslist, container, false)
            auth= FirebaseAuth.getInstance()
            dbReference = FirebaseDatabase.getInstance().reference
            val clicToSelect=resources.getString(R.string.clic_to_select)
            driversListSt.add(clicToSelect)
            routeListSt.add(clicToSelect)
            listaRutas()
            listaChoferes()
            mostrarBuses()
            tblBuses=root.findViewById(R.id.tblBuses)
            fabAddBus=root.findViewById(R.id.fabAddBus)

            fabAddBus.setOnClickListener ( View.OnClickListener {
                agregarBus()
            })


            tblBuses.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            }
        return root

    }

    private fun listaRutas() {
        dbReference.child("Rutas").addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    snapshot.children.forEach {
                        routeListSt.add(it.key.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun listaChoferes(){
        dbReference.child("Usuarios").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    driversList.clear()
                    snapshot.children.forEach {
                        if (it.child("userRol").value.toString() == "driver"){
                            val chofer= user()
                            chofer.userId=it.child("userId").value.toString()
                            chofer.userName=it.child("userName").value.toString()
                            chofer.userPayroll=it.child("userPayroll").value.toString()
                            chofer.userLicence=it.child("userLicence").value.toString()
                            chofer.userCirculation=it.child("userCirculation").value.toString()
                            driversList.add(chofer)
                            driversListSt.add(chofer.userName)
                        }
                    }
                } else {
                    Toast.makeText(activity, "Error, Nodo no encontrado", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }



    private fun agregarBus(){

        val dialogBuilder = AlertDialog.Builder(activity).setCancelable(false)
        val li = layoutInflater
        val vis:View = li.inflate(R.layout.new_bus_alert_dialog,null)
        dialogBuilder.setView(vis)
        var spChoferes:Spinner
        var spRutas:Spinner
        val btnCancelar:ImageButton=vis.findViewById(R.id.btnCancelarAlertDialog2)
        val btnAgregar:Button=vis.findViewById(R.id.btnAddBus)
        val txtId:EditText=vis.findViewById(R.id.txtBusIdAlertDialog)
        spChoferes=vis.findViewById(R.id.spChoferNewBus)
        spRutas=vis.findViewById(R.id.spRutaNewBus)
        spChoferes.adapter = context?.let { ArrayAdapter<String>(it, R.layout.spinner_item_desing, driversListSt) }
        spRutas.adapter = context?.let { ArrayAdapter<String>(it, R.layout.spinner_item_desing, routeListSt) }


        val alertDialog=dialogBuilder.create()

        btnCancelar.setOnClickListener(View.OnClickListener {
            alertDialog.cancel()
        })

        btnAgregar.setOnClickListener(View.OnClickListener {
            if (spChoferes.selectedItemPosition==0||spRutas.selectedItemPosition==0){
                Toast.makeText(activity,R.string.fill_all_txt,Toast.LENGTH_SHORT).show()
            }else{
                val newBus= bus()
                newBus.busId=txtId.text.toString()

                newBus.Chofer=spChoferes.selectedItem.toString()
                newBus.ChoferID=chofId(newBus.Chofer)

                newBus.Ruta=spRutas.selectedItem.toString()
                newBus.Estado="En taller"
                newBus.Modelo="Carton"
                newBus.NumAsientos="2"
                newBus.asiento1="false"
                newBus.asiento2="false"

                activity?.let { it1 ->
                    dbReference.child("Camiones").child(newBus.busId).setValue(newBus).addOnCompleteListener(it1){ task->
                        if (task.isComplete){
                            Toast.makeText(activity,R.string.add_bus_complete,Toast.LENGTH_SHORT).show()
                            alertDialog.cancel()
                        }else{
                            Toast.makeText(activity,R.string.add_bus_incomplete,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
        
        alertDialog.show()

    }

        private fun chofId(name:String):String{
            var id=""
            driversList.forEach {
                if(it.userName==name){
                    id=it.userId
                }
            }
            println(name)
            println("+")
            println("++")
            println("+++")
            println(id)
            println("+++")
            println("++")
            println("+")

            return id
        }

    private fun mostrarBuses(){
        dbReference.child("Camiones").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    busList.clear()
                    snapshot.children.forEach {
                        var readBus=bus()
                        readBus.busId=it.child("busId").value.toString()
                        readBus.Chofer=it.child("chofer").value.toString()
                        readBus.Ruta=it.child("ruta").value.toString()
                        readBus.Estado=it.child("estado").value.toString()
                        busList.add(readBus)

                    }
                    val busAdapter = activity?.let { Buslist_custom_adapter(it, busList) }
                    tblBuses.adapter=busAdapter
                } else {
                    Toast.makeText(activity, "Error, Nodo no encontrado", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }



}


