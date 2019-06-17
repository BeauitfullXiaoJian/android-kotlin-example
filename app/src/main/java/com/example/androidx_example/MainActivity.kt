package com.example.androidx_example

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.androidx_example.until.showToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class MainActivity : BaseActivity() {

    private var canBack = false
    private var mExitClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNav()
    }

    override fun onBackPressed() {
        if (canBack) {
            super.onBackPressed()
        } else {
            if (System.currentTimeMillis() - mExitClickTime > 2000) {
                showToast("再按一次退出程序", this)
                mExitClickTime = System.currentTimeMillis()
            } else {
                exitProcess(0)
            }
        }
    }

    private fun initNav() {
        val navCtrl = nav_host.findNavController().apply {
            addOnDestinationChangedListener { _, destination, _ ->
                canBack = destination.id !in listOf(
                    R.id.homeFragment,
                    R.id.webFragment,
                    R.id.publicFragment,
                    R.id.userCenterFragment
                )
                main_bottom_navigation?.visibility = if (canBack) View.GONE else View.VISIBLE
                navigation_side?.setCheckedItem(destination.id)
            }
        }
        main_bottom_navigation.setupWithNavController(navCtrl)
        navigation_side.apply {
            setupWithNavController(navCtrl)
            itemIconTintList = null
        }
    }

}
