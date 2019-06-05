package com.example.androidx_example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidx_example.until.api.HttpRequest

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 加载默认配置
         HttpRequest.loadConfig(this)

        // 校验登入状态

        // 跳转到主页
         startActivity(Intent(this, MainActivity::class.java));
         finish()
    }
}
