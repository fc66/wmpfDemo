@file:Suppress("SpellCheckingInspection")

package com.hy.wmpfdemo.wmpf

import android.util.Log
import com.hy.wmpfdemo.utils.DLog
import com.tencent.mm.ipcinvoker.IPCInvokeCallbackEx
import com.tencent.wmpf.cli.task.*
import com.tencent.wmpf.cli.task.pb.WMPFBaseRequestHelper
import com.tencent.wmpf.cli.task.pb.WMPFIPCInvoker
import com.tencent.wmpf.proto.*
import com.tencent.wmpf.utils.WMPFHelper
import io.reactivex.Observable
import io.reactivex.Single

object Api {

    private const val TAG = "Demo.Api"

    fun activateDevice(productId: Int, keyVerion: Int,
                       deviceId: String, signature: String, hostAppId: String): Single<WMPFActivateDeviceResponse> {
        return Single.create {
            Log.i(TAG, "activateDevice: isInProductionEnv = " + DeviceInfo.isInProductionEnv)
            val request = WMPFActivateDeviceRequest().apply {
                this.baseRequest = WMPFBaseRequestHelper.checked()
                this.productId = productId
                this.keyVersion = keyVerion
                this.deviceId = deviceId
                this.signature = signature.replace(Regex("[\t\r\n]"), "")
                this.hostAppId = hostAppId
            }

            val result = WMPFIPCInvoker.invokeAsync<IPCInvokerTask_ActivateDevice, WMPFActivateDeviceRequest, WMPFActivateDeviceResponse>(
                    request,
                    IPCInvokerTask_ActivateDevice::class.java,
                    object : IPCInvokeCallbackEx<WMPFActivateDeviceResponse> {
                        override fun onBridgeNotFound() {
                            it.onError(Exception("bridge not found"))
                        }

                        override fun onCallback(response: WMPFActivateDeviceResponse) {
                            it.onSuccess(response)
                        }
                    })

            if (!result) {
                it.onError(Exception("invoke activateDevice fail"))
            }
        }
    }

    fun activateDeviceByIoT(hostAppId: String): Single<WMPFActivateDeviceByIoTResponse> {
        return Single.create {
            Log.i(TAG, "activateDeviceByIoT: isInProductionEnv = " + DeviceInfo.isInProductionEnv)
            val request = WMPFActivateDeviceByIoTRequest().apply {
                this.baseRequest = WMPFBaseRequestHelper.checked()
                this.hostAppId = hostAppId
            }

            val result = WMPFIPCInvoker.invokeAsync<IPCInvokerTask_ActivateDeviceByIoT,
                    WMPFActivateDeviceByIoTRequest, WMPFActivateDeviceByIoTResponse>(
                    request,
                    IPCInvokerTask_ActivateDeviceByIoT::class.java,
                    object : IPCInvokeCallbackEx<WMPFActivateDeviceByIoTResponse> {
                        override fun onBridgeNotFound() {
                            it.onError(Exception("bridge not found"))
                        }

                        override fun onCallback(response: WMPFActivateDeviceByIoTResponse) {
                            it.onSuccess(response)
                        }
                    })

            if (!result) {
                it.onError(Exception("invoke activateDevice fail"))
            }
        }
    }

    fun notifyActivateDevice(): Single<WMPFNotifyActivateDeviceResponse> {
        return Single.create {
            val request = WMPFNotifyActivateDeviceRequest().apply {
                this.baseRequest = WMPFBaseRequestHelper.checked()
            }

            val result = WMPFIPCInvoker.invokeAsync<IPCInvokerTask_NotifyActivateDevice,
                    WMPFNotifyActivateDeviceRequest, WMPFNotifyActivateDeviceResponse>(
                    request,
                    IPCInvokerTask_NotifyActivateDevice::class.java,
                    object : IPCInvokeCallbackEx<WMPFNotifyActivateDeviceResponse> {
                        override fun onBridgeNotFound() {
//                            it.onError(Exception("bridge not found"))
                            DLog.e("")
                        }

                        override fun onCallback(response: WMPFNotifyActivateDeviceResponse) {
//                            it.onSuccess(response)
                            DLog.e("")
                        }
                    })

            if (!result) {
                it.onError(Exception("invoke notifyActivateDevice fail"))
            }
        }
    }

