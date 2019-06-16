package com.example.androidx_example.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.androidx_example.R
import com.example.androidx_example.until.api.HttpRequest
import kotlinx.android.synthetic.main.fragment_public.*


/**
 * 公开广场
 *
 */
class PublicFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_public, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_http.setOnClickListener {
            HttpRequest.get("good").subscribe()
        }
    }


}
