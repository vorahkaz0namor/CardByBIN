package ru.cft.cardbybin.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okio.IOException
import ru.cft.cardbybin.dto.Card
import java.lang.Exception

class CardRepositoryNetImpl: CardRepositoryNet {
    private val client = OkHttpClient.Builder().build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<Card>() {}
    companion object {
        private const val BASE_URL = "https://lookup.binlist.net"
    }

    override fun getCardInfo(bin: Int, callback: CardRepositoryNet.CardCallback<Card>) {
        val request: Request = Request.Builder()
            .url("$BASE_URL/$bin")
            .build()
        return client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
                override fun onResponse(call: Call, response: Response) {
                    callback.apply {
                        if (response.isSuccessful) {
                            val card: Card? = response.body.string().let {
                                gson.fromJson(it, typeToken.type)
                            }
                            if (card != null)
                                onSuccess(card)
                            else
                                onError(Exception("Body is null"))
                        } else
                            onError(Exception(response.code.toString()))
                    }
                }

            })
    }
}