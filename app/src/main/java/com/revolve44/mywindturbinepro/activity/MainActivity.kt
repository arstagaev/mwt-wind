package com.revolve44.mywindturbinepro.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.revolve44.mywindturbinepro.R
import com.revolve44.mywindturbinepro.repository.WindRepository
import com.revolve44.mywindturbinepro.storage.PreferenceMaestro
import com.revolve44.mywindturbinepro.utils.Constants.Companion.IS_PRO_VERSION
import com.revolve44.mywindturbinepro.utils.blinkATextView
import com.revolve44.mywindturbinepro.utils.gradientAnimation2
import com.revolve44.mywindturbinepro.utils.listOfColor
import com.revolve44.mywindturbinepro.viewmodels.MainScreenViewModel
import com.revolve44.mywindturbinepro.viewmodels.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    lateinit var viewModel : MainScreenViewModel
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

        initFirebaseAnalytics()

        setContentView(R.layout.activity_main)
        //supportActionBar?.setDisplayHomeAsUpEnabled(false)
        //supportActionBar?.
        initNavigation()
        initToggleInActionBar()
        textColorChanged()
        navDrawerBlink()
        listenerForChangeColorOfHeaderText()
        switchBetweenVersion()
//        initActionBarandDrawerView()
//        initNavController()

        val repository = WindRepository(application)
        val viewModelProviderFactory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(
            MainScreenViewModel::class.java
        )


    }

    private fun switchBetweenVersion() {
        Timber.i("is Pro version = "+ IS_PRO_VERSION)
        if (!IS_PRO_VERSION){
            headerDrawerTitle.text = "My Wind Turbine"
        }else{
            headerDrawerTitle.text = "My Wind Turbine \nPro"
        }

    }

    private fun initFirebaseAnalytics() {
        var firebaseAnalytics: FirebaseAnalytics = Firebase.analytics
        // Obtain the FirebaseAnalytics instance.
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

    private fun listenerForChangeColorOfHeaderText(){
        var numberOfColor= PreferenceMaestro.pickedColorofToolbarTitle
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
                R.id.setPVStation -> {
                    val intent = Intent(this, AddSolarStationActivity::class.java)
                    startActivity(intent)
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
//                blinkATextView(
//                    headerDrawerTitle,
//                    listOfColor(PreferenceMaestro.pickedColorofToolbarTitle),
//                    Color.BLACK,
//                    listOfColor(PreferenceMaestro.pickedColorofToolbarTitle),
//                    1200
//                )


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

        var percentOfNomial = ((powerOutput/(PreferenceMaestro.chosenStationNOMINALPOWER))*100)
        when(percentOfNomial){
            in 0f..30f -> {
                gradientAnimation2(powerOutputIndicator,Color.BLACK,Color.RED,2000)
//                powerOutputIndicator.setColorFilter(
//                    ContextCompat.getColor(
//                        applicationContext,
//                        R.color.redx
//                    )
//                )
            }
            in 31f..60f -> {
                gradientAnimation2(powerOutputIndicator,Color.BLACK,
                        ContextCompat.getColor(
                                applicationContext,
                                R.color.orangex
                        ),2000)
//                powerOutputIndicator.setColorFilter(
//                    ContextCompat.getColor(
//                        applicationContext,
//                        R.color.orangex
//                    )
//                )
            }
            in 61f..100f -> {
                gradientAnimation2(powerOutputIndicator,Color.BLACK,Color.GREEN,2000)
//                powerOutputIndicator.setColorFilter(
//                    ContextCompat.getColor(
//                        applicationContext,
//                        R.color.greenx
//                    )
//                )
            }
        }
//        gradientAnimation2(powerOutputIndicator,Color.BLACK,
//            ContextCompat.getColor(
//                applicationContext,
//                R.color.orangex
//            ),2000)

        powerOutputIndicator.setOnClickListener {
            Toast.makeText(
                    applicationContext,
                    getString(R.string.mainscreen_powerindicator_tip),
                    Toast.LENGTH_LONG
            ).show()
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
            "Current Wind station: \n${PreferenceMaestro.chosenStationCITY} ${PreferenceMaestro.chosenStationNOMINALPOWER}W"+
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