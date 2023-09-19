package com.devpaul.estructurapublicitarias_roal.domain.mappers

import com.devpaul.estructurapublicitarias_roal.data.models.Worker
import com.devpaul.estructurapublicitarias_roal.data.models.response.WorkersResponse

class WorkerMapper {

    fun map(workerResponse: WorkersResponse): Worker {

        val worker = Worker()
        worker.dni = workerResponse.dni
        worker.name = workerResponse.name
        worker.lastname = workerResponse.lastname
        worker.dateBirth = workerResponse.dateBirth
        worker.dateJoin = workerResponse.dateJoin
        worker.area = workerResponse.area
        worker.bloodType = workerResponse.bloodType
        worker.diseases = workerResponse.diseases
        worker.allergies = workerResponse.allergies
        worker.phone = workerResponse.phone
        worker.phoneEmergency = workerResponse.phoneEmergency
        worker.photo = workerResponse.photo
        worker.photoFormat = workerResponse.photoFormat

        return worker
    }

}