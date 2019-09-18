package com.example.androidx_example.until.tool


/**
 * 时间锁，在一定的时间间隔后才能解开
 */
class TimeLock(private val lockTimeMillis: Int) {

    private var saveTimeMillis = 0L

    val isLock: Boolean
        get() = if (saveTimeMillis > 0) {
            val currentTimeMillis = System.currentTimeMillis()
            if (currentTimeMillis - saveTimeMillis > lockTimeMillis) {
                saveTimeMillis = currentTimeMillis
                false
            } else true

        } else {
            saveTimeMillis = System.currentTimeMillis()
            false
        }

    val isInLockTime: Boolean
        get() = if (saveTimeMillis > 0) {
            val currentTimeMillis = System.currentTimeMillis()
            if (currentTimeMillis - saveTimeMillis > lockTimeMillis) {
                saveTimeMillis = currentTimeMillis
                false
            } else {
                saveTimeMillis = currentTimeMillis
                true
            }
        } else {
            saveTimeMillis = System.currentTimeMillis()
            false
        }

    companion object {

        private const val SHORT_TIME = 100
        private const val MID_TIME = 500
        private const val LONG_TIME = 1000

        fun getShortLock(): TimeLock {
            return TimeLock(SHORT_TIME)
        }

        fun getDefaultLock(): TimeLock {
            return TimeLock(MID_TIME)
        }

        fun getLongLock(): TimeLock {
            return TimeLock(LONG_TIME)
        }
    }
}