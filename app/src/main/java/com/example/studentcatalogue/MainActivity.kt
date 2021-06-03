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
import com.example.studentcatalogue.course.CourseEnroll
import com.example.studentcatalogue.course.TotalLecturers
import com.example.studentcatalogue.signin.LoginType
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.parse.ParseUser
import us.zoom.sdk.*

class MainActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    private lateinit var studentName:TextView

    val user: ParseUser = ParseUser.getCurrentUser()
    val username= user.getString("accountName")
    private val level=user.get("level").toString()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        studentName=findViewById(R.id.v_stud_dash_name)

        studentName.text = username

        initializeSdk(this)




    }

    private fun createJoinMeetingDialog() {
        AlertDialog.Builder(this)
                .setView(R.layout.zoomjoin)
                .setPositiveButton("Join") { dialog, _ ->
                    dialog as AlertDialog
                    val numberInput = dialog.findViewById<TextInputEditText>(R.id.meeting_no_input)
                    val passwordInput = dialog.findViewById<TextInputEditText>(R.id.password_input)
                    val meetingNumber = numberInput?.text?.toString()
                    val password = passwordInput?.text?.toString()
                    meetingNumber?.takeIf { it.isNotEmpty() }?.let { meetingNo ->
                        password?.let { pw ->
                            joinMeeting(this@MainActivity, meetingNo, pw)
                        }
                    }
                    meetingNumber?.takeIf {
                        it.isEmpty() || password!!.isEmpty()
                    }.let {
                        Toast.makeText(this,"Please enter valid meeting number or password",Toast.LENGTH_LONG).show()
                    }
                }

                .show()
    }

    private fun joinMeeting(context: Context, meetingNumber: String, pw: String) {
        val meetingService = ZoomSDK.getInstance().meetingService
        val options = JoinMeetingOptions()
        val params = JoinMeetingParams().apply {
            val currentUser=ParseUser.getCurrentUser()
            val name=currentUser.get("accountName").toString()
            displayName =name

            meetingNo = meetingNumber
            password = pw
        }
        meetingService.joinMeetingWithParams(context, params, options)
    }

    private fun initializeSdk(context: Context) {
        val sdk = ZoomSDK.getInstance()



        val params = ZoomSDKInitParams().apply {
            appKey = context.getString(R.string.appKey) //
            appSecret = context.getString(R.string.appSecret) //
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



    fun onClick(v:MenuItem){
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
                        //  val course=ParseUser.getCurrentUser().getString("courseName").toString()
                        ParseUser.logOut()
                        val intent = Intent(applicationContext, LoginType::class.java)
                        startActivity(intent)
//                        if (course == "null"){
//
                        // ParseUser.logOut()
//                            val intent= Intent(applicationContext,StudentId::class.java)
//                            startActivity(intent)
//                        }else{
//
//                            ParseUser.logOut()
//
//                            val intent= Intent(applicationContext,LecturerId::class.java)
//                            startActivity(intent)
//                        }


                    }
                    .show()
        }
    }

    fun dashBoardItemClick(view: View) {

        when(view.id){
            R.id.v_stud_enroll->

                enroll()
            R.id.v_stud_online->
                    online()
            R.id.v_stud_time_table->
                timetable()
            R.id.v_stud_totalAttended->
                    totalLectures()
        }

    }

    private fun totalLectures() {
        val intent = Intent(applicationContext, TotalLecturers::class.java)
        startActivity(intent)
    }

    private fun timetable() {
        val intent = Intent(applicationContext, PdfViewActivity::class.java)
        startActivity(intent)
    }

    private fun online() {
        createJoinMeetingDialog()

    }

    private fun enroll() {
        val intent =Intent(this,CourseEnroll::class.java)
        intent.putExtra("level",level)
        startActivity(intent)

    }
}