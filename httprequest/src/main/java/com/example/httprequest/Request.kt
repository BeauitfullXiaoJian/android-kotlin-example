package com.example.httprequest

import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import java.sql.ParameterMetaData

object Request {

    fun getSync(
        apiName: String,
        params: HashMap<String, Any>
    ): ApiData = HttpRequest.instance().getSync(apiName, params).apiData


    fun get(
        apiName: String,
        params: HashMap<String, Any>,
        successDo: (res: ApiData) -> Unit,
        completeDo: ((result: Boolean) -> Unit)? = null
    ) {
        val disposableTool = HttpRequest.DisposableTool()
        HttpRequest.instance().let { request ->
            request.get(apiName, params)
                .doOnSubscribe { disposable ->
                    disposableTool.disposable = disposable
                }.doFinally {
                    disposableTool.dispose()
                }.subscribe { res ->
                    if (res.apiData.isOk()) successDo(res.apiData)
                    completeDo?.invoke(res.apiData.isOk())
                }
        }
    }

    fun get(
        apiName: String,
        params: HashMap<String, Any>,
        allowSave: Boolean = false,
        successDo: (res: ApiData) -> Unit,
        completeDo: ((result: Boolean) -> Unit)? = null
    ) {
        if (allowSave) {
            HttpRequest.instance().let { request ->
                val saveData = request.dataSaver.find(apiName, params.hashCode())
                val disposableTool = HttpRequest.DisposableTool()
                if (saveData != null) {
                    val apiData = request.transformer.transform(saveData, HttpRequest.CODE_SUCCESS)
                    successDo(apiData)
                    completeDo?.invoke(apiData.isOk())
                } else {
                    request.get(apiName, params)
                        .doOnSubscribe { disposable ->
                            disposableTool.disposable = disposable
                        }.doFinally {
                            disposableTool.dispose()
                        }.subscribe { res ->
                            if (res.apiData.isOk()) {
                                request.dataSaver.save(
                                    apiName,
                                    params.hashCode(),
                                    res.originBodyStr
                                )
                                successDo(res.apiData)
                            }
                            completeDo?.invoke(res.apiData.isOk())
                        }
                }
            }
        } else {
            get(apiName, params, successDo, completeDo)
        }
    }

    fun postSync(
        apiName: String,
        params: HashMap<String, Any>? = null
    ): ApiData = HttpRequest.instance().postSync(apiName, null, params).apiData

    /**
     * 发送一个POST请求，并剔除掉错误的消息
     * @param apiName String 接口名称
     * @param params HashMap<String, Any> 请求参数
     * @param successDo (res: HttpRequest.ApiData) -> Unit 成功回调方法
     */
    fun post(
        apiName: String,
        params: HashMap<String, Any>,
        successDo: (res: ApiData) -> Unit,
        completeDo: ((result: Boolean) -> Unit)? = null
    ) {
        HttpRequest.instance().let { request ->
            val disposableTool = HttpRequest.DisposableTool()
            request.post(apiName, null, params)
                .doOnSubscribe { disposable ->
                    disposableTool.disposable = disposable
                }.doFinally {
                    disposableTool.dispose()
                }.subscribe { res ->
                    if (res.apiData.isOk()) successDo(res.apiData)
                    completeDo?.invoke(res.apiData.isOk())
                }
        }
    }

    fun download(downloadUrl: String): Response = HttpRequest.instance().download(downloadUrl)

    fun webSocket(
        authToken: String,
        protocol: String,
        messageCallbackFun: (type: HttpRequest.WebSocketContentType, content: String, webSocket: WebSocket) -> Unit
    ) = HttpRequest.instance().webSocket(authToken, protocol, messageCallbackFun)
}