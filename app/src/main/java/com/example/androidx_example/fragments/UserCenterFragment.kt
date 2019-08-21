package com.example.androidx_example.fragments

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.work.WorkInfo
import com.example.androidx_example.GlobalSearchActivity
import com.example.androidx_example.R
import com.example.androidx_example.services.MusicService
import com.example.androidx_example.works.ImageDownloadWorker
import kotlinx.android.synthetic.main.fragment_user_center.*

/**
 * 用户中心
 */
class UserCenterFragment : BaseFragment() {

    private var mMusicConnection: MusicConnection? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_center, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // 初始化顶部工具栏
        initToolbar()

        // 显示相机
        btn_camera.setOnClickListener {
            val action =
                UserCenterFragmentDirections.actionUserCenterFragmentToCameraFragment()
            findNavController().navigate(action)
        }

        // 显示大图加载
        btn_banner.setOnClickListener {
            val action =
                UserCenterFragmentDirections.actionUserCenterFragmentToDevViewFragment(
                    DevViewFragment.DevDemoType.LARGE_IMAGE_LOAD
                )
            findNavController().navigate(action)
        }

        // 显示涂鸦
        btn_draw.setOnClickListener {
            val action =
                UserCenterFragmentDirections.actionUserCenterFragmentToDevViewFragment(
                    DevViewFragment.DevDemoType.IMAGE_DRAW
                )
            findNavController().navigate(action)
        }

        // 显示惯性动画
        btn_fling.setOnClickListener {
            val action =
                UserCenterFragmentDirections.actionUserCenterFragmentToDevViewFragment(
                    DevViewFragment.DevDemoType.IMAGE_FLING
                )
            findNavController().navigate(action)
        }

        // 显示弹性动画
        btn_spring.setOnClickListener {
            val action =
                UserCenterFragmentDirections.actionUserCenterFragmentToDevViewFragment(
                    DevViewFragment.DevDemoType.IMAGE_SPRING
                )
            findNavController().navigate(action)
        }

        // 点击执行下载任务
        btn_flv.setOnClickListener {
            ImageDownloadWorker.execute(
                activity!!.application,
                "https://hello1024.oss-cn-beijing.aliyuncs.com/upload/goods/2018120508282483e0ab7a4bd747e70451e6cd101506105c078c28dcc7d1.80178252.jpg"
            ).observe(this, Observer {
                if (it != null && it.state == WorkInfo.State.SUCCEEDED) {
                    showToast("图片下载成功${it.outputData.getString(ImageDownloadWorker.SAVE_IMAGE_PATH)}")
                }
            })
        }

        // 点击播放音乐
        btn_music.setOnClickListener {
            val connection = mMusicConnection ?: MusicConnection().also { mMusicConnection = it }
            context!!.bindService(
                Intent(context, MusicService::class.java),
                connection,
                Service.BIND_AUTO_CREATE
            )
        }

        // 全局搜索
        btn_search.setOnClickListener {
            startActivity(Intent(activity, GlobalSearchActivity::class.java))
        }
    }

    override fun onStop() {
        super.onStop()
        mMusicConnection?.also {
            it.binder?.removePlayerView()
            context?.unbindService(it)
            mMusicConnection = null
        }
    }

    private fun initToolbar() {
        user_center_toolbar.inflateMenu(R.menu.user_center_menu)
        user_center_toolbar.setOnMenuItemClickListener { item ->
            findNavController().navigate(item.itemId)
            true
        }
    }

    inner class MusicConnection : ServiceConnection {

        var binder: MusicService.MusicBinder? = null

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            binder = (service as MusicService.MusicBinder).apply {
                playMusic("https://cool1024.com/upload/c2d8f23c236f257039305cc263ec6439.mp3")
                showPlayerView(activity!!, 48)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }
}
