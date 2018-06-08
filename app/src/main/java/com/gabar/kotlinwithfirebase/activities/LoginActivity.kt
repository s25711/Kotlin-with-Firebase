package com.gabar.kotlinwithfirebase.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.toolbar.*
import android.widget.Toast
import com.gabar.kotlinwithfirebase.R
import com.gabar.kotlinwithfirebase.utilities.Constants
import com.gabar.kotlinwithfirebase.utilities.SharedPrefUtil
import com.gabar.kotlinwithfirebase.utilities.Utils
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnLogin -> {
                mEmaiId = edtEmail.text.toString()
                mPassword = edtPassword.text.toString()
                if (mEmaiId.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Please enter email", Toast.LENGTH_SHORT).show()
                } else if (mPassword.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Please enter password", Toast.LENGTH_SHORT).show()
                } else if (mPassword.length < 6) {
                    Toast.makeText(this@LoginActivity, "Password must be greater than 6 alphabets", Toast.LENGTH_LONG).show()
                } else {
                    firebaseLogin(mEmaiId, mPassword)
                }
            }
            R.id.btnRegisterlink -> {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            R.id.btnForgotPassword -> {
                startActivity(Intent(this@LoginActivity, ForgotPassword::class.java))
            }

            R.id.imgFacebookLogin -> {
                facebookLogin(this,callbackManager)
            }
            R.id.imgTwitterLogin -> {
                Toast.makeText(this@LoginActivity, "Twitter", Toast.LENGTH_SHORT).show()
            }
            R.id.imgGoogleLogin -> {
                Toast.makeText(this@LoginActivity, "Google", Toast.LENGTH_SHORT).show()
            }

        }
    }

    lateinit var imgFacebookLogin: ImageView
    lateinit var imgTwitterLogin: ImageView
    lateinit var imgGoogleLogin: ImageView
    lateinit var btnRegisterlink: Button
    lateinit var btnForgotPassword: Button
    lateinit var edtEmail: TextInputEditText
    lateinit var edtPassword: TextInputEditText
    lateinit var btnLogin: Button
    lateinit var mtoolbar: Toolbar
    lateinit var auth: FirebaseAuth
    lateinit var mEmaiId: String
    lateinit var mPassword: String
    lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login)
        initViews()
        addActionListner()
        callbackManager = CallbackManager.Factory.create();

        auth = FirebaseAuth.getInstance()

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun initViews() {
        mtoolbar = findViewById(R.id.toolbar)
        toolbar.setTitle("Login")
        setSupportActionBar(toolbar)
        //Utils.centerToolbarTitle(toolbar)
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegisterlink = findViewById(R.id.btnRegisterlink)
        btnForgotPassword = findViewById(R.id.btnForgotPassword)
        imgFacebookLogin = findViewById(R.id.imgFacebookLogin)
        imgGoogleLogin = findViewById(R.id.imgGoogleLogin)
        imgTwitterLogin = findViewById(R.id.imgTwitterLogin)
    }

    private fun addActionListner() {
        btnLogin.setOnClickListener(this)
        btnRegisterlink.setOnClickListener(this)
        btnForgotPassword.setOnClickListener(this)
        imgTwitterLogin.setOnClickListener(this)
        imgGoogleLogin.setOnClickListener(this)
        imgFacebookLogin.setOnClickListener(this)
    }

    private fun firebaseLogin(email: String, password: String) {

        Utils.showProgress(this@LoginActivity)
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this@LoginActivity) { task ->

                    if (!task.isSuccessful) {
                        Utils.hideProgress(this@LoginActivity)

                        try {
                            throw task.getException()!!
                        }  catch (e: FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this@LoginActivity, "" + e.message, Toast.LENGTH_LONG).show()
                        } catch (e: FirebaseAuthUserCollisionException) {
                            Toast.makeText(this@LoginActivity, "" + e.message, Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@LoginActivity, "" + e.message, Toast.LENGTH_LONG).show()
                        }

                    } else {

                        Utils.hideProgress(this@LoginActivity)
                        val intent = Intent(this@LoginActivity, BaseActivity::class.java)
                        startActivity(intent)
                        val pref = SharedPrefUtil(this)
                        pref.saveString(Constants.APP_USER, email)
                        finish()
                    }
                }

    }

    private fun printKeyHash() {
        try {
            val info = packageManager.getPackageInfo(
                    "com.gabar.kotlinwithfirebase",
                    PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }

    }
    fun facebookLogin(activity: Activity, callbackManager: CallbackManager) {
        if (AccessToken.getCurrentAccessToken() == null || AccessToken.getCurrentAccessToken().isExpired) {
            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "user_friends", "email"))
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    val request = GraphRequest.newMeRequest(
                            loginResult.accessToken
                    ) { user, response ->
                        try {
                            Log.d("TAG", "LoginResponse:" + response)
                            val userid = user.getString("id")
                            val first_name = user.getString("first_name")
                            val last_name = user.getString("last_name")

                            val pref=SharedPrefUtil(this@LoginActivity)
                            pref.saveString("user_id",userid)

                            startActivity(Intent(this@LoginActivity,BaseActivity::class.java))
                            //event_bus.postSticky(first_name+""+last_name)
                            finish()
                        } catch (x: Exception) {
                            x.cause
                        }
                    }
                    val parameters = Bundle()
                    parameters.putString("fields", "id,first_name,last_name,email,gender,birthday,picture")
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {

                }

                override fun onError(exception: FacebookException) {
                    if (exception is FacebookAuthorizationException) {
                        if (AccessToken.getCurrentAccessToken() != null) {
                            LoginManager.getInstance().logOut()


                        }
                    }
                }
            })
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


}
