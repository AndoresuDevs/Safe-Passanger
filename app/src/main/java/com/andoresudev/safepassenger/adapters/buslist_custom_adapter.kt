package com.andoresudev.safepassenger.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import com.andoresudev.safepassenger.R
import com.andoresudev.safepassenger.models.bus

class Buslist_custom_adapter constructor(context: Context,busesList:ArrayList<bus>): BaseAdapter() {

    var context: Context
    var busesList:ArrayList<bus>

    init {
        this.context=context
        this.busesList=busesList
    }


    override fun getCount(): Int {
        return  busesList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val actualBus = busesList[position]

        if (view==null)
            view=LayoutInflater.from(context).inflate(R.layout.bus_listview_design,null)

        val txtId: TextView = view?.findViewById(R.id.txtIdLvd)!!
        val txtChofer: TextView = view.findViewById(R.id.txtChoferLvd)!!
        val txtEstado: TextView = view.findViewById(R.id.txtEstadoLvd)!!
        val txtRuta: TextView = view.findViewById(R.id.txtRutaLvd)!!

        txtId.text = actualBus.busId
        txtChofer.text = actualBus.Chofer
        txtEstado.text = actualBus.Estado
        txtRuta.text = actualBus.Ruta
        return view
    }



}

