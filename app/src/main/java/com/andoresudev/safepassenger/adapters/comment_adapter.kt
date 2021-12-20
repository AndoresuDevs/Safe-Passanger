package com.andoresudev.safepassenger.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.andoresudev.safepassenger.R
import com.andoresudev.safepassenger.models.comment
import com.andoresudev.safepassenger.models.user
import org.w3c.dom.Comment

class comment_adapter constructor(context: Context, busesList:ArrayList<comment>): BaseAdapter() {
    var context: Context
    var CommentList:ArrayList<comment>

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
            view= LayoutInflater.from(context).inflate(R.layout.comment_listview_design,null)

        val txtNombre: TextView = view?.findViewById(R.id.txtCommentLv)!!
        txtNombre.text=actual.message
        return  view
    }

}