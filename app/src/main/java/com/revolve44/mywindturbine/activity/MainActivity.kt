package com.revolve44.mywindturbine.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import com.revolve44.mywindturbine.R
import com.revolve44.mywindturbine.repository.WindRepository
import com.revolve44.mywindturbine.storage.PreferenceMaestro
import com.revolve44.mywindturbine.utils.blinkATextView
import com.revolve44.mywindturbine.utils.listOfColor
import com.revolve44.mywindturbine.viewmodels.MainScreenViewModel
import com.revolve44.mywindturbine.viewmodels.ViewModelProviderFactory
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    lateinit var mainScreenViewModel : MainScreenViewModel
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private lateinit var headerOfDrawer: LinearLayout
    private lateinit var headerDrawerTitle: TextView
    private lateinit var chosenStation : TextView
    private lateinit var titleofActionBar: TextView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var mainNavDrawer: DrawerLayout
    private lateinit var toNavDrawer: ImageView
    private lateinit var powerOutputIndicator: ImageView
    private lateinit var main_drawer_layout: DrawerLayout
    //private lateinit var header_drawer_title : TextView

    private fun firstLaunch() {
        if (PreferenceMaestro.firstStart) {
            //viewModel.forecastPower.value = 0f // set zero because needed set PV station characteristics

            val intent = Intent(this, AddSolarStationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme) // for splash screen [1]
        firstLaunch()

        val repository = WindRepository(application)
        val viewModelProviderFactory = ViewModelProviderFactory(application, repository)
        mainScreenViewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainScreenViewModel::class.java)

        setContentView(R.layout.activity_main)
        //supportActionBar?.setDisplayHomeAsUpEnabled(false)
        //supportActionBar?.

        main_drawer_layout = findViewById(R.id.main_drawer_layout)
        initNavigation()
        initToggleInActionBar()
        textColorChanged()
        navDrawerBlink()
//        initActionBarandDrawerView()
//        initNavController()



    }

    private fun textColorChanged() {
//        blinkATextView(
//                header_drawer_title,
//                listOfColor(PreferenceMaestro.pickedColorofToolbarTitle),
//                Color.BLACK,
//                listOfColor(PreferenceMaestro.pickedColorofToolbarTitle),
//                1200
//        )

    }

    private fun initToggleInActionBar() {
        toNavDrawer = findViewById(R.id.to_navdrawer)
        toNavDrawer.setOnClickListener {
            val drawerLayout = findViewById<View>(R.id.main_drawer_layout) as DrawerLayout
            //To Open:
            drawerLayout.openDrawer(GravityCompat.START);
            //https://stackoverflow.com/questions/52973788/android-drawerlayout-opendrawer-with-gravity-start-creates-a-lint-error-must
        }
    }

    private fun initNavigation(){
        toolbar = findViewById(R.id.main_toolbar)
        toolbar.navigationIcon = null
        setSupportActionBar(toolbar)
        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        mainNavDrawer = findViewById(R.id.main_drawer_layout)
        val navigationView: NavigationView = findViewById<View>(R.id.main_navigation_view) as NavigationView
        val headerView: View = navigationView.inflateHeaderView(R.layout.navigation_header)
        //elements of navigation header
        headerOfDrawer = headerView.findViewById(R.id.header_of_drawer)
        headerDrawerTitle = headerView.findViewById(R.id.header_drawer_title)
        chosenStation = headerView.findViewById(R.id.chosenPVStation)
        //mainNavDrawer.addDrawerListener(DrawerLayout.DrawerListener)




        navController = findNavController(R.id.main_nav_host)
//        appBarConfiguration = AppBarConfiguration(
//            setOf(R.id.mainFragment, R.id.ListofStationFragment,
//                R.id.CalibrationFragment,R.id.SettingsFragment),mainNavDrawer
//        )
        //setupActionBarWithNavController(navController,appBarConfiguration)
        //navigationView.setupWithNavController(navController)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mainFragment -> {
                    findNavController(R.id.main_nav_host).navigate(R.id.mainFragment)
                    true
                }
                R.id.settingsFragment -> {
                    findNavController(R.id.main_nav_host).navigate(R.id.settingsFragment)
                    true
                }
                R.id.calibrationFragment -> {
                    findNavController(R.id.main_nav_host).navigate(R.id.calibrationFragment)
                    true
                }
                R.id.SolApp -> {
                    //findNavController(R.id.navHostFragment).navigate(R.id.splashFragment)
                    goToUrl("https://play.google.com/store/apps/details?id=com.revolve44.solarpanelx&hl=en_US&gl=US")
                    //Toast.makeText(applicationContext,"", Toast.LENGTH_SHORT).show()
                    Timber.i("go to solar panel app")
                    true
                }

                else -> false
            }.also {
                mainNavDrawer.closeDrawer(GravityCompat.START)
            }

        }
    }

    private fun navDrawerBlink(){
        mainNavDrawer.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                //Called when a drawer's position changes.
            }

            override fun onDrawerOpened(drawerView: View) {
                //Called when a drawer has settled in a completely open state.
                //The drawer is interactive at this point.
                // If you have 2 drawers (left and right) you can distinguish
                // them by using id of the drawerView. int id = drawerView.getId();
                // id will be your layout's id: for example R.id.left_drawer
                //blink header
                blinkATextView(
                        headerDrawerTitle,
                        listOfColor(PreferenceMaestro.pickedColorofToolbarTitle),
                        Color.BLACK,
                        listOfColor(PreferenceMaestro.pickedColorofToolbarTitle),
                        1200
                )

                var numberOfColor=0
                // listener for change text color
                headerOfDrawer.setOnClickListener {
                    if (numberOfColor<6){
                        blinkATextView(
                                headerDrawerTitle,
                                listOfColor(PreferenceMaestro.pickedColorofToolbarTitle),
                                Color.BLACK,
                                listOfColor(PreferenceMaestro.pickedColorofToolbarTitle + 1),
                                1200
                        )

                        numberOfColor++
                    }else{
                        numberOfColor=0
                    }
                    PreferenceMaestro.pickedColorofToolbarTitle = numberOfColor

                }
            }

            override fun onDrawerClosed(drawerView: View) {
                // Called when a drawer has settled in a completely closed state.
            }

            override fun onDrawerStateChanged(newState: Int) {
                // Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
            }
        })


    }

    fun showOutputIndicator(powerOutput: Float){
        powerOutputIndicator = findViewById(R.id.outputIndicator)

        var percentOfNomial = ((powerOutput/PreferenceMaestro.chosenStationNOMINALPOWER)*100).toInt()
        when(percentOfNomial){
            in 1..30 ->{powerOutputIndicator.setColorFilter(ContextCompat.getColor(applicationContext,R.color.redx))}
            in 31..60 ->{powerOutputIndicator.setColorFilter(ContextCompat.getColor(applicationContext,R.color.orangex))}
            in 61..100 ->{powerOutputIndicator.setColorFilter(ContextCompat.getColor(applicationContext,R.color.greenx))}

        }

        powerOutputIndicator.setOnClickListener {
            Toast.makeText(applicationContext, "Current power generation level: \n 1-30% of Nominal Power (Color Red)\n" +
                    "31-60% of Nominal Power (Color Orange)\n" +
                    "61-100% of Nominal Power (Color Green)",Toast.LENGTH_LONG).show()
        }

    }



