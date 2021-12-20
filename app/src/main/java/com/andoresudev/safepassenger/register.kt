package com.andoresudev.safepassenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.andoresudev.safepassenger.models.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class register : AppCompatActivity() {

    private lateinit var txtUserame: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtPass: EditText
    private lateinit var txtPass2: EditText
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        txtUserame=findViewById(R.id.txtUsuario)
        txtEmail=findViewById(R.id.txtCorreo)
        txtPass=findViewById(R.id.txtContra)
        txtPass2=findViewById(R.id.txtContra2)

        database= FirebaseDatabase.getInstance()
        auth= FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference

    }

    fun register(view:View){
        createNewAccount()
    }

    private fun createNewAccount(){


        //VARIABLES CON LA INFORMACION
        //PUEDES CAMBIARLAS SI  QUIERES
        var username=""
        var email=""
        var pass=""
        var pass2=""


        //AQUI SE SACA EL TEXTO DEL RECUADRO Y SE GUARDA EN LA VARIABLE
        //VARIABLE=CUADRO DE TEXTO.TRAER TEXTO.EN FORMA DE TEXTO
        username=txtUserame.text.toString()
        email=txtEmail.text.toString()
        pass=txtPass.text.toString()
        pass2=txtPass2.text.toString()



        //VALIDACIONES DE LLENAR TODOS LOS CAMPOS
        if (username.isEmpty()||email.isEmpty()||pass.isEmpty()||pass2.isEmpty()){
            Toast.makeText(this,R.string.fill_all_txt,Toast.LENGTH_SHORT).show()
                                        //EN ESTOS CASOS TE SALDRA ERROR PERO SOLO QUITALO Y PON ENTRE COMILLAS EL TEXTO
        }else{
            //LA CONTRASEÑA DEBE TENER MAS DE 6 CARACTERES
            if(pass.length>=6){
                if (pass.equals(pass2)){


                    //ESTA ES LA FUNCION PARA AUTENTICAR SOLO SE LE PASA CORREO Y CONTRA
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this){
                            task->
                        if(task.isComplete){
                            val user:FirebaseUser?=auth.currentUser
                            verifyEmail(user)  //ESTA ES LA VALIDACION DEL COFRREO

                            //AQUI YO CREE   UN OBJETO PARA NO BATALLAR PERO PUEDES HACERLO COMO QUIERAS
                            var newUser = user()
                            newUser.userId= user?.uid.toString()
                            newUser.userName=username
                            newUser.userEmail=email
                            newUser.userPass=pass
                            newUser.userLen="es"
                            newUser.userRol="user"
                            newUser.userPayroll=""
                            newUser.userLicence=""
                            newUser.userCirculation=""
                            newUser.userMode="user"
                            newUser.userTravels=""
                            newUser.userReports=""

                            //AQUI INSERTAS LOS DATOS A LA DB
                            dbReference.child("Usuarios").child(newUser.userId).setValue(newUser).addOnCompleteListener(this){
                                    task->

                                if(task.isComplete){
                                    Toast.makeText(this,R.string.singin_complete,Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(this,R.string.singin_fail,Toast.LENGTH_SHORT).show()
                                }
                            }

                            finish() //ESTO CIERRA EL REGISTRO

                        }else{
                            Toast.makeText(this,R.string.singin_fail,Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this,R.string.pass_diferent,Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,R.string.pass_short,Toast.LENGTH_SHORT).show()
            }

        }

    }

    //ESTA  VERIFICA EL EMAIL
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



    fun login_view(view: View){
        finish()
    }

    fun dirverRegisterVIew(view: View){
       startActivity(Intent(this, driverRegister::class.java))
        finish()
    }
}