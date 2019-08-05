package com.example.androidx_example.fragments.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.example.androidx_example.R
import com.example.androidx_example.data.PhotoData
import com.example.androidx_example.fragments.BaseFragment
import com.example.androidx_example.until.api.HttpRequest
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_public.*


/**
 * 公开广场
 */
class PublicFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_public, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        public_recycler_view.post {
            public_recycler_view.adapter = AlbumAdapter(loadAlbum(), public_recycler_view.width)
            public_recycler_view.layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
        }
    }

    private fun loadAlbum(): Array<PhotoData> {
        val inputStream = resources.openRawResource(R.raw.photos)
        val byteArray = ByteArray(inputStream.available())
        inputStream.read(byteArray)
        return Gson().fromJson(byteArray.toString(Charsets.UTF_8), Array<PhotoData>::class.java)
    }
}
