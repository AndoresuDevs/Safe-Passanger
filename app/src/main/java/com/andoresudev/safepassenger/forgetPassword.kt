package com.andoresudev.safepassenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class forgetPassword : AppCompatActivity() {
    private lateinit var txtEmail: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        txtEmail=findViewById(R.id.txtCorreo)
        auth = FirebaseAuth.getInstance()
    }


    fun restorePassword(view: View){
        val email = txtEmail.text.toString() //aqui vacias el strin del cuadro de texto
        if(email.isEmpty()){
            Toast.makeText(this,R.string.fill_email, Toast.LENGTH_SHORT).show()
        }else{
            auth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                task->

                if(task.isSuccessful){
                    Toast.makeText(this,R.string.email_send, Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this,R.string.email_fail, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }




    fun registerViewFromPassword(view: View){
        startActivity(Intent(this, register::class.java))
        finish()
    }

    fun loginViewFromPassword(view: View){
        finish()
    }
}