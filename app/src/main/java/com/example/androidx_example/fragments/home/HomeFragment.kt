package com.example.androidx_example.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.view.menu.ListMenuItemView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.androidx_example.R
import kotlinx.android.synthetic.main.fragment_home.*

private const val TAG = "HomeFragment";

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        print(TAG + "创建成功");
        val viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
            .create(HomeViewModel::class.java)
        viewModel.getMenuRows().observe(this, Observer<List<MenuItem>> { menuRows ->
            for (menuItem in menuRows) {
                val imageView = ImageView(context);
                imageView.setImageDrawable(resources.getDrawable(menuItem.iconId, null));
                val layoutParams = GridLayout.LayoutParams()
                imageView.layoutParams = layoutParams
                menuContainer.addView(imageView)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_login.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_home_to_login)
        }
    }
}
