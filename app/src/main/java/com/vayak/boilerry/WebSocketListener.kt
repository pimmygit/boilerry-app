package com.vayak.boilerry

import android.util.Log
import com.vayak.boilerry.JsonRequestStore.Companion.getThermostatState
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class WebSocketListener(
    private val viewModel: ThermostatViewModel
): WebSocketListener() {

    private val TAG = "WebSocketListener"

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        viewModel.setStatus(true)
        val thermoState = getThermostatState().toString()
        webSocket.send(thermoState)
        Log.d(TAG, "onOpen: $thermoState")
    }

    override fun onMessage(webSocket: WebSocket, messageText: String) {
        super.onMessage(webSocket, messageText)
        Log.d(TAG, "onMessage: $messageText")

        viewModel.setStateFromJson(Pair(false, messageText))
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d(TAG, "onClosing: code[$code], reason[$reason]")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        viewModel.setStatus(false)
        Log.d(TAG, "onClosed: code[$code], reason[$reason]")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d(TAG, "onFailure: ${t.message} $response")
        super.onFailure(webSocket, t, response)
    }
}