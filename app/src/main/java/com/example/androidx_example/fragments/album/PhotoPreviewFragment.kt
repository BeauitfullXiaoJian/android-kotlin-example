package com.example.androidx_example.fragments.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.androidx_example.R
import com.example.androidx_example.data.PhotoData
import com.example.androidx_example.fragments.BaseFragment
import com.example.androidx_example.until.getPxFromDpIntegerId
import kotlinx.android.synthetic.main.fragment_photo_preview.*

class PhotoPreviewFragment : BaseFragment() {

    private val photos: Array<PhotoData> by lazy {
        arguments!!.getSerializable(PHOTO_DATA) as Array<PhotoData>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        photo_recycler_view.post {
            photo_recycler_view.apply {
                adapter = AlbumAdapter(photos, photo_recycler_view.width)
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                photo_recycler_view.addItemDecoration(
                    PhotoItemDecoration(
                        getPxFromDpIntegerId(
                            resources,
                            R.integer.space_sm_value
                        )
                    )
                )
            }
        }
    }

    companion object {
        private const val PHOTO_DATA: String = "PHOTO_DATA"
        fun create(photos: Array<PhotoData>): PhotoPreviewFragment {
            return PhotoPreviewFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PHOTO_DATA, photos)
                }
            }
        }
    }
}