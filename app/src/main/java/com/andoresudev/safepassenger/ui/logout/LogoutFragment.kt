package com.andoresudev.safepassenger.ui.logout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andoresudev.safepassenger.MainActivity
import com.andoresudev.safepassenger.R
import com.google.firebase.auth.FirebaseAuth

class LogoutFragment : Fragment() {

    private lateinit var slideshowViewModel: LogoutViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogout:Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProvider(this).get(LogoutViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_logout, container, false)
        auth = FirebaseAuth.getInstance()
        btnLogout=root.findViewById(R.id.btnCerrarSesion)
        btnLogout.setOnClickListener(View.OnClickListener {
            logout()
        })

        return root

    }


    private fun logout(){
        Toast.makeText(activity, R.string.sucesfull_logout, Toast.LENGTH_SHORT).show()
        auth.signOut()
        activity?.startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }





}