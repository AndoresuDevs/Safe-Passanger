package com.andoresudev.safepassenger.ui.mailbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andoresudev.safepassenger.R
import com.andoresudev.safepassenger.adapters.comment_adapter
import com.andoresudev.safepassenger.adapters.driverlist_custom_adapter
import com.andoresudev.safepassenger.adapters.reports_adapter
import com.andoresudev.safepassenger.adapters.route_custom_adapter
import com.andoresudev.safepassenger.models.busStop
import com.andoresudev.safepassenger.models.comment
import com.andoresudev.safepassenger.models.report
import com.andoresudev.safepassenger.models.user
import com.andoresudev.safepassenger.ui.route.RouteViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class MailboxFragment : Fragment() {

    private lateinit var mailFragment: MailboxViewModel

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var btnMandarComentario:Button
    private lateinit var tblComentarios:ListView
    private lateinit var tblReportes:ListView
    private lateinit var txtComentario:EditText
    private lateinit var txtTitulo:TextView
    private lateinit var areaComentario:LinearLayout
    private lateinit var adminComentario:HorizontalScrollView

    private var dia=0
    private var mes=0
    private var ano=0
    private var diaS=""
    private var mesS=""
    private var anoS=""
    private var hora=0
    private var min=0
    private var fecha=""
    private var hora_min=""
    private var id_fecha_hora=""
    var commentList=ArrayList<comment>()
    var repList=ArrayList<report>()
    var driversList= ArrayList<user>()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mailFragment =
                ViewModelProvider(this).get(MailboxViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mailbox, container, false)
        auth = FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference
        btnMandarComentario=root.findViewById(R.id.btnConfirmarComentario)
        tblComentarios=root.findViewById(R.id.tblSugerencias)
        tblReportes=root.findViewById(R.id.tblReportes)
        txtComentario=root.findViewById(R.id.txtCommentDescription)
        txtTitulo=root.findViewById(R.id.tituloDescripcion)
        areaComentario=root.findViewById(R.id.hacerSugerencia)
        adminComentario=root.findViewById(R.id.adminComments)

        mostrarChoferes()
        modoDeUsuario()
        reportList()

        btnMandarComentario.setOnClickListener(View.OnClickListener {
            comentar()
        })


        comentariosList()
        return root

    }

    private fun modoDeUsuario(){
        dbReference.child("Usuarios").child(auth.currentUser?.uid.toString()).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val privilegio= snapshot.child("userMode").value.toString()
                if (privilegio=="admin"){
                    adminComentario.visibility=View.VISIBLE
                    areaComentario.visibility=View.GONE
                }else{
                    adminComentario.visibility=View.GONE
                    areaComentario.visibility=View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun comentar(){
        actualizarHora()
        val c = comment()
        c.message= txtComentario.text.toString()
        c.user=auth.currentUser?.uid.toString()
        dbReference.child("Comentarios").push().setValue(c)
        dbReference.child("Administrar").child(anoS).child(mesS).child(diaS).child("Comentarios").push().setValue(c)
        txtComentario.setText("")
        Toast.makeText(activity,"Gracias por sus comentarios",Toast.LENGTH_SHORT).show()


    }

    private fun actualizarHora(){
        var c = Calendar.getInstance()
        dia=c.get(Calendar.DAY_OF_MONTH)
        mes=c.get(Calendar.MONTH)+1
        ano=c.get(Calendar.YEAR)

        hora=c.get(Calendar.HOUR_OF_DAY)
        min=c.get(Calendar.MINUTE)

        hora_min = when(min){
            in 1..9->{
                "$hora:0$min"
            }
            else->{
                "$hora:$min"
            }
        }

        when(mes){
            in 1..9->{
                diaS=""+dia
                mesS= "0$mes"
                anoS=""+ano
                fecha = "$dia-0$mes-$ano"

            }
            else->{
                diaS=""+dia
                mesS=""+mes
                anoS=""+ano
                fecha = "$dia-$mes-$ano"
            }
        }


        id_fecha_hora = "$fecha $hora"
    }


    private fun comentariosList(){
        commentList.clear()
        dbReference.child("Comentarios").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val c=comment()
                    c.user=it.child("user").value.toString()
                    c.message=it.child("message").value.toString()
                    println(c.message)
                    commentList.add(c)
                }
                println("COMENTARIOS: "+commentList.size)
                val driversAdapter = activity?.let { comment_adapter(it, commentList) }
                tblComentarios.adapter=driversAdapter
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    private fun reportList(){
        repList.clear()
        dbReference.child("Reportes").addValueEventListener(object: ValueEventListener {
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
                tblReportes.adapter=driversAdapter
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

                } else {
                    Toast.makeText(activity, "Error, Nodo no encontrado", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }

}