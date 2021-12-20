package com.andoresudev.safepassenger.ui.data

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.andoresudev.safepassenger.R
import com.andoresudev.safepassenger.models.comment
import com.andoresudev.safepassenger.models.dailyReport
import com.andoresudev.safepassenger.models.report
import com.andoresudev.safepassenger.models.user
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class DataFragment : Fragment(), View.OnClickListener {


    private lateinit var dbReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var txtFecha:TextView
    private lateinit var graficaPasajerosAnual: BarChart
    private lateinit var graficaReportesAnual: BarChart
    private lateinit var graficaComentariosAnual: BarChart
    private lateinit var graficaMensual: BarChart
    private lateinit var graficaDiaria: BarChart
    private lateinit var modoAno:ScrollView
    private lateinit var txtTotUsu:TextView
    private lateinit var txtTotRep:TextView
    private lateinit var txtTotCom:TextView
    private lateinit var spMes:Spinner
    private lateinit var spAno:Spinner
    private lateinit var spAnual:Spinner

    var tUsu=0
    var tRep=0
    var lleno=0
    var vacio=0

    private var dia=0
    private var mes=0
    private var ano=0
    private var fechaSelect=""

    val cifrasDiarias= ArrayList<String>()
    val cifrasMensuales= ArrayList<String>()
    val cifrasAnuales= ArrayList<String>()
    var monthsArray = ArrayList<String>()
    var yearsArray = ArrayList<String>()

    private lateinit var btnDia:Button
    private lateinit var btnMes:Button
    private lateinit var btnAno:Button


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_data, container, false)
        dbReference = FirebaseDatabase.getInstance().reference
        auth= FirebaseAuth.getInstance()
        val m=resources.getString(R.string.month)
        val a=resources.getString(R.string.year)
        //monthsArray.add(m)
        //yearsArray.add(a)
        listas()
        graficaPasajerosAnual=vista.findViewById(R.id.graficaPasajerosAnual)
        graficaReportesAnual=vista.findViewById(R.id.graficaReportesAnual)
        graficaComentariosAnual=vista.findViewById(R.id.graficaComentariosAnual)
        graficaMensual=vista.findViewById(R.id.graficaMensual)
        graficaDiaria=vista.findViewById(R.id.graficaDiaria)
        graficaPasajerosAnual.description.isEnabled = false
        txtFecha=vista.findViewById(R.id.txtFechaData)
        txtTotUsu=vista.findViewById(R.id.txtTotUsuarios)
        txtTotRep=vista.findViewById(R.id.txtTotReportes)
        txtTotCom=vista.findViewById(R.id.txtTotComentarios)
        spMes=vista.findViewById(R.id.spAnalisisMes)
        spAno=vista.findViewById(R.id.spAnalisisAno)
        spAnual=vista.findViewById(R.id.spAnual)
        txtFecha.text=fechaDeHoy()
        spMes.adapter = context?.let { ArrayAdapter<String>(it, R.layout.spinner_item_desing,monthsArray) }
        spAno.adapter = context?.let { ArrayAdapter<String>(it, R.layout.spinner_item_desing,yearsArray) }
        spAnual.adapter = context?.let { ArrayAdapter<String>(it, R.layout.spinner_item_desing,yearsArray) }
        txtFecha.setOnClickListener {
            elegirFecha()
        }
        spMes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val month: String = spMes.selectedItem.toString()
                val year = spAno.selectedItem.toString()
                buscarDatosMensuales(month,year)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        spAnual.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val year = spAnual.selectedItem.toString()
                buscarDatosAnuales("05",year)

                //setBarChartAnual(year)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        modoAno=vista.findViewById(R.id.svAnual)

        btnAno=vista.findViewById(R.id.btnAnalisisAno)
        btnMes=vista.findViewById(R.id.btnAnalisisMes)
        btnDia=vista.findViewById(R.id.btnAnalisisDia)

        btnAno.setOnClickListener(this)
        btnMes.setOnClickListener(this)
        btnDia.setOnClickListener(this)

        return vista
    }

    private fun buscarDatosAnuales(mes: String, ano:String) {
        cifrasMensuales.clear()

        val passangerList= ArrayList<user>()
        val reportList= ArrayList<report>()
        val commentList= ArrayList<comment>()

        dbReference.child("Administrar").child(ano).child(mes).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    snapshot.children.forEach { dia ->

                        dia.child("Reportes").children.forEach {
                            val r= report()
                            r.user=it.child("user").value.toString()
                            r.camion=it.child("camion").value.toString()
                            r.chofer=it.child("chofer").value.toString()
                            r.descripcion=it.child("descripcion").value.toString()
                            r.motivo=it.child("motivo").value.toString()
                            r.fecha=it.child("fecha").value.toString()
                            reportList.add(r)

                        }
                        dia.child("Usuarios").children.forEach {
                            val u= user()
                            u.userId=it.child("userId").value.toString()
                            var repetido=false
                            passangerList.add(u)

                        }
                        dia.child("Comentarios").children.forEach {
                            val c=comment()
                            c.user=it.child("user").value.toString()
                            c.message=it.child("message").value.toString()
                            commentList.add(c)
                        }

                    }
                    cifrasMensuales.add(""+passangerList.size)
                    cifrasMensuales.add(""+reportList.size)
                    cifrasMensuales.add(""+commentList.size)
                    txtTotUsu.text = "${passangerList.size}"
                    txtTotRep.text = "${reportList.size}"
                    txtTotCom.text = "${commentList.size}"
                }
                else{
                    cifrasMensuales.add("0")
                    cifrasMensuales.add("0")
                    cifrasMensuales.add("0")
                    txtTotUsu.text = "0"
                    txtTotRep.text = "0"
                    txtTotCom.text = "0"
                    Toast.makeText(activity, "No existen registros del dia $ano",Toast.LENGTH_SHORT).show()
                }
                setBarChartAnual()


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }


    private fun buscarDatosMensuales(mes: String, ano:String) {
        cifrasMensuales.clear()

        val passangerList= ArrayList<user>()
        val reportList= ArrayList<report>()
        val commentList= ArrayList<comment>()

        dbReference.child("Administrar").child(ano).child(mes).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    snapshot.children.forEach { dia ->

                        dia.child("Reportes").children.forEach {
                            val r= report()
                            r.user=it.child("user").value.toString()
                            r.camion=it.child("camion").value.toString()
                            r.chofer=it.child("chofer").value.toString()
                            r.descripcion=it.child("descripcion").value.toString()
                            r.motivo=it.child("motivo").value.toString()
                            r.fecha=it.child("fecha").value.toString()
                            reportList.add(r)

                        }
                        dia.child("Usuarios").children.forEach {
                            val u= user()
                            u.userId=it.child("userId").value.toString()
                            var repetido=false
                            passangerList.add(u)


                        }
                        dia.child("Comentarios").children.forEach {
                            val c=comment()
                            c.user=it.child("user").value.toString()
                            c.message=it.child("message").value.toString()
                            commentList.add(c)
                        }

                    }
                    cifrasMensuales.add(""+passangerList.size)
                    cifrasMensuales.add(""+reportList.size)
                    cifrasMensuales.add(""+commentList.size)
                    txtTotUsu.text = "${passangerList.size}"
                    txtTotRep.text = "${reportList.size}"
                    txtTotCom.text = "${commentList.size}"
                }
                else{
                    cifrasMensuales.add("0")
                    cifrasMensuales.add("0")
                    cifrasMensuales.add("0")
                    txtTotUsu.text = "0"
                    txtTotRep.text = "0"
                    txtTotCom.text = "0"
                    Toast.makeText(activity, "No existen registros del dia $mes - $ano",Toast.LENGTH_SHORT).show()
                }
                setBarcharMensual()
                println("Cifras Mensuales del mes $mes")
                println("pasajeros: "+passangerList.size)
                println("reportes: "+reportList.size)
                println("comentarios: "+commentList.size)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun setBarcharDiaria(){
        val valuesPasajeros = ArrayList<BarEntry>()
        val valuesReportes = ArrayList<BarEntry>()
        val valuesComentarios = ArrayList<BarEntry>()
        valuesPasajeros.add(BarEntry(1f, cifrasDiarias[0].toFloat()))
        valuesReportes.add(BarEntry(2f, cifrasDiarias[1].toFloat()))
        valuesComentarios.add(BarEntry(3f, cifrasDiarias[2].toFloat()))

        val set1:BarDataSet
        val set2:BarDataSet
        val set3:BarDataSet

        val pasa = resources.getString(R.string.passengers)
        val repo = resources.getString(R.string.report)
        val come = resources.getString(R.string.comment)

        set1 = BarDataSet(valuesPasajeros, pasa)
        set1.color=resources.getColor(R.color.secondary_blue)
        set2 = BarDataSet(valuesReportes, repo)
        set2.color=Color.RED
        set3 = BarDataSet(valuesComentarios, come)
        set3.color=resources.getColor(R.color.primary_blue)

        val data = BarData(set1,set2,set3)
        graficaDiaria.data = data
        graficaDiaria.animateY(1500)
    }

    private fun setBarcharMensual(){
        val valuesPasajeros = ArrayList<BarEntry>()
        val valuesReportes = ArrayList<BarEntry>()
        val valuesComentarios = ArrayList<BarEntry>()
        valuesPasajeros.add(BarEntry(1f, cifrasMensuales[0].toFloat()))
        valuesReportes.add(BarEntry(2f, cifrasMensuales[1].toFloat()))
        valuesComentarios.add(BarEntry(3f, cifrasMensuales[2].toFloat()))

        println("CIFRAS MENSUALES "+cifrasMensuales)

        val set1:BarDataSet
        val set2:BarDataSet
        val set3:BarDataSet

        val pasa = resources.getString(R.string.passengers)
        val repo = resources.getString(R.string.report)
        val come = resources.getString(R.string.comment)

        set1 = BarDataSet(valuesPasajeros, pasa)
        set1.color=resources.getColor(R.color.secondary_blue)
        set2 = BarDataSet(valuesReportes, repo)
        set2.color=Color.RED
        set3 = BarDataSet(valuesComentarios, come)
        set3.color=resources.getColor(R.color.primary_blue)

        val data = BarData(set1,set2,set3)
        graficaMensual.data = data
        graficaMensual.animateY(1500)
    }

    private fun setBarChartAnual() {
        val valuesPasajeros = ArrayList<BarEntry>()
        val valuesReportes = ArrayList<BarEntry>()
        val valuesComentarios = ArrayList<BarEntry>()


        for (i in 1..3){
            valuesPasajeros.add(BarEntry(i.toFloat(), 0f))
            valuesComentarios.add(BarEntry(i.toFloat(), 0f))
            valuesReportes.add(BarEntry(i.toFloat(), 0f))
        }
        //buscarDatosMensuales("04",ano)

        //valuesPasajeros.add(BarEntry(4f, cifrasMensuales[0].toFloat()))
        //valuesReportes.add(BarEntry(4f, cifrasMensuales[1].toFloat()))
        //valuesComentarios.add(BarEntry(4f, cifrasMensuales[2].toFloat()))

        //buscarDatosMensuales("05",ano)

        valuesPasajeros.add(BarEntry(4f, 1f))
        valuesReportes.add(BarEntry(4f, 0f))
        valuesComentarios.add(BarEntry(4f, 2f))

        valuesPasajeros.add(BarEntry(5f, cifrasMensuales[0].toFloat()))
        valuesReportes.add(BarEntry(5f, cifrasMensuales[1].toFloat()))
        valuesComentarios.add(BarEntry(5f, cifrasMensuales[2].toFloat()))


        for (i in 6..12){
            valuesPasajeros.add(BarEntry(i.toFloat(), 0f))
            valuesComentarios.add(BarEntry(i.toFloat(), 0f))
            valuesReportes.add(BarEntry(i.toFloat(), 0f))
        }


        val set1:BarDataSet
        val set2:BarDataSet
        val set3:BarDataSet

        // create 3 DataSets
        val pasa = resources.getString(R.string.passengers)
        val repo = resources.getString(R.string.report)
        val come = resources.getString(R.string.comment)
        set1 = BarDataSet(valuesPasajeros, pasa)
        set1.color=resources.getColor(R.color.secondary_blue)
        set2 = BarDataSet(valuesReportes, repo)
        set2.color=Color.RED
        set3 = BarDataSet(valuesComentarios, come)
        set3.color=resources.getColor(R.color.primary_blue)




        val dataPas = BarData(set1)
        val dataRep = BarData(set2)
        val dataCom = BarData(set3)

        graficaPasajerosAnual.data = dataPas // set the data and list of lables into chart
        graficaReportesAnual.data = dataRep
        graficaComentariosAnual.data = dataCom
        graficaPasajerosAnual.animateY(1500)
        graficaReportesAnual.animateY(1500)
        graficaComentariosAnual.animateY(1500)

    }

    private fun fechaDeHoy():String{
        val c = Calendar.getInstance()
        dia=c.get(Calendar.DAY_OF_MONTH)
        mes=c.get(Calendar.MONTH)+1
        ano=c.get(Calendar.YEAR)

        fechaSelect = when(mes){
            in 1..9->{
                "$dia-0$mes-$ano"

            }
            else->{
                "$dia-$mes-$ano"
            }
        }
        Toast.makeText(activity, fechaSelect,Toast.LENGTH_SHORT).show()
        buscarDatosDiarios(fechaSelect)
        return fechaSelect
    }

    private fun elegirFecha(){
        val c = Calendar.getInstance()
        dia=c.get(Calendar.DAY_OF_MONTH)
        mes=c.get(Calendar.MONTH)+1
        ano=c.get(Calendar.YEAR)

        val datePickerDialog = activity?.let {
            DatePickerDialog(it, { view, year, month, dayOfMonth ->
                fechaSelect = when(month){
                    in 1..9->{
                        "$dayOfMonth-0$month-$year"

                    }
                    else->{
                        "$dayOfMonth-$month-$year"
                    }
                }
                txtFecha.text = fechaSelect
                buscarDatosDiarios(fechaSelect)



            }, ano, mes, dia)
        }
        datePickerDialog?.show()
    }

    private fun buscarDatosDiarios(fechaSelect: String) {
        cifrasDiarias.clear()
        val passangerList= ArrayList<user>()
        val reportList= ArrayList<report>()
        val commentList= ArrayList<comment>()
        val fech=fechaSelect.split("-")
        dbReference.child("Administrar").child(fech[2]).child(fech[1]).child(fech[0]).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    snapshot.child("Reportes").children.forEach {
                        val r= report()
                        r.user=it.child("user").value.toString()
                        r.camion=it.child("camion").value.toString()
                        r.chofer=it.child("chofer").value.toString()
                        r.descripcion=it.child("descripcion").value.toString()
                        r.motivo=it.child("motivo").value.toString()
                        r.fecha=it.child("fecha").value.toString()
                        reportList.add(r)

                    }
                    snapshot.child("Usuarios").children.forEach {
                        val u= user()
                        u.userId=it.child("userId").value.toString()
                        passangerList.add(u)
                    }
                    snapshot.child("Comentarios").children.forEach {
                        val c=comment()
                        c.user=it.child("user").value.toString()
                        c.message=it.child("message").value.toString()
                        commentList.add(c)
                    }
                    cifrasDiarias.add(""+passangerList.size)
                    cifrasDiarias.add(""+reportList.size)
                    cifrasDiarias.add(""+commentList.size)
                    txtTotUsu.text = "${passangerList.size}"
                    txtTotRep.text = "${reportList.size}"
                    txtTotCom.text = "${commentList.size}"

                }else{
                    cifrasDiarias.add("0")
                    cifrasDiarias.add("0")
                    cifrasDiarias.add("0")
                    txtTotUsu.text = "0"
                    txtTotRep.text = "0"
                    txtTotCom.text = "0"
                    Toast.makeText(activity, "No existen registros del dia $fechaSelect",Toast.LENGTH_SHORT).show()
                }
                setBarcharDiaria()


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun listas(){
        monthsArray.add("01")
        monthsArray.add("02")
        monthsArray.add("03")
        monthsArray.add("04")
        monthsArray.add("05")
        monthsArray.add("06")
        monthsArray.add("07")
        monthsArray.add("08")
        monthsArray.add("09")
        monthsArray.add("10")
        monthsArray.add("11")
        monthsArray.add("12")
        val c = Calendar.getInstance()
        ano=c.get(Calendar.YEAR)
        yearsArray.add(""+ano)

        dbReference.child("Administrar").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    //yearsArray.add(""+it.key)
                    //AQUI DEBERIAN LLENARSE LOS AÑOS PARA EL COMBO BOX
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun setearBotones(){
        btnAno.setBackgroundResource(R.drawable.button_primary)
        btnMes.setBackgroundResource(R.drawable.button_primary)
        btnDia.setBackgroundResource(R.drawable.button_primary)
        modoAno.visibility=View.GONE
        graficaDiaria.visibility=View.GONE
        graficaMensual.visibility=View.GONE
        txtFecha.visibility=View.GONE
        spAnual.visibility=View.GONE
        spAno.visibility=View.GONE
        spMes.visibility=View.GONE
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAnalisisDia->{
                setearBotones()
                btnDia.setBackgroundResource(R.drawable.button_secondary)
                graficaDiaria.visibility=View.VISIBLE
                txtFecha.visibility=View.VISIBLE
            }
            R.id.btnAnalisisMes->{
                setearBotones()
                btnMes.setBackgroundResource(R.drawable.button_secondary)
                graficaMensual.visibility=View.VISIBLE
                spMes.visibility=View.VISIBLE
                spAno.visibility=View.VISIBLE
            }
            R.id.btnAnalisisAno->{
                setearBotones()
                btnAno.setBackgroundResource(R.drawable.button_secondary)
                modoAno.visibility=View.VISIBLE
                spAnual.visibility=View.VISIBLE
            }
        }
    }


}