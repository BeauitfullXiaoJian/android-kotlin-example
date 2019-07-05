package com.example.androidx_example.fragments.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.androidx_example.R
import com.example.androidx_example.data.ChatMessage
import com.example.androidx_example.fragments.BaseFragment
import com.example.androidx_example.until.ChatMessageBus
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : BaseFragment() {

    private var mMessageDisposable: Disposable? = null
    private var mChatAdapter: ChatAdapter? = null
    private var mChatRows: ArrayList<ChatMessage> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mMessageDisposable = ChatMessageBus.obsOnMainThread {
            val size = mChatRows.size
            mChatRows.add(it)
            mChatAdapter?.notifyItemRangeChanged(size, 1)
            chat_recycler_view.scrollToPosition(size)
        }
        mChatAdapter = mChatAdapter ?: ChatAdapter(mChatRows)
        chat_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mChatAdapter
        }
    }

    override fun onDetach() {
        super.onDetach()
        mMessageDisposable?.dispose()
    }
}
