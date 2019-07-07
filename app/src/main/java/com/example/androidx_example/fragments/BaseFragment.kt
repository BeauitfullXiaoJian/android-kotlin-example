package com.example.androidx_example.fragments

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.androidx_example.MainActivity
import com.example.androidx_example.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_tool_bar.*

open class BaseFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()
    private var _actionBar: ActionBar? = null
    private var _toolbarView: Toolbar? = null

    fun addDisposableToCompositeDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun setNavToolBar(toolbar: Toolbar, title: String? = null) {
        _toolbarView = toolbar
        val appCompatActivity = (activity as AppCompatActivity)
        appCompatActivity.setSupportActionBar(_toolbarView)
        _actionBar = appCompatActivity.supportActionBar
        _toolbarView?.setupWithNavController(findNavController())
        title?.also { _toolbarView?.title = it }
    }

    fun setMainDrawerMenuIcon() {
        val parentActivity = activity as MainActivity
        val toggle = ActionBarDrawerToggle(
            parentActivity,
            parentActivity.main_drawer,
            parentActivity.app_toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        parentActivity.app_toolbar.setupWithNavController(nav_host.findNavController())
        parentActivity.main_drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    fun <T : ViewModel> createViewModel(modelClass: Class<T>): T {
        return com.example.androidx_example.until.createViewModel(modelClass)
    }

    fun <T : ViewModel> activityViewModel(modelClass: Class<T>): T {
        return com.example.androidx_example.until.createViewModel(
            activity = this.activity!!,
            modelClass = modelClass
        )
    }

    fun <T : ViewModel> fragmentViewModel(modelClass: Class<T>, fragment: Fragment = this): T {
        return com.example.androidx_example.until.createViewModel(fragment, modelClass)
    }

    fun showToast(message: String) {
        com.example.androidx_example.until.showToast(message, activity as Activity)
    }

    fun debugLog(vararg messages: String) {
        Log.d(this.javaClass.name, messages.joinToString(","))
    }

    open fun onBackPressed() = true

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}