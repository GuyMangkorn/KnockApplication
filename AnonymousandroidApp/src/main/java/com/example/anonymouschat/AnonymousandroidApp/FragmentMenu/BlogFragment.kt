package com.example.anonymouschat.AnonymousandroidApp.FragmentMenu

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anonymouschat.AnonymousandroidApp.BlogDirectory.BlogAdapter
import com.example.anonymouschat.AnonymousandroidApp.Model.BlogRepository
import com.example.anonymouschat.AnonymousandroidApp.R
import com.example.anonymouschat.AnonymousandroidApp.ViewModel.BlogViewModel
import com.example.anonymouschat.AnonymousandroidApp.WriteBlogActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

class BlogFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter:RecyclerView.Adapter<*>
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var writePost:FloatingActionButton
    private val repository:BlogRepository = BlogRepository()
    private lateinit var viewModel:BlogViewModel
    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_blog,container,false)
        val locationPreferences = requireActivity().getSharedPreferences("Location", Context.MODE_PRIVATE)
        val state:String = locationPreferences.getString("state","Unknown")!!
        viewModel = getViewModel("สมุทรปราการ")
        recyclerView = view.findViewById(R.id.recyclerBlog)
        layoutManager = LinearLayoutManager(requireContext())
        viewModel.fetchAllBlog.observe(requireActivity(), {
            recyclerView.layoutManager = layoutManager
            adapter = BlogAdapter(requireContext(), it)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()

        })

        writePost = view.findViewById(R.id.floatingActionButtonWrite)
        writePost.setOnClickListener{
            val intent = Intent(requireContext(),WriteBlogActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    private fun getViewModel(state:String): BlogViewModel {
        return ViewModelProviders.of(requireActivity(),object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return BlogViewModel(repository,state) as T
            }
        }).get(BlogViewModel::class.java)
    }
}