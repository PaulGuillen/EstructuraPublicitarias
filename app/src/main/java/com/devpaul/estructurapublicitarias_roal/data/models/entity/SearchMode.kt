package com.devpaul.estructurapublicitarias_roal.data.models.entity

import com.devpaul.estructurapublicitarias_roal.domain.utils.SEARCH_TITLE_DNI
import com.devpaul.estructurapublicitarias_roal.domain.utils.SEARCH_TITLE_PHOTO
import com.devpaul.estructurapublicitarias_roal.domain.utils.SEARCH_TITLE_QR

data class SearchMode(val title: String)

val BY_DNI = SearchMode(SEARCH_TITLE_DNI)
val BY_PHOTO = SearchMode(SEARCH_TITLE_PHOTO)
val BY_QR = SearchMode(SEARCH_TITLE_QR)
