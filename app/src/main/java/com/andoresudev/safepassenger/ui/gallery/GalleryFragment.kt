package com.andoresudev.myapplication.ui.gallery

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andoresudev.safepassenger.R
import com.andoresudev.safepassenger.models.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GalleryFragment : Fragment(), View.OnClickListener {

    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference

    private lateinit var txtNombre: EditText
    private lateinit var txtCorreo: EditText
    private lateinit var txtContra: EditText
    private lateinit var txtNomina: EditText
    private lateinit var txtLicencia: EditText
    private lateinit var txtCircu: EditText

    private lateinit var btnUsuario: ImageButton
    private lateinit var btnCorreo: ImageButton
    private lateinit var btnContra: ImageButton
    private lateinit var btnNomina: ImageButton
    private lateinit var btnLicencia: ImageButton
    private lateinit var btnCircu: ImageButton

    private lateinit var linea1: View
    private lateinit var linea2: View
    private lateinit var linea3: View
    private lateinit var txt1: TextView
    private lateinit var txt2: TextView
    private lateinit var txt3: TextView

    var ActualUser= user()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProvider(this).get(GalleryViewModel::class.java)
        val vista = inflater.inflate(R.layout.fragment_gallery, container, false)

        auth= FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference
        txtNombre=vista.findViewById(R.id.txtUsernameAccount)
        txtCorreo=vista.findViewById(R.id.txtEmailAccount)
        txtContra=vista.findViewById(R.id.txtPassAccount)

        txtNomina=vista.findViewById(R.id.txtPayrollAccount)
        txtLicencia=vista.findViewById(R.id.txtLicenceAccount)
        txtCircu=vista.findViewById(R.id.txtCirculationAccount)

        btnUsuario=vista.findViewById(R.id.btnUsernameAccount)
        btnCorreo=vista.findViewById(R.id.btnEmailAccount)
        btnContra=vista.findViewById(R.id.btnPassAccount)
        btnNomina=vista.findViewById(R.id.btnPayrollAccount)
        btnLicencia=vista.findViewById(R.id.btnLicenceAccount)
        btnCircu=vista.findViewById(R.id.btnCirculationAccount)


        linea1=vista.findViewById(R.id.view2)
        linea2=vista.findViewById(R.id.view3)
        linea3=vista.findViewById(R.id.view4)
        txt1=vista.findViewById(R.id.txtEstadoLvd)
        txt2=vista.findViewById(R.id.textView16)
        txt3=vista.findViewById(R.id.textView17)

        infoUser()

        btnUsuario.setOnClickListener(this)
        btnCorreo.setOnClickListener(this)
        btnContra.setOnClickListener(this)
        btnNomina.setOnClickListener(this)
        btnLicencia.setOnClickListener(this)
        btnCircu.setOnClickListener(this)



        return vista
    }

    private fun infoUser(){
        val id =auth.currentUser?.uid.toString()
        dbReference.child("Usuarios").child(id).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    ActualUser.userId=id
                    ActualUser.userName=snapshot.child("userName").value.toString()
                    ActualUser.userEmail=snapshot.child("userEmail").value.toString()
                    ActualUser.userPass=snapshot.child("userPass").value.toString()
                    ActualUser.userPayroll=snapshot.child("userPayroll").value.toString()
                    ActualUser.userCirculation=snapshot.child("userCirculation").value.toString()
                    ActualUser.userLicence=snapshot.child("userLicence").value.toString()
                    ActualUser.userLen=snapshot.child("userLen").value.toString()
                    ActualUser.userRol=snapshot.child("userRol").value.toString()

                    setInfoUser(ActualUser)

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })


    }

    fun setInfoUser(us: user){
        if(us.userRol.equals("driver")){
            txtNombre.setText(ActualUser.userName)
            txtCorreo.setText(ActualUser.userEmail)
            txtContra.setText(ActualUser.userPass)
            txtNomina.setText(ActualUser.userPayroll)
            txtLicencia.setText(ActualUser.userLicence)
            txtCircu.setText(ActualUser.userCirculation)


        }else{
            txtNombre.setText(us.userName)
            txtCorreo.setText(ActualUser.userEmail)
            txtContra.setText(ActualUser.userPass)
            txtNomina.visibility=View.GONE
            txtLicencia.visibility=View.GONE
            txtCircu.visibility=View.GONE
            btnCircu.visibility=View.GONE
            btnLicencia.visibility=View.GONE
            btnNomina.visibility=View.GONE
            linea1.visibility=View.GONE
            linea2.visibility=View.GONE
            linea3.visibility=View.GONE
            txt1.visibility=View.GONE
            txt2.visibility=View.GONE
            txt3.visibility=View.GONE

        }
    }



    private fun cambiarDato(ruta:String, subtitle_text:String){
        val dialogBuilder = AlertDialog.Builder(activity).setCancelable(false)
        val li = layoutInflater
        val vis:View = li.inflate(R.layout.alert_dialog_design,null)
        dialogBuilder.setView(vis)

        val subtitle: TextView=vis.findViewById(R.id.subTitleAlertDialog)
        val txtDato: EditText= vis.findViewById(R.id.txtAlertDialog)
        val btnCambio:Button = vis.findViewById(R.id.btnAlertDialog)
        val btnCancel: ImageButton = vis.findViewById(R.id.btnCancelarAlertDialog)
        subtitle.text=subtitle_text

        val alertDialog=dialogBuilder.create()

        btnCancel.setOnClickListener(View.OnClickListener {
            alertDialog.cancel()
        })

        btnCambio.setOnClickListener(View.OnClickListener {
            val dato=txtDato.text.toString()
            if (dato==""){
                Toast.makeText(activity,R.string.alert_dialog_null_validation,Toast.LENGTH_SHORT).show()
            }else{
                dbReference.child("Usuarios").child(auth.currentUser?.uid.toString()).child(ruta).setValue(dato)
                Toast.makeText(activity,R.string.update_information,Toast.LENGTH_SHORT).show()
                alertDialog.cancel()
            }
        })


        alertDialog.show()

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnUsernameAccount->{
                val text = "" + resources.getString(R.string.alert_dialog_message) +" "+resources.getString(R.string.username)+"?"
                cambiarDato("userName",text)
            }

            R.id.btnEmailAccount->{
                val text = "" + resources.getString(R.string.alert_dialog_message) +" "+resources.getString(R.string.email)+"?"
                cambiarDato("userEmail",text)
            }

            R.id.btnPassAccount->{
                val text = "" + resources.getString(R.string.alert_dialog_message) +" "+resources.getString(R.string.pass)+"?"
                cambiarDato("userPass",text)
            }

            R.id.btnPayrollAccount->{
                val text = "" + resources.getString(R.string.alert_dialog_message) +" "+resources.getString(R.string.payroll)+"?"
                cambiarDato("userPayroll",text)
            }

            R.id.btnLicenceAccount->{
                val text = "" + resources.getString(R.string.alert_dialog_message) +" "+resources.getString(R.string.driver_licence)+"?"
                cambiarDato("userLicence",text)
            }

            R.id.btnCirculationAccount-> {
                val text = "" + resources.getString(R.string.alert_dialog_message) + " " + resources.getString(R.string.circulation_card) + "?"
                cambiarDato("userCirculation", text)
            }
        }
    }


}



