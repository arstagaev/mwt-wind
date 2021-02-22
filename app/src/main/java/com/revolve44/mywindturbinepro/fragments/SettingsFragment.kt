package com.revolve44.mywindturbinepro.fragments


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.revolve44.mywindturbinepro.R
import com.revolve44.mywindturbinepro.activity.AddSolarStationActivity
import com.revolve44.mywindturbinepro.activity.MainActivity
import com.revolve44.mywindturbinepro.storage.PreferenceMaestro
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
    private lateinit var screenSettings : LinearLayout

    private fun switchNightMode(){
        if (PreferenceMaestro.isNightNode){
            screenSettings.setBackgroundColor(
                    ContextCompat.getColor(
                            requireActivity(),
                            R.color.black_spoke
                    )
            )
        }else{
            screenSettings.setBackgroundColor(
                    ContextCompat.getColor(
                            requireActivity(),
                            R.color.hint_white
                    )
            )
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showProgressBar("Settings")
        screenSettings = view.findViewById(R.id.screen_settings)
        switchNightMode()

        initListViewOfSettings(view)


    }

    private fun initListViewOfSettings(view: View){
        var array: ArrayList<String> = ArrayList()
        //arrayOf("PV Stations Characteristics", "Check Updates","Pro Version","About Us")
        array.add("Wind Turbine Characteristics")
        array.add("Check Updates")
        array.add("Our Discord Channel")
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
                    val intent = Intent(requireActivity(), AddSolarStationActivity::class.java)
                    startActivity(intent)
                }
                1 -> goToUrl("https://play.google.com/store/apps/dev?id=8066230504463138782&hl=en_US&gl=US")

                2 -> goToUrl("https://discord.gg/GYNFsdPw")
                3 -> showDialogAboutUs()

            }

        }
    }

    private fun goToUrl(url: String) {
        val uriUrl: Uri = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }

    private fun showDialogAboutUs(){

        var version = ""
        try {
            val pInfo = requireActivity().packageManager?.getPackageInfo(requireActivity().packageName, 0)
            version = pInfo?.versionName.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        val alertDialog: AlertDialog = AlertDialog.Builder(activity).create()
        alertDialog.setTitle("My Wind Turbine Pro \n(v. "+version+")")
        alertDialog.setMessage("First mobile app for forecasting of Wind Turbines\n" +
                "Features:\n" +
                "> forecasting of wind turbines generation on Hour, Week, Month\n" +
                "> detailed charts of forecast for 5 days\n" +
                "> the ability to calibrate the forecast\n" +
                "\n" +
                "Future features: \n" +
                "> Coming soon we will add wind roses for defines periods ")
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
