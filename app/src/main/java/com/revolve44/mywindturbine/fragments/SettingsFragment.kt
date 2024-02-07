package com.revolve44.mywindturbine.fragments


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.revolve44.mywindturbine.R
import com.revolve44.mywindturbine.activity.AddWindStationActivity
import com.revolve44.mywindturbine.activity.MainActivity
import timber.log.Timber

//import com.revolve44.solarpanelx.activity.MainActivity
//import com.revolve44.solarpanelx.R
//import com.revolve44.solarpanelx.activity.AddSolarStationActivity


/**
 * SettingsFragment
 */

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    lateinit var setCharacteristics : Button
    lateinit var mListView: ListView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showProgressBar("Settings")

        initListViewOfSettings(view)


    }

    private fun initListViewOfSettings(view: View){
        var array: ArrayList<String> = ArrayList()
        //arrayOf("PV Stations Characteristics", "Check Updates","Pro Version","About Us")
        array.add("PV Stations Characteristics")
        array.add("Check Updates")
        array.add("Pro Version")
        array.add("About Us")
        // access the listView from xml file
        mListView = view.findViewById<ListView>(R.id.settings_list)
        val arrayAdapter = ArrayAdapter(
            activity as MainActivity,
            R.layout.settings_list_item, array
        )
        mListView.adapter = arrayAdapter

        mListView.setOnItemClickListener { parent, view, position, id ->
            //Toast.makeText(activity,"pos $position",Toast.LENGTH_SHORT).show()
            when(position){
                0 -> {
                    val intent = Intent(activity, AddWindStationActivity::class.java)
                    startActivity(intent)
                }
                1 -> goToUrl("https://play.google.com/store/apps/dev?id=8066230504463138782&hl=en_US&gl=US")

                2 -> Toast.makeText(activity, "Coming soon", Toast.LENGTH_SHORT).show()
                3 ->showDialogAboutUs()

            }

        }
    }

    private fun goToUrl(url: String) {
        val uriUrl: Uri = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }

    private fun showDialogAboutUs(){
        val alertDialog: AlertDialog = AlertDialog.Builder(activity).create()
        alertDialog.setTitle("My Wind Turbine (v. 1.3)")
        alertDialog.setMessage("this is a new generation of apps for solar panels. \n" +
                "Features: \n" +
                "+ forecasting of solar panels generation on \n" +
                ">Hour >Week >Month \n" +
                "+ Detailed three charts showing the change in forecast production for 3 days ahead\n" +
                "+ You may calibrate forecast\n" +
                "Future features (in the first half of 2021):\n" +
                "++ First neural network for forecasting of solar panels \n" +
                "++ Sunshine analyze tool for future/current location of PV station\n" +
                "++ Improve UI (custom colors, more stable UI)\n" +
                "++ Notifications about cleaning time of your solar panels ")
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
//        alertDialog.setButton(
//            AlertDialog.BUTTON_POSITIVE, "Don't show again",
//            DialogInterface.OnClickListener { dialog, which ->
//                editor.putBoolean("showagain", false)
//                editor.apply()
//                dialog.dismiss()
//            })
        alertDialog.show()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.i("attach Called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("oncreate Called")
    }
    override fun onResume() {
        super.onResume()
        Timber.i("onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy Called")
    }


}
