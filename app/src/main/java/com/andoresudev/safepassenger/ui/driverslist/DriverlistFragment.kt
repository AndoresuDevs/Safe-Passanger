package com.andoresudev.safepassenger.ui.driverslist

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andoresudev.safepassenger.R
import com.andoresudev.safepassenger.adapters.driverlist_custom_adapter
import com.andoresudev.safepassenger.adapters.reports_adapter
import com.andoresudev.safepassenger.models.report
import com.andoresudev.safepassenger.models.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DriverlistFragment: Fragment() {

    private lateinit var driverlistFragment: DriverlistViewModel

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var tblDrivers: ListView

    var driversList= ArrayList<user>()
    var repList= java.util.ArrayList<report>()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        driverlistFragment =
                ViewModelProvider(this).get(DriverlistViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_driverlist, container, false)
            auth= FirebaseAuth.getInstance()
            dbReference = FirebaseDatabase.getInstance().reference
            tblDrivers=root.findViewById(R.id.tblChoferes)
            mostrarChoferes()

        tblDrivers.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val i:Int = parent.getItemIdAtPosition(position).toInt()
            val id=driversList[i].userId


            val dialogBuilder = AlertDialog.Builder(activity).setCancelable(false)
            val li = layoutInflater
            val vis:View = li.inflate(R.layout.driver_reports_alert_dialog,null)
            dialogBuilder.setView(vis)
            val btnCancelar: ImageButton =vis.findViewById(R.id.btnCerrarListaReportes)
            val tblDriverReports:ListView=vis.findViewById(R.id.tblDriverReports)
            val alertDialog=dialogBuilder.create()

            //val driversAdapter = activity?.let { reports_adapter(it, repList) }
            //tblDriverReports.adapter=driversAdapter
            reportList(id,tblDriverReports)

            btnCancelar.setOnClickListener(View.OnClickListener {
                alertDialog.cancel()
            })
            alertDialog.show()
        }

        return root

    }


    private fun reportList(id: String, tblDriverReports: ListView){
        repList.clear()
        dbReference.child("Usuarios").child(id).child("Reportes").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val c=report()
                    c.fecha=it.child("fecha").value.toString()
                    driversList.forEach { chof->

                    }
                    driversList.forEach { user ->
                        if(it.child("chofer").value.toString()==user.userId){
                            c.chofer=user.userName
                        }
                    }

                    c.motivo=it.child("motivo").value.toString()
                    c.descripcion=it.child("descripcion").value.toString()

                    repList.add(c)
                }
                val driversAdapter = activity?.let { reports_adapter(it, repList) }
                tblDriverReports.adapter=driversAdapter


            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun mostrarChoferes() {
        dbReference.child("Usuarios").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    driversList.clear()
                    snapshot.children.forEach {
                        if (it.child("userRol").value.toString() == "driver"){
                            val chofer=user()
                            chofer.userId=it.child("userId").value.toString()
                            chofer.userName=it.child("userName").value.toString()
                            chofer.userPayroll=it.child("userPayroll").value.toString()
                            chofer.userLicence=it.child("userLicence").value.toString()
                            chofer.userCirculation=it.child("userCirculation").value.toString()
                            driversList.add(chofer)
                        }
                    }
                    val driversAdapter = activity?.let { driverlist_custom_adapter(it, driversList) }
                    tblDrivers.adapter=driversAdapter
                } else {
                    Toast.makeText(activity, "Error, Nodo no encontrado", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }

}