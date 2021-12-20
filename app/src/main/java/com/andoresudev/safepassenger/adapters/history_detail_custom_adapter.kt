package com.andoresudev.safepassenger.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.andoresudev.safepassenger.R
import com.andoresudev.safepassenger.models.travels

class history_detail_custom_adapter constructor(context: Context, detailList:ArrayList<travels>): BaseAdapter() {
    var context: Context
    var detailList:ArrayList<travels>

    init {
        this.context=context
        this.detailList=detailList
    }

    override fun getCount(): Int {
        return detailList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val travel = detailList[position]


        if (view==null)
            view= LayoutInflater.from(context).inflate(R.layout.history_detail_listview_adapter,null)

        val txtHora: TextView = view?.findViewById(R.id.txtHistoryHora)!!
        val txtPeriodo: TextView = view.findViewById(R.id.txtHistoryPeriodo)!!
        val txtRuta: TextView = view.findViewById(R.id.txtHistoryRuta)!!
        val txtCamion: TextView = view.findViewById(R.id.txtHistoryCamion)!!
        println("adapter: ")
        println(travel.inicio)
        txtHora.text=travel.inicio
        val periodo ="${travel.inicio} - ${travel.fin}"
        txtPeriodo.text=periodo
        txtRuta.text=travel.ruta
        txtCamion.text=travel.camion
        return view
    }



}