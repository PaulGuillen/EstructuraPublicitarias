package com.devpaul.estructurapublicitarias_roal.data.models.entity

data class GetWorker(
    var isSuccess: Boolean? = null,
    var code: Int? = null,
    var message: MessageEntity? = null,
)

data class MessageEntity(
    var dni: String? = null,
    var dateBirth: String? = null,
    var dateJoin: String? = null,
    var name: String? = null,
    var gender: String? = null,
    var phone: String? = null,
    var bloodType: String? = null,
    var allergies: String? = null,
    var lastname: String? = null,
    var diseases: String? = null,
    var nationality: String? = null,
    var phoneEmergency: String? = null,
    var photo: String? = null,
    var area: String? = null,
)

