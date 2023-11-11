package com.devpaul.estructurapublicitarias_roal.domain.custom_result

import com.devpaul.estructurapublicitarias_roal.domain.utils.MESSAGE_SERVICE_NOT_AVAILABLE
import com.devpaul.estructurapublicitarias_roal.domain.utils.SingletonError

sealed class CustomResult<out T> {
    data class OnSuccess<out T>(val data: T) : CustomResult<T>()
    data class OnError<out T>(val error: CustomError) : CustomResult<T>()
}

open class CustomError(
    val code: Int? = 0,
    val title: String? = null,
    val subtitle: String? = null,
) {
    init {
        if (code == 400 || code == 401 || code == 404 || code == 403 || code == 503 || code == 504
            || code == 502 || code == 408 || code == 500
        ) {
            SingletonError.subTitle = subtitle?.let {
                it.ifEmpty {
                    MESSAGE_SERVICE_NOT_AVAILABLE
                }
            } ?: MESSAGE_SERVICE_NOT_AVAILABLE
        }
    }

    override fun toString(): String {
        return subtitle ?: ""
    }
}

class HttpError(
    code: Int? = 0,
    title: String? = null,
    subtitle: String? = null,
) : CustomError(code, title) {

    init {
        when (code) {
            400, 401, 403, 404, 500, 502, 503, 408 -> {
                SingletonError.title = title
                SingletonError.subTitle = subtitle.takeIf { it?.isNotEmpty() == true } ?: MESSAGE_SERVICE_NOT_AVAILABLE
                SingletonError.code = code
            }


            else -> {
                SingletonError.title = title
                SingletonError.subTitle = "Not Maped"
                SingletonError.code = 0
            }
        }
    }
}

class CustomNotFoundError(code: Int? = 0, title: String? = null, subTitle: String? = null) :
    CustomError(code, subTitle ?: "Data not found", title)

