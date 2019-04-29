package com.example.sayan.locationtracking.Authentication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.sayan.locationtracking.Activity.AuthenticateActivity
import com.example.sayan.locationtracking.Activity.MapNewActivity
import com.example.sayan.locationtracking.Fragment.AuthenticationFragment.SignUpFragment
import com.example.sayan.locationtracking.InterFace.Authenticate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser

class EmailAuthenticate(val context: Activity) : Authenticate
{
    //lateinit var context: AuthenticateActivity
//    var context= AuthenticateActivity()

    val signUpFragment:SignUpFragment= SignUpFragment()

    private lateinit var auth: FirebaseAuth

    lateinit var email: String

    lateinit var password: String

    override fun authenticate(email:String,password:String)
    {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        createNewUser(email,password)

    }

     fun createNewUser(email: String,password: String)  // to authenticate new user
    {

        auth = FirebaseAuth.getInstance()

    //    val currentUser = auth.currentUser

        Log.e("TAG2","In create new user method");

            auth.createUserWithEmailAndPassword(email, password)

                    .addOnCompleteListener(context) { task ->
                        if (task.isSuccessful)
                        {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success")


                          /*    if (currentUser != null) {
                                updateUI(currentUser)  // Sign in happened successfully
                            } */
                            Toast.makeText(context,"Authentication successful",Toast.LENGTH_LONG).show()
                            Log.e("TAG1","Authentication Successful")

                            val user = auth.currentUser
                            if (user != null) {
                                updateUI(user)
                            }

                        }
                        else
                        {
                            val authException:FirebaseAuthException = task.exception as FirebaseAuthException
                            Toast.makeText(context,authException.message,Toast.LENGTH_LONG).show()
                            Log.e("TAG2","Authentication Failed for :" + authException)

                        }
                    }
    }

    fun signInUser(email:String,password: String)     // to sign In existing user
    {
        auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(context){task->
                    if(task.isSuccessful)
                    {
                        val user=auth.currentUser

                        updateUI(user!!)  // Sign in happened successfully
                    }
                    else
                    {
                        Toast.makeText(context,"Check Password Or Email Again",Toast.LENGTH_LONG).show()

                        Log.e("TAG","Error in authentication")
                    }
                }
    }

    private fun updateUI(user: FirebaseUser)
    {
        val intent= Intent(context, MapNewActivity::class.java)
        intent.putExtra("UserName",user.toString())
        context.startActivity(intent)
    }
}