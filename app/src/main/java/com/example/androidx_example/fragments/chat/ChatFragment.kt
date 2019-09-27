package com.example.androidx_example.fragments.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.androidx_example.R
import com.example.androidx_example.data.ChatMessage
import com.example.androidx_example.fragments.BaseFragment
import com.example.androidx_example.until.ui.ViewUntil
import com.example.androidx_example.works.MessageSendWorker
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : BaseFragment() {

    private var mChatAdapter: ChatAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        initMsgAction()
    }

    override fun onStop() {
        super.onStop()
        ViewUntil.closeKeyBoard(context!!, activity!!.window)
    }

    /**
     * 初始化消息列表
     */
    private fun initRecyclerView() {
        mChatAdapter = mChatAdapter ?: ChatAdapter("cool1024")
        chat_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            adapter = mChatAdapter
        }
        createViewModel(ChatViewModel::class.java).msgRows.observe(this, Observer {
            mChatAdapter?.submitList(it)
        })
    }


    /**
     * 初始化聊天框，设置发送事件
     */
    private fun initMsgAction() {
        msg_edit.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                if (v.text.isNotEmpty()) {
                    val msg = ChatMessage.MessageData(
                        fromUid = "cool1024",
                        toUid = "梦想的乡",
                        type = "TEXT",
                        content = v.text.toString()
                    )
                    v.text = ""
                    // v.clearFocus()
                    MessageSendWorker.send(context!!, msg.toUid, Gson().toJson(msg))
                } else {
                    showToast("不能发空消息哦～")
                }
            }
            true
        }
        btn_send.setOnClickListener { msg_edit.onEditorAction(EditorInfo.IME_ACTION_SEND) }
    }
}
