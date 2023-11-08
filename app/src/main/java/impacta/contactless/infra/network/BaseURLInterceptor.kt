package impacta.contactless.infra.network

import android.util.Log
import android.util.Patterns
import impacta.contactless.infra.Constants
import okhttp3.Interceptor
import okhttp3.Response

class BaseURLInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var originalRequest = chain.request()
        val requestURL = originalRequest.url.toString()
        val protocol = "(?i:http|https|rtsp)://gm"
        var newURL = (requestURL
            .replaceBefore("v1", ""))
        newURL = if (validateBackSlash(newURL)) Constants.kBaseURL.plus(newURL) else newURL.replaceFirst("/", Constants.kBaseURL)
        originalRequest = originalRequest.newBuilder().url(newURL).build()
        Log.d("BaseURLInterceptor", newURL)
        return chain.proceed(originalRequest)
    }

    private fun validateBackSlash(str: String): Boolean {
        return str.substring(str.length - 1) != "/"
    }
}