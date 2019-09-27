package com.example.androidx_example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidx_example.until.sql.RoomUntil
import com.example.httprequest.HttpRequest
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class SplashActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 初始化HttpRequest
        val obsRequest = HttpRequest.prepareByConfigRawId(this, R.raw.http)

        // 初始化RoomDB
        val obsDB = RoomUntil.initDB()

        // 校验登入状态
        // val obsAuth = AuthCheck.checkLogin()

        // 显示广告
        // val obsAds = Banner.display()

        // 跳转到主页
        disposable = Completable.concatArray(
            obsRequest,
            obsDB
        ).subscribe {
            startActivity(Intent(this, MainActivity::class.java));
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
