package com.example.androidx_example.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import  androidx.core.content.ContextCompat

import com.example.androidx_example.R
import com.example.androidx_example.until.postSuccess
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.btn_login

class LoginFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    /**
     * 初始化相关视图控件
     */
    private fun initView() {
        val defaultColor = ContextCompat.getColor(context!!, R.color.colorMuted)
        val activeColor = ContextCompat.getColor(context!!, R.color.colorPrimary)
        val focusAction = fun(v: View, hasFocus: Boolean) {
            (v as EditText).compoundDrawables.first()?.setTint(if (hasFocus) activeColor else defaultColor)
            updatePasswordImageState()
        }
        input_password.setOnFocusChangeListener(focusAction)
        input_password.compoundDrawables.first()?.setTint(defaultColor)
        input_account.setOnFocusChangeListener(focusAction)
        input_account.compoundDrawables.first()?.setTint(activeColor)
        btn_login.setOnClickListener { submitLoginForm() }
    }

    /**
     * 切换2233娘密码状态图标
     */
    private fun updatePasswordImageState() {
        val drawable22 = ContextCompat.getDrawable(
            context!!,
            if (input_password.isFocused) R.drawable.ic_22_hide else R.drawable.ic_22
        )
        val drawable33 = ContextCompat.getDrawable(
            context!!,
            if (input_password.isFocused) R.drawable.ic_33_hide else R.drawable.ic_33
        )
        ic_22.setImageDrawable(drawable22)
        ic_33.setImageDrawable(drawable33)
    }

    private fun submitLoginForm() {
        val disposable = postSuccess(
            apiName = "managerapi/signin",
            params = hashMapOf(
                "account" to input_account.text.toString(),
                "password" to input_password.text.toString(),
                "platform" to "manager"
            ),
            activity = activity,
            successDo = {
                debugLog(it.getStringData())
            }
        )
        addDisposableToCompositeDisposable(disposable)
    }
}
