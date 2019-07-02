package com.example.androidx_example.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.work.WorkInfo
import com.example.androidx_example.R
import com.example.androidx_example.fragments.BaseFragment
import com.example.androidx_example.fragments.DevViewFragment
import com.example.androidx_example.fragments.UserCenterFragmentDirections
import com.example.androidx_example.works.ImageDownloadWorker
import kotlinx.android.synthetic.main.fragment_user_center.*

/**
 * 用户中心
 */
class UserCenterFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_center, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initToolbar()
        btn_camera.setOnClickListener {
            val action =
                UserCenterFragmentDirections.actionUserCenterFragmentToCameraFragment()
            findNavController().navigate(action)
        }
        btn_banner.setOnClickListener {
            val action =
                UserCenterFragmentDirections.actionUserCenterFragmentToDevViewFragment(
                    DevViewFragment.DevDemoType.LARGE_IMAGE_LOAD
                )
            findNavController().navigate(action)
        }
        btn_draw.setOnClickListener {
            val action =
                UserCenterFragmentDirections.actionUserCenterFragmentToDevViewFragment(
                    DevViewFragment.DevDemoType.IMAGE_DRAW
                )
            findNavController().navigate(action)
        }
        btn_fling.setOnClickListener {
            val action =
                UserCenterFragmentDirections.actionUserCenterFragmentToDevViewFragment(
                    DevViewFragment.DevDemoType.IMAGE_FLING
                )
            findNavController().navigate(action)
        }
        btn_spring.setOnClickListener {
            val action =
                UserCenterFragmentDirections.actionUserCenterFragmentToDevViewFragment(
                    DevViewFragment.DevDemoType.IMAGE_SPRING
                )
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

    private fun initToolbar() {
        user_center_toolbar.inflateMenu(R.menu.user_center_menu)
        user_center_toolbar.setOnMenuItemClickListener { item ->
            findNavController().navigate(item.itemId)
            true
        }
    }
}
