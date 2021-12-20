package com.andoresudev.safepassenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class driverRegister : AppCompatActivity() {

    private lateinit var txtUserame: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtPass: EditText
    private lateinit var txtPass2: EditText
    private lateinit var txtNomina: EditText
    private lateinit var txtLicencia: EditText
    private lateinit var txtCircu: EditText
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_register)

        txtUserame=findViewById(R.id.txtUsuarioDriv)
        txtEmail=findViewById(R.id.txtCorreoDriv)
        txtPass=findViewById(R.id.txtContraDriv)
        txtPass2=findViewById(R.id.txtContra2Driv)
        txtNomina=findViewById(R.id.txtNominaDriv)
        txtLicencia=findViewById(R.id.txtLicenciaDriv)
        txtCircu=findViewById(R.id.txtCircDriv)

        database= FirebaseDatabase.getInstance()
        auth= FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference
    }

    fun registerDriver(view:View){
        createNewAccount()
    }

    private fun createNewAccount(){
        var username=""
        var email=""
        var pass=""
        var pass2=""
        var nomina=""
        var licencia=""
        var circulacion=""



        username=txtUserame.text.toString()
        email=txtEmail.text.toString()
        pass=txtPass.text.toString()
        pass2=txtPass2.text.toString()
        nomina=txtNomina.text.toString()
        licencia=txtLicencia.text.toString()
        circulacion=txtCircu.text.toString()


        if (username.isEmpty()||email.isEmpty()||pass.isEmpty()||pass2.isEmpty()||nomina.isEmpty()||licencia.isEmpty()||circulacion.isEmpty()){
            Toast.makeText(this,R.string.fill_all_txt, Toast.LENGTH_SHORT).show()
        }else{
            if(pass.length>=6){
                if (pass.equals(pass2)){
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this){
                            task->

                        if(task.isComplete){
                            val user: FirebaseUser?=auth.currentUser
                            verifyEmail(user)
                            var newUser = com.andoresudev.safepassenger.models.user()
                            newUser.userId= user?.uid.toString()
                            newUser.userName=username
                            newUser.userEmail=email
                            newUser.userPass=pass
                            newUser.userLen="es"
                            newUser.userRol="driver"
                            newUser.userPayroll=nomina
                            newUser.userLicence=licencia
                            newUser.userCirculation=circulacion
                            dbReference.child("Usuarios").child(newUser.userId).setValue(newUser).addOnCompleteListener(this){
                                    task->

                                if(task.isComplete){
                                    Toast.makeText(this,R.string.singin_complete, Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(this,R.string.singin_fail, Toast.LENGTH_SHORT).show()
                                }
                            }
                            finish()

                        }else{
                            Toast.makeText(this,R.string.singin_fail, Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this,R.string.pass_diferent, Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,R.string.pass_short, Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun verifyEmail(user: FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                    task->

                if(task.isComplete){
                    Toast.makeText(this,R.string.email_send,Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,R.string.email_fail,Toast.LENGTH_SHORT).show()
                }
            }
    }













    fun login_view_from_driverR(view: View){
        finish()
    }
}