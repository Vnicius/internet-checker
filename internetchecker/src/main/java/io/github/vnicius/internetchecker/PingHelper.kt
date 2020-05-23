package io.github.vnicius.internetchecker

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.lang.ref.WeakReference


/**
 * Created by @vnicius on 23/05/20.
 * vinicius.matheus252@gmail.com
 */
class PingHelper(context: Context) {
    private val contextReference = WeakReference(context.applicationContext)
    private val queue: RequestQueue? by lazy {
        contextReference.get()?.let { context ->
            Volley.newRequestQueue(context)
        }
    }

    fun ping(onSuccess: () -> Unit = {}, onError: () -> Unit = {}) {
        val request = StringRequest(Request.Method.GET, PING_HOST, Response.Listener { _ ->
            onSuccess()
        }, Response.ErrorListener {
            onError()
        })

        queue?.add(request)
    }

    companion object {
        const val PING_HOST = "https://www.google.com"
    }
}