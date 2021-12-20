package com.andoresudev.safepassenger.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.andoresudev.myapplication.ui.home.HomeViewModel
import com.andoresudev.safepassenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var btnMonitorear: Button
    private lateinit var btnRutas: Button
    private lateinit var btnBuses: Button
    private lateinit var btnChoferes: Button
    private lateinit var btnHistorial: Button
    private lateinit var btnAnalisis: Button
    private lateinit var btnBuzon: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var txtSaludo: TextView

    private lateinit var clBuses: ConstraintLayout
    private lateinit var clChoferes: ConstraintLayout
    private lateinit var clAnalisis: ConstraintLayout



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val vista = inflater.inflate(R.layout.fragment_home, container, false)
        auth= FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference



        clBuses=vista.findViewById(R.id.clBuses)
        clChoferes=vista.findViewById(R.id.clChoferes)
        clAnalisis=vista.findViewById(R.id.clAnalisis)
        txtSaludo=vista.findViewById(R.id.saludoHome)

        btnMonitorear=vista.findViewById(R.id.btnMonitorear)
        btnBuses=vista.findViewById(R.id.btnBuses)
        btnRutas=vista.findViewById(R.id.btnRuta)
        btnChoferes=vista.findViewById(R.id.btnChoferes)
        btnHistorial=vista.findViewById(R.id.btnHistorial)
        btnAnalisis=vista.findViewById(R.id.btnAnalisis)
        btnBuzon=vista.findViewById(R.id.btnBuzon)

        modoDeUsuario()

        btnMonitorear.setOnClickListener(this)
        btnBuses.setOnClickListener(this)
        btnRutas.setOnClickListener(this)
        btnChoferes.setOnClickListener(this)
        btnAnalisis.setOnClickListener(this)
        btnHistorial.setOnClickListener(this)
        btnBuzon.setOnClickListener(this)

        return vista
    }



    private fun modoDeUsuario(){
        dbReference.child("Usuarios").child(auth.currentUser?.uid.toString()).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val privilegio= snapshot.child("userMode").value.toString()
                if (privilegio=="user"){
                    clChoferes.visibility=View.GONE
                    clBuses.visibility=View.GONE
                    clAnalisis.visibility=View.GONE
                    val salu = resources.getString(R.string.greeting)
                    txtSaludo.text=salu
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }



    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnMonitorear  ->v.findNavController().navigate(R.id.action_nav_home_to_nav_bus)
            R.id.btnBuses       ->v.findNavController().navigate(R.id.action_nav_home_to_buslistFragment)
            R.id.btnChoferes    ->v.findNavController().navigate(R.id.action_nav_home_to_driverlistFragment)
            R.id.btnRuta        ->v.findNavController().navigate(R.id.action_nav_home_to_routeFragment2)
            R.id.btnHistorial   ->v.findNavController().navigate(R.id.action_nav_home_to_historyFragment)
            R.id.btnAnalisis   ->v.findNavController().navigate(R.id.action_nav_home_to_dataFragment)
            R.id.btnBuzon       ->v.findNavController().navigate(R.id.action_nav_home_to_mailboxFragment)



        }
    }




}