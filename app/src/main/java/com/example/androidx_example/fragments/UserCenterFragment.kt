package com.example.androidx_example.fragments


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.work.WorkInfo
import com.example.androidx_example.BaseActivity

import com.example.androidx_example.R
import com.example.androidx_example.until.requestPermission
import com.example.androidx_example.works.ImageDownloadWorker
import kotlinx.android.synthetic.main.fragment_user_center.*

/**
 * 用户中心
 *
 */
class UserCenterFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_center, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_camera.setOnClickListener {
            val action = UserCenterFragmentDirections.actionUserCenterFragmentToCameraFragment()
            findNavController().navigate(action)
        }
        btn_banner.setOnClickListener {
            val action = UserCenterFragmentDirections.actionUserCenterFragmentToLargeImageFragment()
            findNavController().navigate(action)
        }
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
    }
}
