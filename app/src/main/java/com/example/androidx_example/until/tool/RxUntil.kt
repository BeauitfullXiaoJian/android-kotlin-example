package com.example.androidx_example.until.tool

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.InputStream
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

object RxUntil {
    fun mainTask(runnable: Runnable, completeDo: () -> Unit): Disposable {
        return Completable
            .fromRunnable(runnable)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe { completeDo() }
    }

    fun mainDelayDo(delayMilliTime: Long, delayDo: () -> Unit): Disposable {
        return Observable.timer(delayMilliTime, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                delayDo()
            }
    }

    fun <T> mainObs(getData: () -> T?): Single<T?> {
        return Single.fromCallable<T> { getData() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
    }
}