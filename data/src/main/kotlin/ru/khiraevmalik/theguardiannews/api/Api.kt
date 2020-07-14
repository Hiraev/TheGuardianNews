package ru.khiraevmalik.theguardiannews.api

import ru.khiraevmalik.theguardiannews.data.BuildConfig

object Api {

    object TheGuardian {
        const val baseUrl = "https://content.guardianapis.com"
        const val api_key = "api-key"
        const val api_key_value = BuildConfig.THE_GUARDIAN_API_KEY

        object Path {
            const val search = "search?$api_key=$api_key_value&${Params.showFieldsParams}"
        }

        object Params {

            const val query = "q"
            const val showFields = "show-fields"
            const val page = "page"
            const val pageSize = "page-size"
            const val orderBy = "order-by"

            object QueryField {
                const val thumbnail = "thumbnail"
                const val bodyText = "bodyText"
                const val headline = "headline"
                const val trailText = "trailText"
            }

            object OrderByValue {
                const val newest = "newest"
            }

            const val showFieldsParams = "$showFields=${QueryField.bodyText}," +
                    "${QueryField.thumbnail},${QueryField.headline},${QueryField.trailText}"
        }
    }

}
