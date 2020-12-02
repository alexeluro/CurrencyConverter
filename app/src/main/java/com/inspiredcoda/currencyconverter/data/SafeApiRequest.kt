package com.inspiredcoda.currencyconverter.data

import com.inspiredcoda.currencyconverter.util.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {

    /**
     * This is a wrapper class to map the response of each request code to a desired response
     * */

    suspend fun<T: Any> apiRequest(call: suspend() -> Response<T>) : T{
        val response = call.invoke()
        if(response.isSuccessful){
            return response.body()!!
        }
        if (response.code() == 503) {
            throw ApiException("Service Temporarily Unavailable")
        }

        if (response.code() == 400) {
            throw ApiException("Invalid Data")
        }

        else {

            val error = response.errorBody()?.string()

            val message = StringBuilder()
            error?.let{
                try{
                    val v = JSONObject(it).getString("success")
                    message.append((v))
                }catch(e: JSONException){ }
                message.append("\n")
            }
            message.append("Error Code: ${response.code()}")
            throw ApiException(message.toString())

        }
    }

}