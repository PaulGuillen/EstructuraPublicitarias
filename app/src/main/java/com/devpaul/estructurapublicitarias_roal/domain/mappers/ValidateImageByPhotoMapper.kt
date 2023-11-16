package com.devpaul.estructurapublicitarias_roal.domain.mappers

import com.devpaul.estructurapublicitarias_roal.data.models.entity.ValidateImageByPhoto
import com.devpaul.estructurapublicitarias_roal.data.models.response.ValidateImageByPhotoResponse

class ValidateImageByPhotoMapper {
    fun map(validateImageByPhotoResponse: ValidateImageByPhotoResponse): ValidateImageByPhoto {

        val validateImageByPhoto = ValidateImageByPhoto()
        validateImageByPhoto.code = validateImageByPhotoResponse.code
        validateImageByPhoto.dni = validateImageByPhotoResponse.dni

        return validateImageByPhoto
    }
}
