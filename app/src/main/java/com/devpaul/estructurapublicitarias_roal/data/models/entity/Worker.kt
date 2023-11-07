package com.devpaul.estructurapublicitarias_roal.data.models.entity

data class Worker(
    var dni: String? = null,
    var name: String? = null,
    var lastname: String? = null,
    var dateBirth: String? = null,
    var dateJoin: String? = null,
    var area: String? = null,
    var bloodType: String? = null,
    var diseases: String? = null,
    var allergies: String? = null,
    var phone: String? = null,
    var phoneEmergency: String? = null,
    var photo: String? = null,
    var photoFormat: String? = null,
) {

    fun filterByArea() : Boolean {

        if (area.equals("TI",true)){
            return false
        }
        if (area.equals("SI",true)){
            return false
        }

        return true
    }
}