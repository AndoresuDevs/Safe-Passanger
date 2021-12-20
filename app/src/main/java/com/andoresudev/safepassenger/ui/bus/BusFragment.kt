package com.andoresudev.safepassenger.ui.bus

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andoresudev.safepassenger.R
import com.andoresudev.safepassenger.models.*
import com.github.mikephil.charting.charts.LineChart
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class BusFragment: Fragment(), View.OnClickListener {
    private lateinit var busViewModel: BusViewModel
    private lateinit var historialReference: DatabaseReference
    private lateinit var reportReference: DatabaseReference
    private lateinit var busReference: DatabaseReference
    private lateinit var viajeReference: DatabaseReference
    private lateinit var dbReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var spinner: Spinner
    private lateinit var asiento1ocu: ImageView
    private lateinit var asiento2ocu: ImageView
    private lateinit var asientoChofer: ImageView
    private lateinit var borde: LinearLayout
    private lateinit var disCamion:TableLayout
    private lateinit var btnAbordar:Button
    private lateinit var btnReportar:Button
    private lateinit var btnDesabordar:Button
    private lateinit var txtNumCamion:TextView
    private var busSelected=""
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
    private var idUser=""
    private var horaInicio=""
    private var horaFin=""
    private var choferActual=""

    var busList= ArrayList<String>()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        busViewModel =
            ViewModelProvider(this).get(BusViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_bus, container, false)
        val clicToSelect=resources.getString(R.string.click_to_select_bus)
        busList.add(clicToSelect)
        historialReference=FirebaseDatabase.getInstance().reference
        reportReference=FirebaseDatabase.getInstance().reference
        busReference=FirebaseDatabase.getInstance().reference
        viajeReference=FirebaseDatabase.getInstance().reference
        dbReference = FirebaseDatabase.getInstance().reference
        auth= FirebaseAuth.getInstance()
        idUser=auth.currentUser?.uid.toString()
        spinner=root.findViewById(R.id.busSpinner)
        disCamion=root.findViewById(R.id.busModel)
        borde=root.findViewById(R.id.borde_bus)
        asiento1ocu=root.findViewById(R.id.asiento1ocu)
        asiento2ocu=root.findViewById(R.id.asiento2ocu)
        asientoChofer=root.findViewById(R.id.asientoChofer)
        btnAbordar=root.findViewById(R.id.btnAbordarBus)
        btnDesabordar=root.findViewById(R.id.btnDesabordarBus)
        btnReportar=root.findViewById(R.id.btnReportarBus)
        txtNumCamion=root.findViewById(R.id.lblTituloFrgBus)
        listaCamiones()
        val cont = context
        spinner.adapter = cont?.let { ArrayAdapter<String>(it, R.layout.spinner_item_desing, busList) }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val camion: String = parent?.getItemAtPosition(position).toString()
                if(position==0){
                    borde.setBackgroundResource(R.color.white)
                    disCamion.visibility=View.GONE
                    btnAbordar.visibility=View.GONE
                    val txt=resources.getString(R.string.bus_number)
                    txtNumCamion.text=txt

                }else{
                    disCamion.visibility=View.VISIBLE
                    btnAbordar.visibility=View.VISIBLE
                    busSelected=camion
                    mostrarAsientos(busSelected)
                    txtNumCamion.text=busSelected
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        btnAbordar.setOnClickListener(this)
        btnDesabordar.setOnClickListener(this)
        btnReportar.setOnClickListener(this)
        asientoChofer.setOnClickListener(this)
        historialDeViajes()
        return root

    }

    private fun listaCamiones(){
        dbReference.child("Camiones").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        busList.add(it.child("busId").value.toString())
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun mostrarAsientos(camion: String){
        dbReference.child("Camiones").child(camion).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var actualBus = bus()
                    actualBus.busId = camion
                    actualBus.Chofer = snapshot.child("Chofer").value.toString()
                    actualBus.Estado = snapshot.child("Estado").value.toString()
                    actualBus.Modelo = snapshot.child("Modelo").value.toString()
                    actualBus.NumAsientos = snapshot.child("NumAsientos").value.toString()
                    actualBus.Ruta = snapshot.child("Ruta").value.toString()
                    actualBus.asiento1 = snapshot.child("asiento1").value.toString()
                    actualBus.asiento2 = snapshot.child("asiento2").value.toString()
                    actualBus.pasillo = snapshot.child("pasillo").value.toString()






                    if (actualBus.asiento1 == "true") {
                        asiento1ocu.setImageResource(R.drawable.asiento1off)
                        asiento1ocu.setOnClickListener(View.OnClickListener {
                            Toast.makeText(activity, R.string.occupied_seat, Toast.LENGTH_SHORT).show()
                        })

                    } else {
                        asiento1ocu.setImageResource(R.drawable.asiento1on)
                        asiento1ocu.setOnClickListener(View.OnClickListener {
                            Toast.makeText(activity, R.string.available_seat, Toast.LENGTH_SHORT).show()
                        })
                    }



                    if (actualBus.asiento2 == "true") {
                        asiento2ocu.setImageResource(R.drawable.asiento2off)
                        asiento2ocu.setOnClickListener(View.OnClickListener {
                            Toast.makeText(activity, R.string.occupied_seat, Toast.LENGTH_SHORT).show()
                        })
                    } else {
                        asiento2ocu.setImageResource(R.drawable.asiento2on)
                        asiento2ocu.setOnClickListener(View.OnClickListener {
                            Toast.makeText(activity, R.string.available_seat, Toast.LENGTH_SHORT).show()
                        })
                    }

                    if (actualBus.pasillo=="true"){
                        if (actualBus.asiento1 != "true" || actualBus.asiento2 != "true") {
                            borde.setBackgroundResource(R.drawable.bus_red_border)
                        } else {
                            borde.setBackgroundResource(R.drawable.bus_green_border)
                        }
                    }else{
                        borde.setBackgroundResource(R.drawable.bus_green_border)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
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

    private fun historialDeViajes() {
        actualizarHora()
        historialReference.child("Historial").child(fecha).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                }else{
                    var busListTemp= ArrayList<String>()
                    busListTemp=busList
                    busListTemp.removeAt(0)
                    val report= dailyReport()
                    busListTemp.forEach{
                        historialReference.child("Historial").child(fecha).child(it).setValue(report)
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    fun abordar(){
        btnReportar.visibility=View.VISIBLE
        btnDesabordar.visibility=View.VISIBLE
        btnAbordar.visibility=View.GONE
        spinner.visibility=View.INVISIBLE

        val tr = travels()
        tr.usaurio=idUser
        tr.fecha=fecha
        actualizarHora()
        horaInicio=hora_min
        tr.inicio=horaInicio
        tr.fin=""
        tr.camion=busSelected
        dbReference.child("Camiones").child(busSelected).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    tr.ruta=snapshot.child("ruta").value.toString()
                    choferActual=snapshot.child("choferID").value.toString()

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        viajeReference.child("Usuarios").child(idUser).child("userTravels").child(fecha).child(horaInicio).setValue(tr)
        historialReference.child("Administrar").child(anoS).child(mesS).child(diaS).child("Usuarios").child(idUser).setValue(idUser)


    }

    fun desabordar(){
        btnReportar.visibility=View.GONE
        btnDesabordar.visibility=View.GONE
        btnAbordar.visibility=View.VISIBLE
        spinner.visibility=View.VISIBLE
        actualizarHora()
        horaFin=hora_min
        viajeReference.child("Usuarios").child(idUser).child("userTravels").child(fecha).child(horaInicio).child("fin").setValue(horaFin)

    }




    override fun onClick(v: View?) {
        when(v?.id){
            R.id.asientoChofer->{
                Toast.makeText(activity, R.string.driver_seat, Toast.LENGTH_SHORT).show()
            }
            R.id.btnReportarBus->{
                nuevoReporte()
            }
            R.id.btnAbordarBus->{
                abordar()
            }
            R.id.btnDesabordarBus->{
                desabordar()
            }
        }
    }

    private fun nuevoReporte(){
        val dialogBuilder = AlertDialog.Builder(activity).setCancelable(false)
        val li = layoutInflater
        val vis:View = li.inflate(R.layout.report_alert_dialog,null)
        dialogBuilder.setView(vis)

        val btnCancelar:ImageButton=vis.findViewById(R.id.btnCerrarReporte)
        val btnReportar:Button=vis.findViewById(R.id.btnConfirmarReporte)
        val txtMotivo:EditText=vis.findViewById(R.id.txtMotivoReporte)
        val txtDescripcion:EditText=vis.findViewById(R.id.txtDescripcionReporte)
        val btnAtraso:Button=vis.findViewById(R.id.btnAtrasado)
        val btnConducir:Button=vis.findViewById(R.id.btnConduceMal)
        val btnCambio:Button=vis.findViewById(R.id.btnMalCambio)
        val btnDetuvo:Button=vis.findViewById(R.id.btnNoSeDetuvo)
        val btnServicio:Button=vis.findViewById(R.id.btnMalServicio)


        val alertDialog=dialogBuilder.create()

        btnCancelar.setOnClickListener(View.OnClickListener {
            alertDialog.cancel()
        })

        btnAtraso.setOnClickListener(View.OnClickListener {
            txtMotivo.setText("Retraso")
            txtDescripcion.setText("")
        })
        btnConducir.setOnClickListener(View.OnClickListener {
            txtMotivo.setText("El chofer conduce mal")
            txtDescripcion.setText("")
        })
        btnCambio.setOnClickListener(View.OnClickListener {
            txtMotivo.setText("Me dieron mal mi cambio")
            txtDescripcion.setText("")
        })
        btnDetuvo.setOnClickListener(View.OnClickListener {
            txtMotivo.setText("El camión no se detuvo")
            txtDescripcion.setText("")
        })
        btnServicio.setOnClickListener(View.OnClickListener {
            txtMotivo.setText("El chofer brindo un mal servicio")
            txtDescripcion.setText("")
        })

        btnReportar.setOnClickListener(View.OnClickListener {
            actualizarHora()
            val r = report()
            r.camion=busSelected
            r.chofer=choferActual
            r.fecha=fecha
            r.motivo=txtMotivo.text.toString()
            r.descripcion=txtDescripcion.text.toString()
            r.user=auth.currentUser?.uid.toString()
            dbReference.run {
                r.camion=busSelected
                r.chofer=choferActual
                r.motivo=txtMotivo.text.toString()
                r.descripcion=txtDescripcion.text.toString()
                historialReference.child("Administrar").child(anoS).child(mesS).child(diaS).child("Reportes").push().setValue(r).addOnCompleteListener(OnCompleteListener {
                    task->
                    if (task.isComplete){
                        Toast.makeText(activity,R.string.sacceful_report,Toast.LENGTH_SHORT).show()
                    }
                })
                reportReference.child("Usuarios").child(choferActual).child("Reportes").push().setValue(r)
                historialReference.child("Reportes").push().setValue(r)

            }
            alertDialog.cancel()

        })

        alertDialog.show()

    }




}



