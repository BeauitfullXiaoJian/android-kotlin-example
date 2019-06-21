package com.example.androidx_example.fragments

import android.app.Activity
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseFragment : Fragment() {
    private val compositeDisposable = CompositeDisposable()

    fun addDisposableToCompositeDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun setToolBar(toolbar: Toolbar, titleView: TextView) {
        val appCompatActivity = (activity as AppCompatActivity)
        appCompatActivity.setSupportActionBar(toolbar)
        appCompatActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        titleView.text = findNavController().currentDestination?.label
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

    fun debugLog(message: String) {
        Log.d(this.javaClass.name, message)
    }

    open fun onBackPressed() = true

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}