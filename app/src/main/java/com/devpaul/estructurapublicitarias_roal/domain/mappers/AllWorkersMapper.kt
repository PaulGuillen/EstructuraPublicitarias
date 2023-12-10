package com.devpaul.estructurapublicitarias_roal.domain.mappers

import com.devpaul.estructurapublicitarias_roal.data.models.entity.ListWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.BodyWorker
import com.devpaul.estructurapublicitarias_roal.data.models.entity.PaginationInfoResponse
import com.devpaul.estructurapublicitarias_roal.data.models.response.ListWorkerResponse

class AllWorkersMapper {
    fun map(listWorkerResponse: ListWorkerResponse): ListWorker {

        val listWorker = ListWorker()

        listWorker.workers = listWorkerResponse.workers?.map { item ->
            BodyWorker(
                dni = item.dni,
                dateBirth = item.dateBirth,
                dateJoin = item.dateJoin,
                name = item.name,
                gender = item.gender,
                phone = item.phone,
                bloodType = item.bloodType,
                allergies = item.allergies,
                lastname = item.lastname,
                nationality = item.nationality,
                phoneEmergency = item.phoneEmergency,
                photo = item.photo,
                diseases = item.diseases,
                area = item.area
            )
        }
        listWorker.paginationInfo = PaginationInfoResponse(
            totalResult = listWorkerResponse.paginationInfo?.totalResult ?: 0
        )

        return listWorker
    }
}