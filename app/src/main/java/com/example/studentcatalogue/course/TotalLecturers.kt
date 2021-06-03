package com.example.studentcatalogue.course

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.studentcatalogue.R

class TotalLecturers : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var heading: TextView

    private lateinit var totalLecturersAdapter: TotalLecturersAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total_lecturers)

        heading = findViewById(R.id.v_heading)

        heading.text = "Accumulated Attendance"

        recyclerView = findViewById(R.id.v_recycleView)

        totalLecturersAdapter = TotalLecturersAdapter(applicationContext)

        recyclerView.adapter = totalLecturersAdapter
    }
}