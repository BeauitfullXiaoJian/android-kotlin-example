package com.example.androidx_example

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.androidx_example.fragments.BaseFragment
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
        val activeFragment = nav_host.childFragmentManager.primaryNavigationFragment as BaseFragment
        if (activeFragment.onBackPressed()) {
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
                if (!canBack) {
                    navigation_side.setCheckedItem(destination.id)
                    main_bottom_navigation.visibility = View.VISIBLE
                    main_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                } else {
                    main_bottom_navigation.visibility = View.GONE
                    main_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            }
        }
        main_bottom_navigation.setupWithNavController(navCtrl)
        navigation_side.apply {
            setupWithNavController(navCtrl)
            itemIconTintList = null
        }
    }

//    private fun intActionBar() {
//        setSupportActionBar(main_toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//        val toggle = ActionBarDrawerToggle(
//            this, main_drawer, main_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
//        )
//        main_drawer.addDrawerListener(toggle)
//        toggle.syncState()
//    }
}
