package com.example.androidx_example.fragments.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.androidx_example.R
import com.example.androidx_example.fragments.BaseFragment
import com.example.androidx_example.until.getPxFromDpIntegerId
import kotlinx.android.synthetic.main.fragment_photo_preview.*

class PhotoPreviewFragment : BaseFragment() {

    private val albumName: String by lazy {
        arguments!!.getString(ALBUM_NAME, ALBUM_NAME)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        photo_recycler_view.post {
            photo_recycler_view.apply {
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = AlbumAdapter(width).also {
                    val dps = PhotoDataLoader.loadPhoto(albumName).subscribe { photos ->
                        it.setAlbums(photos)
                    }
                    this@PhotoPreviewFragment.addDisposableToCompositeDisposable(dps)
                }
                addItemDecoration(
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
        private const val ALBUM_NAME: String = "ALBUM_NAME"
        fun create(name: String): PhotoPreviewFragment {
            return PhotoPreviewFragment().apply {
                arguments = Bundle().apply {
                    putString(ALBUM_NAME, name)
                }
            }
        }
    }
}