    fun preloadRuntime(): Single<WMPFPreloadRuntimeResponse> {
        return Single.create {
            val request = WMPFPreloadRuntimeRequest().apply {
                baseRequest = WMPFBaseRequestHelper.checked()
            }

            val result = WMPFIPCInvoker.invokeAsync<IPCInvokerTask_PreloadRuntime,
                    WMPFPreloadRuntimeRequest, WMPFPreloadRuntimeResponse>(
                    request,
                    IPCInvokerTask_PreloadRuntime::class.java
            ) { response -> it.onSuccess(response) }

            if (!result) {
                it.onError(Exception("invoke authorize fail"))
            }
        }
    }

    fun authorize(appId: String, ticket: String, scope: String): Single<WMPFAuthorizeResponse> {
        return Single.create {
            val request = WMPFAuthorizeRequest()
            request.baseRequest = WMPFBaseRequestHelper.checked()
            request.ticket = ticket
            request.appId = appId // OpenSDK AppId for App
            request.scope = scope
            // 需要OauthCode，将该变量置为true
            // OauthCode需要BuildConfig.HOST_APPID有开发者资质
            request.needOauthCode = true

            val result = WMPFIPCInvoker.invokeAsync<IPCInvokerTask_Authorize,
                    WMPFAuthorizeRequest, WMPFAuthorizeResponse>(
                    request,
                    IPCInvokerTask_Authorize::class.java
            ) {
                response ->
                Log.i(TAG, ": $response")
                it.onSuccess(response) }

            if (!result) {
                it.onError(Exception("invoke authorize fail"))
            }
        }
    }

    fun authorizeNoLogin(appId: String, ticket: String, scope: String): Single<WMPFAuthorizeNoLoginResponse> {
        return Single.create {
            val request = WMPFAuthorizeNoLoginRequest()
            request.baseRequest = WMPFBaseRequestHelper.checked()
            request.deviceInfo = ""
            request.deviceTicket = ""
            request.ticket = ticket
            request.appId = appId // OpenSDK AppId for App
            request.scope = scope

            val result = WMPFIPCInvoker.invokeAsync<IPCInvokerTask_AuthorizeNoLogin,
                    WMPFAuthorizeNoLoginRequest, WMPFAuthorizeNoLoginResponse>(
                    request,
                    IPCInvokerTask_AuthorizeNoLogin::class.java
            ) { response -> it.onSuccess(response) }

            if (!result) {
                it.onError(Exception("invoke authorize fail"))
            }
        }
    }

    fun initWxPayInfoAuthInfo(authInfoMap: Map<String, Any>): Single<WMPFInitWxFacePayInfoResponse> {
        return Single.create {

            val request = WMPFInitWxFacePayInfoRequest()
            request.baseRequest = WMPFBaseRequestHelper.checked()
            request.wxFacePayInfo = WMPFHelper.map2Json(authInfoMap)

            val result = WMPFIPCInvoker.invokeAsync<IPCInvokerTask_InitWxFacePayInfo,
                    WMPFInitWxFacePayInfoRequest, WMPFInitWxFacePayInfoResponse>(
                    request,
                    IPCInvokerTask_InitWxFacePayInfo::class.java
            ) {
                response -> it.onSuccess(response)
            }

            if (!result) {
                it.onError(Exception("invoke authorize fail"))
            }
        }
    }

    fun authorizeFaceLogin(): Single<WMPFAuthorizeByWxFacePayResponse> {
        return Single.create {

            val request = WMPFAuthorizeByWxFacePayRequest()
            request.baseRequest = WMPFBaseRequestHelper.checked()

            val result = WMPFIPCInvoker.invokeAsync<IPCInvokerTask_AuthorizeByWxFacePay,
                    WMPFAuthorizeByWxFacePayRequest, WMPFAuthorizeByWxFacePayResponse>(
                    request,
                    IPCInvokerTask_AuthorizeByWxFacePay::class.java
            ) {
                response -> it.onSuccess(response)
            }

            if (!result) {
                it.onError(Exception("invoke authorize fail"))
            }
        }
    }


