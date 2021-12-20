package com.andoresudev.safepassenger.ui.slideshow

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andoresudev.myapplication.ui.slideshow.SlideshowViewModel
import com.andoresudev.safepassenger.R
import com.andoresudev.safepassenger.main_menu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class SlideshowFragment : Fragment(), View.OnClickListener {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var Espanol1: TextView
    private lateinit var Espanol2: TextView
    private lateinit var Ingles1: TextView
    private lateinit var Ingles2: TextView
    private lateinit var Frances1: TextView
    private lateinit var Frances2: TextView
    private lateinit var Aleman1: TextView
    private lateinit var Aleman2: TextView
    private lateinit var Portugues1: TextView
    private lateinit var Portugues2: TextView


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProvider(this).get(SlideshowViewModel::class.java)
        val vista = inflater.inflate(R.layout.fragment_slideshow, container, false)
        auth= FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference
        Espanol1 = vista.findViewById(R.id.txtEspañol1)
        Ingles1=vista.findViewById(R.id.txtIngles1)
        Espanol2 = vista.findViewById(R.id.txtEspañol2)
        Ingles2=vista.findViewById(R.id.txtIngles2)
        Frances1 = vista.findViewById(R.id.txtFrances)
        Frances2=vista.findViewById(R.id.txtFrances2)
        Aleman1 = vista.findViewById(R.id.txtAleman)
        Aleman2=vista.findViewById(R.id.txtAleman2)
        Portugues1 = vista.findViewById(R.id.txtPortugues)
        Portugues2=vista.findViewById(R.id.txtPortugues2)


        //setearGris()
        //resaltarLenSelected()
        return vista
    }

    private fun setearGris(){
        Espanol1.setTextColor(resources.getColor(R.color.grey))
        Espanol2.setTextColor(resources.getColor(R.color.grey))
        Ingles1.setTextColor(resources.getColor(R.color.grey))
        Ingles2.setTextColor(resources.getColor(R.color.grey))
        Frances1.setTextColor(resources.getColor(R.color.grey))
        Frances2.setTextColor(resources.getColor(R.color.grey))
        Aleman1.setTextColor(resources.getColor(R.color.grey))
        Aleman2.setTextColor(resources.getColor(R.color.grey))
        Portugues1.setTextColor(resources.getColor(R.color.grey))
        Portugues2.setTextColor(resources.getColor(R.color.grey))
    }


    fun changeLanguage(len:String,txt1:TextView,txt2:TextView){
        val config: Configuration = Configuration()
        val locale: Locale = Locale(len)
        config.locale=locale
        resources.updateConfiguration(config,null)

        setearGris()
        txt1.setTextColor(resources.getColor(R.color.black))
        txt2.setTextColor(resources.getColor(R.color.black))
        dbReference.child("Usuarios").child(auth.currentUser?.uid.toString()).child("userLen").setValue(len)
        activity?.startActivity(Intent(activity, main_menu::class.java))
        activity?.finish()

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.txtEspañol1, R.id.txtEspañol2->{
               //changeLanguage("es",Espanol1,Espanol2)
            }

            R.id.txtIngles1, R.id.txtIngles2->{
                //changeLanguage("en",Ingles1,Ingles2)
            }

            R.id.txtFrances, R.id.txtFrances2->{
                //changeLanguage("fr",Frances1,Frances2)
            }

            R.id.txtAleman, R.id.txtAleman2->{
                //changeLanguage("de",Aleman1,Aleman2)
            }

            R.id.txtPortugues, R.id.txtPortugues2->{
                //changeLanguage("pt",Portugues1,Portugues2)
            }
        }
    }


    private fun resaltarLenSelected(){
        dbReference.child("Usuarios").child(auth.currentUser?.uid.toString()).child("userLen").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    when(snapshot.value.toString()){
                        "es"->{
                            Espanol1.setTextColor(resources.getColor(R.color.black))
                            Espanol2.setTextColor(resources.getColor(R.color.black))
                        }
                        "en"->{
                            Ingles1.setTextColor(resources.getColor(R.color.black))
                            Ingles2.setTextColor(resources.getColor(R.color.black))
                        }
                        "fr"->{
                            Frances1.setTextColor(resources.getColor(R.color.black))
                            Frances2.setTextColor(resources.getColor(R.color.black))
                        }
                        "de"->{
                            Aleman1.setTextColor(resources.getColor(R.color.black))
                            Aleman2.setTextColor(resources.getColor(R.color.black))
                        }
                        "pt"->{
                            Portugues1.setTextColor(resources.getColor(R.color.black))
                            Portugues2.setTextColor(resources.getColor(R.color.black))
                        }
                    }


                } else {

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }

}