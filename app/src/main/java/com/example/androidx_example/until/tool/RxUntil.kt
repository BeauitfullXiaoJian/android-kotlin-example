package com.example.androidx_example.until.tool

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object RxUntil {
    fun mainTask(runnable: Runnable, completeDo: () -> Unit): Disposable {
        return Completable
            .fromRunnable(runnable)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe { completeDo() }
    }
}