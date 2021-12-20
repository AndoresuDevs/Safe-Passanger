package com.andoresudev.safepassenger

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class MainActivity : AppCompatActivity(){

    private lateinit var txtEmail: EditText
    private lateinit var txtPass: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var check: CheckBox

    // private var location: Locale = Locale("es", "MX")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtEmail = findViewById(R.id.txtCorreo)
        txtPass = findViewById(R.id.txtContra)
        check=findViewById(R.id.checkBox)
        auth= FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference

    }

    fun login(view: View) {
        loginUser()
    }

    private fun loginUser() {
        val user = txtEmail.text.toString()
        val pass = txtPass.text.toString()
        var rolUser =""

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, R.string.fill_all_txt, Toast.LENGTH_SHORT).show()
        } else {
            auth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if (check.isChecked){
                            dbReference.child("Usuarios").child(auth.currentUser?.uid.toString()).addValueEventListener(object: ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val privilegio= snapshot.child("userRol").value.toString()
                                    Toast.makeText(this@MainActivity, privilegio, Toast.LENGTH_SHORT).show()
                                    if (privilegio=="driver"){
                                        dbReference.child("Usuarios").child(auth.currentUser?.uid.toString()).child("userMode").setValue("admin")
                                    }else{
                                        dbReference.child("Usuarios").child(auth.currentUser?.uid.toString()).child("userMode").setValue("user")
                                        Toast.makeText(this@MainActivity,R.string.admin_only,Toast.LENGTH_SHORT).show()
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {

                                }
                            })


                        }else{
                            dbReference.child("Usuarios").child(auth.currentUser?.uid.toString()).child("userMode").setValue("user")
                        }
                        startActivity(Intent(this, main_menu::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, R.string.incorrect_data, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    fun forPassView(view: View) {
        startActivity(Intent(this, forgetPassword::class.java))
    }

    fun forRegisterView(view: View) {
       startActivity(Intent(this, register::class.java))

    }

    override fun onBackPressed() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(R.string.close_app_confirm)
            .setCancelable(false)
            .setPositiveButton(R.string.getout, DialogInterface.OnClickListener { dialog, id ->
                finish()
            })
            .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        dialogBuilder.show()
    }


    override fun onStart() {
        super.onStart()

        if(auth.currentUser!=null){
            startActivity(Intent(this, main_menu::class.java))
            finish()
        }
    }




}






