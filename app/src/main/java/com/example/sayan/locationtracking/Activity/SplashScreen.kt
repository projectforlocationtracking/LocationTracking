package com.example.sayan.locationtracking.Activity

import android.os.Build
import android.view.View

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.util.Log
import android.widget.Button
import com.example.sayan.locationtracking.GPSTracker

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.sayan.locationtracking.ConncetToNet
import com.google.firebase.auth.FirebaseAuth
import com.smarteist.autoimageslider.SliderLayout
import com.smarteist.autoimageslider.SliderView

import java.util.*
import android.content.DialogInterface

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AlertDialog
import android.widget.TextView
import com.example.sayan.locationtracking.R
import com.example.sayan.locationtracking.SharedPref
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_splash_screen.*


class SplashScreen : AppCompatActivity(), View.OnClickListener
{
    private lateinit var googleSignIn:SignInButton
    private lateinit var signUpBtn: Button
    private lateinit var logInBtn:Button
    private lateinit var sliderLayout: SliderLayout
    private lateinit var connect:ConncetToNet
    private lateinit var gso:GoogleSignInOptions
    private lateinit var client:GoogleSignInClient
    private lateinit var textView:TextView


    companion object
    {
        val TAG = "Tag For Google Sign In"
        val TAG1 = "TAG1"
        val TAG2 = "TAG2"
        val RC_SIGN_IN = 1
        //val MY_PREF = "My Pref"
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        connect = ConncetToNet(this)

        //sharedPref = getSharedPreferences(MY_PREF,0)

        var user = FirebaseAuth.getInstance().currentUser

        if (user!=null)
        {

           Log.v(TAG1,"User logged in")
        }
        else
        {
            // No user is signed in.
            Log.v(TAG2,"User not Logged in")
        }

        googleSignIn = findViewById(R.id.sign_in_button)
        signUpBtn = findViewById(R.id.signUpBtn)
        logInBtn=findViewById(R.id.logInBtn)
        sliderLayout = findViewById(R.id.imgView_logo)
        textView = findViewById(R.id.textView)
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.FILL)

        sliderLayout.scrollTimeInSec = 1

        signUpBtn.setVisibility(INVISIBLE)
        logInBtn.setVisibility(INVISIBLE)
        googleSignIn.visibility = INVISIBLE
        textView.visibility = INVISIBLE

        signUpBtn.setOnClickListener(this)
        logInBtn.setOnClickListener(this)

        googleSignIn.setOnClickListener {

            val intent = client.signInIntent
            startActivityForResult(intent,RC_SIGN_IN)
        }



        if(!connect.isConnected())
        {
            showAlertDialog(this);

        }


        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()


        client = GoogleSignIn.getClient(this, gso);

        reqPermission()

        startService(Intent(baseContext, GPSTracker::class.java))

        CheckUser()

        val hide =
                AnimationUtils.loadAnimation(this, R.anim.bottom_to_top)

        val t = Timer(false)
        t.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {

                    signUpBtn.setVisibility(VISIBLE)
                    logInBtn.setVisibility(VISIBLE)
                    textView.visibility= VISIBLE
                    googleSignIn.setVisibility(VISIBLE)

                    signUpBtn.startAnimation(hide)
                    logInBtn.startAnimation(hide)
                    googleSignIn.startAnimation(hide)
                    textView.startAnimation(hide)

                }
            }
        }, 2000)


        /**
         *  For starting a thread  to run in background
         */

        /**val background = object : Thread() {
            override fun run() {
                try {
                    // Thread will sleep for 5 seconds
                    Thread.sleep((3 * 1000).toLong())

                    // After 3  seconds buttons appear

                    //Remove activity
                    finish()
                } catch (e: Exception) {
                }
            }
        }
            // start thread
        background.start()

**/
        setSliderViews()
    }

    override fun onStart()
    {
        super.onStart()
        val account:GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)

        if(account!=null)
        {
            val intent = Intent(this,MapNewActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    fun CheckUser()
    {
        /*val check:Boolean = SharedPref.readSharedSetting(this,"MyPref","true").toBoolean()

        if(check)
        {
            val intent = Intent(this,MapNewActivity::class.java)
            startActivity(intent)
            //finish()
        }*/

        val value = getSharedPreferences("MyPref",Context.MODE_PRIVATE).getBoolean("UserExists",false)
        if(value)
        {
            val intent = Intent(this,MapNewActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    fun reqPermission()
    {
        val permission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.SEND_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission, 1001)
        }
    }

    private fun setSliderViews() {

        for (i in 0..2) {

            val sliderView = SliderView(this)

            when (i) {
                0 ->
                    sliderView.setImageDrawable(R.drawable.images1)
                1 ->
                    sliderView.setImageDrawable(R.drawable.download1)
                2 ->
                    sliderView.setImageDrawable(R.drawable.download2)
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            //sliderView.setDescription("setDescription " + (i + 1))
            //final int finalI=i;

            sliderView.setOnSliderClickListener {
                //Toast.makeText(ImageSlider.this,"This is a slider",Toast.LENGTH_SHORT).show();
            }

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        when (requestCode) {
            1001 -> for (grper in grantResults) {
               // grper = PackageManager.PERMISSION_GRANTED
            }

            else -> {
            }
        }
    }

    /**
     * on Click methods
     */

    override fun onClick(p0: View)
    {
        if (p0.id == com.example.sayan.locationtracking.R.id.signUpBtn)
        {
            val intent:Intent = Intent(this,AuthenticateActivity::class.java)
            intent.putExtra("EXTRA",getString(com.example.sayan.locationtracking.R.string.signUp))
            startActivity(intent)
        }
        else if(p0.id== com.example.sayan.locationtracking.R.id.logInBtn)
        {

            val intent:Intent = Intent(this,AuthenticateActivity::class.java)
            intent.putExtra("EXTRA",getString(com.example.sayan.locationtracking.R.string.login))
            startActivity(intent)
        }
       /* else if(p0.id == R.id.sign_in_button )
        {
            val intent = client.signInIntent
            startActivityForResult(intent,RC_SIGN_IN)
        }*/

    }


    fun showAlertDialog(c: Context) {

        val builder = AlertDialog.Builder(c)
        builder.setTitle("No Internet Connection")
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit")

        builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which -> })

        val dialog = builder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            if (connect.isConnected()) {
                //dialog.dismiss()
                backGroundTask(dialog)
            } else
                finish()
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        Log.v("request code returned", requestCode.toString())

        if(requestCode == RC_SIGN_IN)
        {
            val completedTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try
            {
                val account = completedTask.getResult(ApiException::class.java)
                startActivity(Intent(this,MapNewActivity::class.java))
                finish()

            }
            catch (e:ApiException)
            {
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }


    fun backGroundTask(dialog: AlertDialog) {
        val background = object : Thread() {
            override fun run() {
                try {
                    // Thread will sleep for 5 seconds
                    Thread.sleep((2 * 1000).toLong())

                    // After 5 seconds redirect to another intent

                    dialog.dismiss()
                    //Remove activity
                    //finish()
                } catch (e: Exception) {
                }

            }

        }
        // start thread
        background.start()
    }

    override fun onStop() {
        super.onStop()

        stopService(Intent(baseContext, GPSTracker::class.java))

    }
}