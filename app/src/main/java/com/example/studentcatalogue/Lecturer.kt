package com.example.studentcatalogue

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.studentcatalogue.lecturer.LectureSetup
import com.example.studentcatalogue.lecturer.TotalStudents
import com.example.studentcatalogue.signin.LoginType
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.parse.ParseUser
import us.zoom.sdk.*

class Lecturer : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    private lateinit var toolbar:androidx.appcompat.widget.Toolbar

    private lateinit var dashname: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecturer)

        toolbar=findViewById(R.id.v_lec_toolbar)

        dashname=findViewById(R.id.v_lec_dash_name)

        val user=ParseUser.getCurrentUser()

        val name=user.getString("accountName")

        dashname.text=name

        initializeSdk(this)




    }

    private fun createLoginDialog() {
        AlertDialog.Builder(this)
                .setView(R.layout.zoomlogin)
                .setPositiveButton("Log in") { dialog, _ ->
                    dialog as AlertDialog
                    val emailInput = dialog.findViewById<TextInputEditText>(R.id.email_input)
                    val passwordInput = dialog.findViewById<TextInputEditText>(R.id.pw_input)
                    val email = emailInput?.text?.toString()
                    val password = passwordInput?.text?.toString()
                    email?.takeIf { it.isNotEmpty() }?.let { emailAddress ->
                        password?.takeIf { it.isNotEmpty() }?.let { pw ->
                            login(emailAddress, pw)
                        }
                    }

                    dialog.dismiss()
                }
                .show()
    }
    // 2. Add login feature
    private fun login(username: String, password: String) {
        val result = ZoomSDK.getInstance().loginWithZoom(username, password)
        if (result == ZoomApiError.ZOOM_API_ERROR_SUCCESS) {

            //  listen for authentication result before starting a meeting            ZoomSDK.getInstance().addAuthenticationListener(authListener)
        }
    }

    // 3. Add Start Meeting feature
    private fun startMeeting(context: Context) {
        val zoomSdk = ZoomSDK.getInstance()
        if (zoomSdk.isLoggedIn) {
            val meetingService = zoomSdk.meetingService
            val options = StartMeetingOptions()
            meetingService.startInstantMeeting(context, options)
        }
    }

    private fun initializeSdk(context: Context) {
        val sdk = ZoomSDK.getInstance()



        val params = ZoomSDKInitParams().apply {
            appKey = context.getString(R.string.appKey) //
            appSecret = context.getString(R.string.appSecret)
            domain = "zoom.us"
            enableLog = true // Optional: enable logging for debugging
        }

        val listener = object : ZoomSDKInitializeListener {
            /**
             * If the [errorCode] is [ZoomError.ZOOM_ERROR_SUCCESS], the SDK was initialized and can
             * now be used to join/start a meeting.
             */
            override fun onZoomSDKInitializeResult(errorCode: Int, internalErrorCode: Int) = Unit
            override fun onZoomAuthIdentityExpired() = Unit
        }

        sdk.initialize(context, listener, params)
    }


    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            return
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_LONG).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }



    fun onClick(v: MenuItem){
        when(v.itemId)
        {
            R.id.v_logout->
                MaterialAlertDialogBuilder(this)
                    .setTitle("Log Out?")
                    .setMessage("Are you sure you want to log out")

                    .setNegativeButton(resources.getString(R.string.decline)) { dialog, _ ->
                        // Respond to negative button press
                        dialog.cancel()
                    }
                    .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                        // Respond to positive button press
                        //   val course=ParseUser.getCurrentUser().getString("courseName").toString()
                        ParseUser.logOut()
                        val intent = Intent(applicationContext, LoginType::class.java)
                        startActivity(intent)
//                        if (course == ""){
//
//                            ParseUser.logOut()
//                            val intent= Intent(applicationContext, StudentId::class.java)
//                            startActivity(intent)
//                        }else{
//
//
//                            ParseUser.logOut()
//                            val intent= Intent(applicationContext, LecturerId::class.java)
//                            startActivity(intent)
//                        }
                    }
                    .show()
        }
    }

    fun dashBoardClick(view: View) {

        when (view.id) {
            R.id.v_start_class ->
                startClass()
            R.id.v_stud_online ->
                online()
            R.id.v_stud_time_table ->
                timetable()
            R.id.v_stud_totalAttended ->
                totalLectures()
        }
    }

    private fun totalLectures() {
        val intent= Intent(applicationContext, TotalStudents::class.java)
        startActivity(intent)
    }

    private fun timetable() {
        val intent = Intent(applicationContext, PdfViewActivity::class.java)
        startActivity(intent)
    }

    private fun online() {
        //val user=ParseUser.getCurrentUser()
      //  val roomName=user.getString("accountName")


        if (ZoomSDK.getInstance().isLoggedIn) {
            startMeeting(this)
        } else {
            createLoginDialog()
        }




    }

    private fun startClass() {
        val intent = Intent(applicationContext, LectureSetup::class.java)
        startActivity(intent)
    }
}