package com.devpaul.estructurapublicitarias_roal.data.models.entity

data class SearchMode(val title: String)

val BY_DNI = SearchMode("Busquedad por DNI")
val BY_PHOTO = SearchMode("Busquedad por FOTO")
val BY_QR = SearchMode("Busquedad por QR")
