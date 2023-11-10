package com.devpaul.estructurapublicitarias_roal.domain.mappers

import com.devpaul.estructurapublicitarias_roal.data.models.entity.GetWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.MessageEntity
import com.devpaul.estructurapublicitarias_roal.data.models.response.GetWorkerResponse
import com.devpaul.estructurapublicitarias_roal.data.models.response.MessageResponse

class WorkerMapper {
    fun map(getWorkerResponse: GetWorkerResponse): GetWorker {
        return GetWorker(
            isSuccess = getWorkerResponse.isSuccess,
            code = getWorkerResponse.code,
            message = mapMessage(getWorkerResponse.message)
        )
    }

    private fun mapMessage(message: MessageResponse?): MessageEntity? {
        return message?.let {
            MessageEntity(
                dni = it.dni,
                name = it.name,
                lastname = it.lastname,
                dateBirth = it.dateBirth,
                dateJoin = it.dateJoin,
                area = it.area,
                bloodType = it.bloodType,
                nationality = it.nationality,
                allergies = it.allergies,
                phone = it.phone,
                phoneEmergency = it.phoneEmergency,
                photo = it.photo,
                gender = it.gender,
                diseases = it.diseases
            )
        }
    }


}