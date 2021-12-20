package com.andoresudev.safepassenger


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainer
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.andoresudev.safepassenger.ui.bus.BusFragment
import com.andoresudev.safepassenger.ui.slideshow.SlideshowFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class main_menu : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference

        drawerLayout= findViewById(R.id.drawer_layout2)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_logout
                ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }





    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        else {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage(R.string.close_app_confirm)
                .setCancelable(false)
                .setPositiveButton(R.string.getout) { dialog, id ->
                    finish()
                }
                    .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })

            dialogBuilder.show()
        }
    }

    /*
    override fun onBackPressed() {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage(R.string.close_app_confirm)
                .setCancelable(false)
                .setPositiveButton(R.string.getout) { dialog, id ->
                    finish()
                }
                    .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })

            dialogBuilder.show()
    }
     */

    fun changeLanguageEN(view:View){
        val config: Configuration = Configuration()
        val locale: Locale = Locale("en") //LAS DOS LETRITAS SON LA CLAVE DEL IDIOMA
        config.locale=locale
        resources.updateConfiguration(config,null)
        //LAS SIGUIENTES 2 LINEAS REINICIAN LA ACTIVITY PARA QUE SE CAMBIE DE IDIOMA
        startActivity(Intent(this, main_menu::class.java))
        finish()
    }

    fun changeLanguageES(view:View){
        val config: Configuration = Configuration()
        val locale: Locale = Locale("es")
        config.locale=locale
        resources.updateConfiguration(config,null)
        startActivity(Intent(this, main_menu::class.java))
        finish()
    }

    fun changeLanguageDE(view:View){
        val config: Configuration = Configuration()
        val locale: Locale = Locale("de")
        config.locale=locale
        resources.updateConfiguration(config,null)
        startActivity(Intent(this, main_menu::class.java))
        finish()
    }

    fun changeLanguageFR(view:View){
        val config: Configuration = Configuration()
        val locale: Locale = Locale("fr")
        config.locale=locale
        resources.updateConfiguration(config,null)
        startActivity(Intent(this, main_menu::class.java))
        finish()
    }

    fun changeLanguageBR(view:View){
        val config: Configuration = Configuration()
        val locale: Locale = Locale("pt")
        config.locale=locale
        resources.updateConfiguration(config,null)
        startActivity(Intent(this, main_menu::class.java))
        finish()
    }


}