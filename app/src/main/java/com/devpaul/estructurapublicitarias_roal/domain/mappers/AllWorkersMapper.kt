package com.devpaul.estructurapublicitarias_roal.domain.mappers

import com.devpaul.estructurapublicitarias_roal.data.models.entity.AllWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.BodyWorker
import com.devpaul.estructurapublicitarias_roal.data.models.response.AllWorkerResponse
import com.devpaul.estructurapublicitarias_roal.data.models.response.PaginationInfoResponse

class AllWorkersMapper {
    fun mapToWorkersResponseEntity(allWorkerResponse: AllWorkerResponse): AllWorker {

        val allWorker = AllWorker()

        allWorker.workers = allWorkerResponse.workers?.map { item ->
            BodyWorker(
                dni = item.dni,
                date_birth = item.date_birth,
                date_join = item.date_join,
                name = item.name,
                gender = item.gender,
                phone = item.phone,
                blood_type = item.blood_type,
                allergies = item.allergies,
                lastname = item.lastname,
                nationality = item.nationality,
                phone_emergency = item.phone_emergency,
                photo = item.photo,
                diseases = item.diseases,
                area = item.area
            )
        }
        allWorker.paginationInfo = PaginationInfoResponse(
            totalResult = allWorkerResponse.paginationInfo?.totalResult ?: 0
        )

        return allWorker
    }
}