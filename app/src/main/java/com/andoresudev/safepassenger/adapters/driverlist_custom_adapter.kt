package com.andoresudev.safepassenger.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.andoresudev.safepassenger.R
import com.andoresudev.safepassenger.models.user

class driverlist_custom_adapter constructor(context: Context, busesList:ArrayList<user>): BaseAdapter() {
    var context: Context
    var driversList:ArrayList<user>

    init {
        this.context=context
        this.driversList=busesList
    }

    override fun getCount(): Int {
        return driversList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val actualDriver = driversList[position]

        if (view==null)
            view= LayoutInflater.from(context).inflate(R.layout.driver_listview_desing,null)

        val txtNombre: TextView = view?.findViewById(R.id.txtEmpleadoLvdddd)!!
        val txtNomina: TextView = view.findViewById(R.id.txtNominaLvd)
        val txtLicencia: TextView = view.findViewById(R.id.txtLicenciaLvd)
        val txtCircula: TextView = view.findViewById(R.id.txtCirculacionLvd)
        txtNombre.text = actualDriver.userName
        txtNomina.text = actualDriver.userPayroll
        txtLicencia.text = actualDriver.userLicence
        txtCircula.text = actualDriver.userCirculation


        return view
    }
}