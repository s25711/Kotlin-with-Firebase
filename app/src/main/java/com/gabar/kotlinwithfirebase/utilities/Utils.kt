package com.gabar.kotlinwithfirebase.utilities

import android.app.Activity
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gabar.kotlinwithfirebase.R
import com.kaopiz.kprogresshud.KProgressHUD
import java.util.ArrayList

/**
 * Created by brst-pc81 on 1/9/18.
 */
class Utils {
    companion object {
       lateinit var loader:KProgressHUD
        fun centerToolbarTitle(toolbar: Toolbar) {
            val title = toolbar.title
            val outViews = ArrayList<View>(1)
            toolbar.findViewsWithText(outViews, title, View.FIND_VIEWS_WITH_TEXT)
            if (!outViews.isEmpty()) {
                val titleView = outViews[0] as TextView
                titleView.gravity = Gravity.CENTER
                val layoutParams = titleView.layoutParams as Toolbar.LayoutParams
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                toolbar.requestLayout()

            }
        }



        fun showProgress(context: Context){
           loader = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                   // .setLabel("Please wait")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show()
        }

        fun hideProgress(context: Context){
            loader.dismiss()
        }

        fun isInternetOn(context: Context): Boolean {
            val connec = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

            if (connec.getNetworkInfo(0).state == android.net.NetworkInfo.State.CONNECTED ||
                    connec.getNetworkInfo(0).state == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).state == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).state == android.net.NetworkInfo.State.CONNECTED) {
                return true

            } else if (connec.getNetworkInfo(0).state == android.net.NetworkInfo.State.DISCONNECTED || connec.getNetworkInfo(1).state == android.net.NetworkInfo.State.DISCONNECTED) {
                return false
            }
            return false
        }

        fun ShowNoIternetDialog(mContext: Activity, message: String) {
            try {
                Log.i("message===", message)
                val builder = AlertDialog.Builder(mContext, R.style.CustomPopUpThemeBlue)
                builder.setTitle("Network status")
                builder.setMessage(message)
                builder.setPositiveButton("OK") { dialog, which -> System.exit(0) }
                builder.setCancelable(false)
                builder.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }



    }




}