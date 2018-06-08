package com.gabar.kotlinwithfirebase.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.gabar.kotlinwithfirebase.R
import com.gabar.kotlinwithfirebase.utilities.Utils
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast


class ForgotPassword : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
      when(p0?.id){
          R.id.btnReset->{
              if(edtEmailId.text.isEmpty()){
                  Toast.makeText(this@ForgotPassword,"Please enter Registered Email Id.",Toast.LENGTH_SHORT).show()
              }else {

                  sendPasswordToEmail(edtEmailId.text.toString())

              }
          }
          R.id.textViewBack->{
              onBackPressed()
          }
      }
    }

    lateinit var edtEmailId: TextInputEditText
    lateinit var btnReset:Button
    lateinit var textViewBack:TextView
    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        initViews()
        addActionListner()

        auth= FirebaseAuth.getInstance()

    }


    private fun initViews() {
        edtEmailId = findViewById(R.id.edtEmailId)
        btnReset = findViewById(R.id.btnReset)
        textViewBack=findViewById(R.id.textViewBack)

    }

    private fun addActionListner(){
        btnReset.setOnClickListener(this)
        textViewBack.setOnClickListener(this)

    }

    private fun sendPasswordToEmail(emailId:String){
        Utils.showProgress(this@ForgotPassword)
        auth.sendPasswordResetEmail(emailId)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(this@ForgotPassword, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    } else {
                        Toast.makeText(this@ForgotPassword, "Failed to send reset email!", Toast.LENGTH_SHORT).show()
                    }
                    Utils.hideProgress(this@ForgotPassword)

                }

    }
}
