package com.gabar.kotlinwithfirebase.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.gabar.kotlinwithfirebase.R
import com.gabar.kotlinwithfirebase.utilities.Constants
import com.gabar.kotlinwithfirebase.utilities.SharedPrefUtil
import com.gabar.kotlinwithfirebase.utilities.Utils

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({

            if(Utils.isInternetOn(this)){
                val pref=SharedPrefUtil(this)
                var emailId=pref.getString(Constants.APP_USER)
                val user_id=pref.getString("user_id")

                if (!emailId.isNullOrEmpty()||!user_id.isNullOrEmpty()) {

                    val mainIntent = Intent(this, BaseActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                }else{
                    val mainIntent = Intent(this, LoginActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                }
            }else{

                Utils.ShowNoIternetDialog(this,"No Internet Connection Available")

            }


        }, 2000)
    }
}
