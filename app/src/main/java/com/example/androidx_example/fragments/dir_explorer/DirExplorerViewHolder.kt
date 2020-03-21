package com.example.androidx_example.fragments.dir_explorer

import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import com.example.androidx_example.R
import com.example.androidx_example.databinding.DirExplorerItemViewBinding
import com.example.androidx_example.until.adapter.BaseRecyclerAdapter
import com.example.androidx_example.until.ui.ViewUntil
import com.example.httprequest.Request

class DirExplorerViewHolder(
    view: View,
    private val binding: DirExplorerItemViewBinding,
    private val navCtrl: NavController,
    private val maxWidth: Int
) : BaseRecyclerAdapter.ViewHolderBinder<DirExplorerViewModel.FileItem>(view) {

    override fun bind(item: DirExplorerViewModel.FileItem) {
        binding.file = item
        binding.handler = this
        item.size?.run {
            val fitSize = ViewUntil.getFitSize(item.size.toSize(), Size(maxWidth, Int.MAX_VALUE))
            itemView.layoutParams = itemView.layoutParams.apply {
                height = fitSize.height
            }
        }
    }

    fun showFileExplorer(item: DirExplorerViewModel.FileItem) {
        when (item.type) {
            DirExplorerViewModel.FileType.PDF -> {
                val pdfUrl = Request.getRequestUrl(item.downloadUrl)
                PdfWindow.createAndShow(pdfUrl, itemView.rootView as ViewGroup)
            }
            DirExplorerViewModel.FileType.VIDEO -> {

            }
            DirExplorerViewModel.FileType.OTHER -> {

            }
            DirExplorerViewModel.FileType.IMAGE -> {
                PhotoWindow.createAndShow(item.previewUrl, itemView.rootView as ViewGroup)
            }
            DirExplorerViewModel.FileType.DIR -> {
                val action =
                    DirExplorerFragmentDirections.actionDirExplorerFragmentSelf(item.filePath)
                navCtrl.navigate(action)
            }
        }
    }

    companion object {
        fun create(
            parentView: ViewGroup,
            navCtrl: NavController,
            maxWidth: Int
        ): DirExplorerViewHolder {
            val binding = DataBindingUtil.inflate<DirExplorerItemViewBinding>(
                LayoutInflater.from(parentView.context),
                R.layout.dir_explorer_item_view,
                parentView,
                false
            )
            return DirExplorerViewHolder(binding.root, binding, navCtrl, maxWidth)
        }
    }
}