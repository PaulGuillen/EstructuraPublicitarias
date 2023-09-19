package com.devpaul.estructurapublicitarias_roal.domain.mappers

import com.devpaul.estructurapublicitarias_roal.data.models.Options
import com.devpaul.estructurapublicitarias_roal.data.models.response.OptionsResponse
import java.util.ArrayList

class OptionsMapper {

    fun map(optionsResponse: List<OptionsResponse>): List<Options> {

        val optionsList: MutableList<Options> = ArrayList()
        for (item in optionsResponse) {
            optionsList.add(mapOptions(item))
        }
        return optionsList
    }

    private fun mapOptions(optionsResponse: OptionsResponse): Options {
        val options = Options()
        options.optionID = optionsResponse.optionID
        options.description = optionsResponse.description
        options.name = optionsResponse.name
        options.image = optionsResponse.image

        return options
    }

}