package ru.cft.cardbybin.util

import android.util.Log
import okhttp3.internal.http.HTTP_FORBIDDEN
import okhttp3.internal.http.HTTP_NOT_FOUND
import okhttp3.internal.http.HTTP_NO_CONTENT
import okhttp3.internal.http.HTTP_UNAUTHORIZED
import retrofit2.HttpException
import java.net.ConnectException

object CompanionCardByBin {
    /** BIN example */
    const val SAMPLE_BIN = "45713706"
    /** Required BIN length */
    const val BIN_LENGTH = 8
    /** Converts Boolean condition to String representation */
    val yesOrNo: (Boolean) -> String = { if (it) "yes" else "no" }
    /** Sets text depending on the input content */
    val customSetText: (String) -> String = { it.ifBlank { "-" } }
    /** `520 Unknown Error` (non-standard HTTP code CloudFlare) */
    private const val HTTP_UNKNOWN_ERROR = 520
    /** `444 Connection Failed` (thought up code) */
    private const val HTTP_CONNECTION_FAILED = 444
    /** Definition of the exception code */
    val exceptionCode = { e: Exception ->
        when (e) {
            is HttpException -> e.code()
            is ConnectException -> HTTP_CONNECTION_FAILED
            else -> HTTP_UNKNOWN_ERROR
        }
    }
    /** Description of the exception code */
    val overview = { code: Int ->
        when (code) {
            in 200..299 -> {
                if (code == HTTP_NO_CONTENT)
                    "Body is null"
                else
                    "Successful"
            }
            in 400..499 ->
                when (code) {
                    HTTP_CONNECTION_FAILED -> "Connection failed"
                    HTTP_NOT_FOUND -> "Not found"
                    HTTP_FORBIDDEN -> "Forbidden"
                    HTTP_UNAUTHORIZED -> "Unauthorized"
                    else -> "Some other HTTP code"
                }
            in 500..599 -> {
                if (code == HTTP_UNKNOWN_ERROR)
                    "Unknown error"
                else
                    "Internal server error"
            }
            else -> "Continue..."
        }
    }

    /** Custom caught exception logging */
    fun customLog(action: String, e: Exception) {
        Log.d(action, "CAUGHT EXCEPTION => $e\n" +
                "DESCRIPTION => ${overview(exceptionCode(e))}")
    }
}
