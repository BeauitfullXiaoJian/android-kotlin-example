package com.example.androidx_example.fragments.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidx_example.R
import com.example.androidx_example.fragments.BaseFragment
import com.example.androidx_example.until.reObserve
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    private var listAdapter: HomeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        btn_login.setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.action_home_to_login)
//        }
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
            .create(HomeViewModel::class.java)
        viewModel.getVideos().reObserve(this, Observer { videos ->
            listAdapter?.submitList(videos)
            debugLog("刷新列表数据")
        })
    }

    private fun initView() {
        listAdapter = HomeAdapter(activity as Activity)
        home_recycler.adapter = listAdapter
        home_recycler.layoutManager = GridLayoutManager(context, 2);
    }

}
