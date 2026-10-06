package com.example.simpleenergy.data.remote

import android.content.Context
import com.example.simpleenergy.data.remote.dto.VehicleDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * Serves mock REST responses from assets for offline/demo use.
 * Retrofit still performs HTTP-shaped calls; this interceptor short-circuits them.
 */
class AssetMockInterceptor(
    private val context: Context,
) : Interceptor {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val listAdapter = moshi.adapter<List<VehicleDto>>(
        Types.newParameterizedType(List::class.java, VehicleDto::class.java),
    )

    private val itemAdapter = moshi.adapter(VehicleDto::class.java)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath.trimEnd('/')

        return when {
            path.endsWith("/vehicles") -> {
                val body = readAsset("mock/vehicles.json")
                jsonResponse(request, body)
            }
            path.contains("/vehicles/") -> {
                val id = path.substringAfterLast("/")
                val vehicles = listAdapter.fromJson(readAsset("mock/vehicles.json")).orEmpty()
                val match = vehicles.firstOrNull { it.id == id }
                if (match == null) {
                    errorResponse(request, code = 404, message = """{"error":"Vehicle not found"}""")
                } else {
                    jsonResponse(request, itemAdapter.toJson(match))
                }
            }
            else -> chain.proceed(request)
        }
    }

    private fun readAsset(fileName: String): String =
        context.assets.open(fileName).bufferedReader().use { it.readText() }

    private fun jsonResponse(request: okhttp3.Request, body: String): Response =
        Response.Builder()
            .code(200)
            .message("OK")
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .body(body.toResponseBody("application/json".toMediaType()))
            .build()

    private fun errorResponse(request: okhttp3.Request, code: Int, message: String): Response =
        Response.Builder()
            .code(code)
            .message("Error")
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .body(message.toResponseBody("application/json".toMediaType()))
            .build()
}
