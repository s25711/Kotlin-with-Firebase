package com.gabar.kotlinwithfirebase.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.gabar.kotlinwithfirebase.R
import com.gabar.kotlinwithfirebase.utilities.Utils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.toolbar.*
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.HashMap


class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
       when(p0?.id){
           R.id.btnRegister->{

               if (edtUsername.text.isEmpty()){
                   Toast.makeText(this@RegisterActivity,"All the fields must be filled",Toast.LENGTH_SHORT).show()
               }else if (edtEmail.text.isEmpty()){
                   Toast.makeText(this@RegisterActivity,"All the fields must be filled",Toast.LENGTH_SHORT).show()
               }else if(edtPassword.text.isEmpty()){
                   Toast.makeText(this@RegisterActivity,"All the fields must be filled",Toast.LENGTH_SHORT).show()
               }else if (edtConfirmPassword.text.isEmpty()){
                   Toast.makeText(this@RegisterActivity,"All the fields must be filled",Toast.LENGTH_SHORT).show()
               }else {
                   registerToFirebase(edtEmail.text.toString() , edtPassword.text.toString())
               }

           }
           R.id.btnLoginlink->{
              onBackPressed()
           }
           R.id.textViewBack->{
               onBackPressed()
           }
       }
    }

    lateinit var textViewBack:TextView
    lateinit var mtoolbar: Toolbar
    lateinit var btnLoginlink:Button
    lateinit var btnRegister:Button
    lateinit var edtPassword: TextInputEditText
    lateinit var edtUsername:TextInputEditText
    lateinit var edtEmail:TextInputEditText
    lateinit var edtConfirmPassword:TextInputEditText
    lateinit var database: DatabaseReference

    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initViews()
        addActionListner()

        auth= FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
    }

    private fun initViews(){
        mtoolbar=findViewById(R.id.toolbar)
        toolbar.setTitle("Register")
        setSupportActionBar(toolbar)
       // Utils.centerToolbarTitle(toolbar)
        btnRegister=findViewById(R.id.btnRegister)
        btnLoginlink=findViewById(R.id.btnLoginlink)
        edtEmail=findViewById(R.id.edtEmail)
        edtPassword=findViewById(R.id.edtPassword)
        edtUsername=findViewById(R.id.edtUsername)
        edtConfirmPassword=findViewById(R.id.edtConfirmPassword)
        textViewBack=findViewById(R.id.textViewBack)
    }
    private fun addActionListner(){
        btnLoginlink.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
        textViewBack.setOnClickListener(this)
    }
    private fun registerToFirebase(email:String,password:String){
        Utils.showProgress(this@RegisterActivity)
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this@RegisterActivity, OnCompleteListener<AuthResult> { task ->
                    Utils.hideProgress(this@RegisterActivity)
                    if (!task.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, ""+task.result + task.exception!!,
                                Toast.LENGTH_SHORT).show()
                    } else {
                        val usersData = HashMap<String, String>()
                        usersData.put("Username", edtUsername.text.toString())
                        usersData.put("Email_Id", edtEmail.text.toString())
                        usersData.put("UId", task.result.user.uid)

                        database.child("Users").push().setValue(usersData).addOnCompleteListener(OnCompleteListener<Void> { task ->
                            if (!task.isSuccessful) {
                                try {
                                    throw task.getException()!!
                                }catch (e:Exception){
                                    Toast.makeText(this@RegisterActivity,""+e.message,Toast.LENGTH_SHORT).show()
                                }

                            }
                        })
                       onBackPressed()
                    }
                })
    }

}