//    private fun initActionBarandDrawerView() {
//
//
//
//        //mainNavDrawer
//        toolbar.navigationIcon = null
//
//
//
//        titleofActionBar = findViewById(R.id.actionbarTitle)
//
//        val navigationView: NavigationView = findViewById<View>(R.id.main_navigation_view) as NavigationView
//        val headerView: View = navigationView.inflateHeaderView(R.layout.navigation_header)
//        headerOfDrawer = headerView.findViewById(R.id.header_of_drawer)
//        headerDrawerTitle = headerView.findViewById(R.id.header_drawer_title)
//        chosenStation = headerView.findViewById(R.id.chosenPVStation)
//
//
//    }

    fun showProgressBar(labelOfActionBar: String){
        titleofActionBar = findViewById(R.id.actionbarTitle)
        titleofActionBar.text = labelOfActionBar
        blinkATextView(
                titleofActionBar,
                Color.WHITE,
                Color.BLACK, Color.WHITE, 1000
        )

    }


    @SuppressLint("SetTextI18n")
    fun refreshNavigationHeader(){
        // All data from SharedPreferences
        chosenStation.text =
            "Current PV station: \n${PreferenceMaestro.chosenStationCITY} ${PreferenceMaestro.chosenStationNOMINALPOWER}W"+
                    "\n(Lat:${PreferenceMaestro.chosenStationLAT}, Lon:${PreferenceMaestro.chosenStationLON})"
    }


    override fun onBackPressed() {
        //numOfClickOnBackPress++

        when { //If drawer layout is open close that on back pressed
            main_drawer_layout.isDrawerOpen(GravityCompat.START) -> {
                main_drawer_layout.closeDrawer(GravityCompat.START)
            }
            else -> {
                //if (numOfClickOnBackPress==2){
                super.onBackPressed() //If drawer is already in closed condition then go back
//
//                }else{
//                    Snackbar.make(findViewById(android.R.id.content),"Tap again, to exit",Snackbar.LENGTH_LONG).show()
//                }
            }
        }
    }

    private fun goToUrl(url: String) {
        val uriUrl: Uri = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }

}