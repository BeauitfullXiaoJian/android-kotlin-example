package com.example.androidx_example.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.androidx_example.R
import kotlinx.android.synthetic.main.fragment_user_center.*

/**
 * 用户中心
 *
 */
class UserCenterFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_center, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_camera.setOnClickListener {
            val action = UserCenterFragmentDirections.actionUserCenterFragmentToCameraFragment()
            findNavController().navigate(action)
        }
        btn_banner.setOnClickListener {
            val action = UserCenterFragmentDirections.actionUserCenterFragmentToLargeImageFragment()
            findNavController().navigate(action)
        }
    }

}
