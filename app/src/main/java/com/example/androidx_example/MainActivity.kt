package com.example.androidx_example

//import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.androidx_example.fragments.BaseFragment
//import com.example.androidx_example.services.ChatService
import com.example.androidx_example.until.tool.TimeLock
import com.example.androidx_example.until.tool.showToast
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
//        initChatService()
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
                    R.id.playerFragment,
                    R.id.cameraXFragment
                )

                // 设置toolbar显示/隐藏
                if (showMainToolBar) {
                    app_toolbar.visibility = View.VISIBLE
                    setUpActionBar(app_toolbar, this)
                } else app_toolbar.visibility = View.GONE

                // 设置底部导航显示/隐藏
                if (!canBack) {
                    navigation_side.setCheckedItem(destination.id)
                    main_bottom_navigation.visibility = View.VISIBLE
                    main_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

                } else {
                    main_bottom_navigation.visibility = View.GONE
                    main_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
        }
        main_bottom_navigation.setupWithNavController(navCtrl)
        navigation_side.apply {
            setupWithNavController(navCtrl)
            itemIconTintList = null
        }
        app_toolbar.setupWithNavController(navCtrl)
    }

//    private fun initChatService() {
//        startService(Intent(this, ChatService::class.java))
//    }
}
