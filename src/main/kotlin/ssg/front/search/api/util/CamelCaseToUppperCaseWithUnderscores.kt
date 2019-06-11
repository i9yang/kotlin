package ssg.front.search.api.util

import com.fasterxml.jackson.databind.PropertyNamingStrategy

class CamelCaseToUppperCaseWithUnderscores: PropertyNamingStrategy.PropertyNamingStrategyBase() {
    override fun translate(propertyName: String?): String? {
        if (propertyName == null) return propertyName
        var length = propertyName.length
        var result = StringBuilder(length * 2)
        var resultLength = 0
        for (i in 0 until length) {
            var c = propertyName[i]
            if (i > 0 || c != '_') {
                if (Character.isUpperCase(c)) {
                    if (resultLength > 0 && result[resultLength - 1] != '_') {
                        result.append('_')
                        resultLength++
                    }
                }

                result.append(c.toUpperCase())
                resultLength++
            }
        }

        return if (resultLength > 0) result.toString()
        else propertyName
    }
}