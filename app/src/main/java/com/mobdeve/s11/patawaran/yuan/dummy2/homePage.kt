package com.mobdeve.s11.patawaran.yuan.dummy2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class homePage : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: DatabaseHelper

    // Add a function to refresh posts in the fragment
    fun refreshPosts() {
        // Refresh posts by fetching data from the database again
        val posts = dbHelper.getAllPosts().map { PostDB(it.caption, it.location, null, emptyList()) }
        (recyclerView.adapter as? HomePageAdapter)?.updatePosts(posts)
    }

    // Modify onCreateView to call refreshPosts() to fetch posts from the database
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize DatabaseHelper
        dbHelper = DatabaseHelper(requireContext())

        // Refresh posts
        refreshPosts()

        return view
    }
}
