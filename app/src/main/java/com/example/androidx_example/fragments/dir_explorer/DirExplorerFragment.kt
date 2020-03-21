package com.example.androidx_example.fragments.dir_explorer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.example.androidx_example.R
import com.example.androidx_example.fragments.BaseFragment
import com.example.androidx_example.until.adapter.BaseRecyclerAdapter
import com.example.androidx_example.until.ui.ViewUntil
import kotlinx.android.synthetic.main.dir_explorer_fragment.*
import java.lang.Exception

class DirExplorerFragment : BaseFragment() {

    private val args by navArgs<DirExplorerFragmentArgs>()
    private lateinit var viewModel: DirExplorerViewModel
    private val listColNum = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dir_explorer_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = createViewModel(DirExplorerViewModel::class.java)
        trySetDir()
        initView()
    }

    private fun trySetDir() {
        try {
            viewModel.parentDirPath.value = args.dirPath
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {
        val defaultSpace = ViewUntil.getPxFromDpIntegerId(resources, R.integer.space_sm_value)
        mRecyclerView.post {
            mRecyclerView.apply {
                addItemDecoration(BaseRecyclerAdapter.ViewItemDecoration(defaultSpace))
                adapter = DirExplorerAdapter(findNavController(),  width / 2).also {
                    viewModel.fileList.observe(this@DirExplorerFragment) { items ->
                        it.updateItems(items)
                    }
                }
                layoutManager = StaggeredGridLayoutManager(
                    listColNum,
                    StaggeredGridLayoutManager.VERTICAL
                )
            }
        }
        mRefreshLayout.apply {
            viewModel.refreshing.observe(this@DirExplorerFragment) { value ->
                isRefreshing = value
            }
            setColorSchemeResources(R.color.colorPrimary)
            setOnRefreshListener { viewModel.loadFileList() }
        }
    }
}