    fun launchWxaApp(launchAppId: String, path: String, appType: Int = 0, landsapeMode: Int = 0): Single<WMPFLaunchWxaAppResponse> {
        return Single.create {
            val request = WMPFLaunchWxaAppRequest()
            request.baseRequest = WMPFBaseRequestHelper.checked()
            // Launch target(wxa appId)
            // WARNING: hostAppIds and wxaAppIds are binded sets.
            request.appId = launchAppId // Binded with HOST_APPID: wx64b7714cf1f64585
            request.path = path
            request.appType = appType // 0-正式版 1-开发版 2-体验版
            // mayRunInLandscapeCompatMode Deprecated
//            request.mayRunInLandscapeCompatMode = true
            request.forceRequestFullscreen = false
            request.landscapeMode = landsapeMode // 0:和微信行为保持一致;1:允许横屏铺满显示，忽略小程序的pageOrientation配置;2:强制横屏并居中以16:9显示，忽略pageOrientation配置
            Log.i(TAG, "launchWxaApp: appId = " + launchAppId + ", hostAppID = " +
                    DeviceInfo.HOST_APP_ID + ", deviceId = " + DeviceInfo.deviceId)
            val result = WMPFIPCInvoker.invokeAsync<IPCInvokerTask_LaunchWxaApp, WMPFLaunchWxaAppRequest,
                    WMPFLaunchWxaAppResponse>(
                    request,
                    IPCInvokerTask_LaunchWxaApp::class.java
            ) { response -> it.onSuccess(response) }

            if (!result) {
                it.onError(Exception("invoke launchWxaApp fail"))
            }
        }
    }

    fun launchWxaAppByScan(rawData: String): Single<WMPFLaunchWxaAppByQRCodeResponse> {
        return Single.create {
            val request = WMPFLaunchWxaAppByQRCodeRequest()
            request.baseRequest = WMPFBaseRequestHelper.checked()
            request.baseRequest.clientApplicationId = ""
            request.rawData = rawData // rawData from qrcode

            Log.i(TAG, "launchWxaApp: " + "hostAppID = " +
                    DeviceInfo.HOST_APP_ID + ", deviceId = " + DeviceInfo.deviceId)

            val result = WMPFIPCInvoker.invokeAsync<IPCInvokerTask_LaunchWxaAppByQrCode,
                    WMPFLaunchWxaAppByQRCodeRequest, WMPFLaunchWxaAppByQRCodeResponse>(
                        request,
                        IPCInvokerTask_LaunchWxaAppByQrCode::class.java
                ) { response -> it.onSuccess(response) }

            if (!result) {
                it.onError(Exception("invoke launchWxaAppByScan fail"))
            }
        }
    }

    fun closeWxaApp(appId: String): Single<WMPFCloseWxaAppResponse> {
        return Single.create {
            val request = WMPFCloseWxaAppRequest()
            request.baseRequest = WMPFBaseRequestHelper.checked()
            request.baseRequest.clientApplicationId = ""
            request.appId = appId

            val result = WMPFIPCInvoker.invokeAsync<IPCInvokerTask_CloseWxaApp,
                    WMPFCloseWxaAppRequest, WMPFCloseWxaAppResponse>(
                    request,
                    IPCInvokerTask_CloseWxaApp::class.java
            ) { response -> it.onSuccess(response) }

            if (!result) {
                it.onError(Exception("invoke launchWxaAppByScan fail"))
            }
        }
    }

    fun manageBackgroundMusic(showManageUI: Boolean = true, forceRequestFullscreen: Boolean = false): Single<WMPFManageBackgroundMusicResponse> {
        return Single.create {
            val request = WMPFManageBackgroundMusicRequest()
            request.baseRequest = WMPFBaseRequestHelper.checked()
            request.showManageUI = showManageUI
            request.forceRequestFullscreen = forceRequestFullscreen
            val result = WMPFIPCInvoker.invokeAsync<IPCInvokerTask_ManageBackgroundMusic, WMPFManageBackgroundMusicRequest,
                    WMPFManageBackgroundMusicResponse>(
                    request,
                    IPCInvokerTask_ManageBackgroundMusic::class.java
            ) { response -> it.onSuccess(response) }

            if (!result) {
                it.onError(Exception("invoke manageBackgroundMusic fail"))
            }
        }
    }

