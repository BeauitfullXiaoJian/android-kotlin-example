package com.example.androidx_example.fragments

import android.util.Log
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseFragment : Fragment() {
    private val compositeDisposable = CompositeDisposable()

    fun addDisposableToCompositeDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun showToast(message: String) {
        activity?.runOnUiThread {
            com.example.androidx_example.until.showToast(message, activity)
        }
    }

    fun debugLog(message: String) {
        Log.d(this.javaClass.name, message)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}