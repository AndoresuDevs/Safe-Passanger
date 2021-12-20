package com.andoresudev.safepassenger.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.andoresudev.safepassenger.R
import com.andoresudev.safepassenger.models.comment
import com.andoresudev.safepassenger.models.report

class reports_adapter constructor(context: Context, busesList:ArrayList<report>): BaseAdapter() {
    var context: Context
    var CommentList:ArrayList<report>

    init {
        this.context=context
        this.CommentList=busesList
    }

    override fun getCount(): Int {
        return CommentList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val actual = CommentList[position]

        if (view==null)
            view= LayoutInflater.from(context).inflate(R.layout.report_listview_design,null)

        val txtFecha: TextView = view?.findViewById(R.id.reportFechaLv)!!
        val txtChofer: TextView = view.findViewById(R.id.reportChoferLv)!!
        val txtMotivo: TextView = view.findViewById(R.id.reportMotivoLv)!!
        val txtDesc: TextView = view.findViewById(R.id.reportDescripcionLv)!!


        txtFecha.text=actual.fecha
        txtChofer.text=actual.chofer
        txtMotivo.text=actual.motivo
        txtDesc.text=actual.descripcion


        return  view
    }

}