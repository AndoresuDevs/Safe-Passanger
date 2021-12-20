package com.andoresudev.safepassenger.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.andoresudev.safepassenger.R
import com.andoresudev.safepassenger.models.busStop

class route_custom_adapter constructor(context: Context, routeList:ArrayList<busStop>): BaseAdapter() {
    var context: Context
    var routeList:ArrayList<busStop>

    init {
        this.context=context
        this.routeList=routeList
    }

    override fun getCount(): Int {
        return routeList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val actualStop = routeList[position]


        if (view==null)
            view= LayoutInflater.from(context).inflate(R.layout.route_listview_desing,null)

        val txtNombre: TextView = view?.findViewById(R.id.nombre_ruta_lvd)!!
        val txtDireccion:TextView = view.findViewById(R.id.direc_ruta_lvd)!!
        val txtNumParada:TextView = view.findViewById(R.id.num_parada_lvd)!!
        txtNombre.text = actualStop.name
        txtDireccion.text=actualStop.direction
        txtNumParada.text=actualStop.id


        return view
    }
}