package com.revolve44.mywindturbine.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.revolve44.mywindturbine.R
import com.revolve44.mywindturbine.fragments.LastConfirmFragment
import com.revolve44.mywindturbine.repository.WindRepository
import com.revolve44.mywindturbine.storage.PreferenceMaestro
import com.revolve44.mywindturbine.viewmodels.ViewModelAddSolarStation
//import com.revolve44.solarpanelx.R
//import com.revolve44.solarpanelx.repository.SolarRepository
//import com.revolve44.solarpanelx.storage.PreferenceMaestro
//import com.revolve44.solarpanelx.fragment.LastConfirmFragment
import com.revolve44.mywindturbine.fragments.MapFragment
//import com.revolve44.solarpanelx.viewmodels.ViewModelAddSolarStation
//import kotlinx.android.synthetic.main.activity_add_station.*
import timber.log.Timber

class AddSolarStationActivity : AppCompatActivity() {

    //lateinit var viewmodelAddSolarStation: ViewModelAddSolarStation

    //lateinit var viewPager2 : ViewPager2
    val titles = arrayOf("Map", "Characterisctics")
    //lateinit var toCharacteristics : Button
    lateinit var viewPager2 : ViewPager2
    lateinit var tab_layout : TabLayout
    //lateinit var to_characteristics : Button
    var mainActivity : MainActivity = MainActivity()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_station)
        Timber.i("AddSolarStationActivity started")
        //mainActivity.finish()

        val repo = WindRepository(application)

        val viewModelAddSolarStation: ViewModelAddSolarStation =
                ViewModelProvider(this).get(ViewModelAddSolarStation::class.java)



//        toCharacteristics = findViewById(R.id.to_characteristics)
        viewPager2 = findViewById(R.id.viewPager2)
        tab_layout = findViewById(R.id.tab_layout)

        //viewModelAddSolarStation.addSolarStation(SolarStation(12,144,"HORRAYY!!"))




        viewPager2.adapter = ViewPagerFragmentAdapter(this)
        //disabling swipe in viewpager
        viewPager2.isUserInputEnabled = false


        // attaching tab mediator
        TabLayoutMediator(tab_layout, viewPager2)
        { tab: TabLayout.Tab, position: Int ->
            tab.setText(titles.get(position))
        }.attach()
        //to_characteristics.setOnClickListener { }

    }

    fun gotoSecondPage() {
        viewPager2.currentItem = 2
    }

    class ViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        val titles = arrayOf("Map", "Characterisctics")

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return MapFragment()
                1 -> return LastConfirmFragment()
                //2 -> return TicketsFragment()
            }
            return MapFragment()
        }

        override fun getItemCount(): Int {
            return titles.size
        }
    }

    override fun onBackPressed() {
        if (PreferenceMaestro.firstStart){
            Snackbar.make(
                findViewById(android.R.id.content),
                "Please, fill correct all forms",
                Snackbar.LENGTH_LONG
            ).show()
        }else{
            super.onBackPressed()
        }

    }



//    override fun onBackPressed() {
//        viewPager2.currentItem = 1
//        //super.onBackPressed()
//    }
}