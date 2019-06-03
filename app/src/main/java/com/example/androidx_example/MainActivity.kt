package com.example.androidx_example

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNav()
    }

    private fun initNav() {
        val navCtrl = nav_host.findNavController().apply {
            addOnDestinationChangedListener { _, destination, _ ->
                navigation_side?.setCheckedItem(destination.id)
                main_bottom_navigation?.visibility = when (destination.id) {
                    R.id.homeFragment, R.id.webFragment, R.id.publicFragment, R.id.userCenterFragment -> View.VISIBLE
                    else -> View.GONE
                }
            }
        }
        main_bottom_navigation.setupWithNavController(navCtrl)
        navigation_side.apply {
            setupWithNavController(navCtrl)
            itemIconTintList = null
        }
    }

}
