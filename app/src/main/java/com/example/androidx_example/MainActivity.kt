package com.example.androidx_example

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.androidx_example.fragments.BaseFragment
import com.example.androidx_example.until.TimeLock
import com.example.androidx_example.until.shareImage
import com.example.androidx_example.until.showToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_tool_bar.*
import kotlin.system.exitProcess

class MainActivity : BaseActivity() {

    private var canBack = false
    private var showMainToolBar = true
    private val mTimeLock = TimeLock(2000)

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
                if (mTimeLock.isInLockTime) {
                    exitProcess(0)
                } else {
                    showToast("再按一次退出程序", this)
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
                showMainToolBar = destination.id !in listOf(
                    R.id.webFragment,
                    R.id.publicFragment,
                    R.id.userCenterFragment,
                    R.id.playerFragment
                )
                if (!canBack) {
                    navigation_side.setCheckedItem(destination.id)
                    main_bottom_navigation.visibility = View.VISIBLE
                    main_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                } else {
                    main_bottom_navigation.visibility = View.GONE
                    main_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                // 设置toolbar显示/隐藏
                if (showMainToolBar) {
                    app_toolbar.visibility = View.VISIBLE
                    setUpActionBar(app_toolbar, nav_host.findNavController())
                } else app_toolbar.visibility = View.GONE
            }
        }
        main_bottom_navigation.setupWithNavController(navCtrl)
        navigation_side.apply {
            setupWithNavController(navCtrl)
            itemIconTintList = null
        }
    }

//    private fun initActionBar() {
//        setSupportActionBar(app_toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//        val toggle = ActionBarDrawerToggle(
//            this, main_drawer, app_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
//        )
//        app_toolbar.setupWithNavController(nav_host.findNavController())
//        main_drawer.addDrawerListener(toggle)
//        toggle.syncState()
//    }
}