    // 监听背景音频改变
    fun notifyBackgroundMusic(): Observable<WMPFNotifyBackgroundMusicResponse> {
        return Observable.create {
            val request = WMPFNotifyBackgroundMusicRequest()
            request.baseRequest = WMPFBaseRequestHelper.checked()
            request.notify = true
            val result = WMPFIPCInvoker.invokeAsync<IPCInvokerTask_NotifyBackgroundMusic,
                    WMPFNotifyBackgroundMusicRequest, WMPFNotifyBackgroundMusicResponse>(
                    request,
                    IPCInvokerTask_NotifyBackgroundMusic::class.java
            ) {
                response ->
                /**
                 * {@see com.tencent.wmpf.cli.task.IPCInvokerTask_NotifyBackgroundMusic}
                 * val START = 1
                 * val RESUME = 2
                 * val PAUSE = 3
                 * val STOP = 4
                 * val COMPLETE = 5
                 * val ERROR = 6
                 **/
                it.onNext(response)
            }

            if (!result) {
                it.onError(Exception("invoke notifyBackgroundMusic fail"))
            }
        }
    }

    fun deauthorize(): Single<WMPFDeauthorizeResponse> {
        return Single.create {
            val request = WMPFDeauthorizeRequest()
            request.baseRequest = WMPFBaseRequestHelper.checked()

            val result = WMPFIPCInvoker.invokeAsync<IPCInvokerTask_Deauthorize,
                    WMPFDeauthorizeRequest, WMPFDeauthorizeResponse>(
                    request,
                    IPCInvokerTask_Deauthorize::class.java
            ) { response -> it.onSuccess(response) }

            if (!result) {
                it.onError(Exception("invoke deauthorize fail"))
            }
        }
    }

    fun listeningPushMsg(): Observable<WMPFPushMsgResponse> {
        return Observable.create {
            val request = WMPFPushMsgRequest()
            request.baseRequest = WMPFBaseRequestHelper.checked()
            val result = WMPFIPCInvoker.invokeAsync<IPCInovkerTask_SetPushMsgCallback,
                    WMPFPushMsgRequest, WMPFPushMsgResponse>(request, IPCInovkerTask_SetPushMsgCallback::class.java) { response ->
                it.onNext(response)
            }
            if (!result) {
                it.onError(Exception("invoke listeningPushMsg fail"))
            }
        }
    }

    fun activeStatus(): Single<WMPFActiveStatusResponse> {
        return Single.create {
            val request = WMPFActiveStatusRequest().apply {
                this.baseRequest = WMPFBaseRequestHelper.checked()
            }

            val result = WMPFIPCInvoker.invokeAsync<IPCInvokerTask_ActiveStatus, WMPFActiveStatusRequest, WMPFActiveStatusResponse>(
                    request,
                    IPCInvokerTask_ActiveStatus::class.java,
                    object : IPCInvokeCallbackEx<WMPFActiveStatusResponse> {
                        override fun onBridgeNotFound() {
                            it.onError(Exception("bridge not found"))
                        }

                        override fun onCallback(response: WMPFActiveStatusResponse) {
                            it.onSuccess(response)
                        }
                    })

            if (!result) {
                it.onError(Exception("invoke activeStatus fail"))
            }
        }
    }

    fun authorizeStatus(): Single<WMPFAuthorizeStatusResponse> {
        return Single.create {
            val request = WMPFAuthorizeStatusRequest().apply {
                this.baseRequest = WMPFBaseRequestHelper.checked()
            }

            val result = WMPFIPCInvoker.invokeAsync<IPCInvokerTask_AuthorizeStatus, WMPFAuthorizeStatusRequest, WMPFAuthorizeStatusResponse>(
                    request,
                    IPCInvokerTask_AuthorizeStatus::class.java,
                    object : IPCInvokeCallbackEx<WMPFAuthorizeStatusResponse> {
                        override fun onBridgeNotFound() {
                            it.onError(Exception("bridge not found"))
                        }

                        override fun onCallback(response: WMPFAuthorizeStatusResponse) {
                            it.onSuccess(response)
                        }
                    })

            if (!result) {
                it.onError(Exception("invoke activeStatus fail"))
            }
        }
    }
}