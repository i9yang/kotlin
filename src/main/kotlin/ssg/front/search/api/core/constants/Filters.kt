package ssg.front.search.api.core.constants

import ssg.front.search.api.dto.Parameter

enum class Filters {
    PRC_FILTER {
        override fun getFilter(parameter: Parameter): String {
            val strMinPrc = parameter.minPrc ?: "-1"
            val strMaxPrc = parameter.maxPrc ?: "-1"
            // 숫자로 변환되는 경우에만 보냄
            var minprc: Int
            var maxprc: Int

            try {
                minprc = Integer.parseInt(strMinPrc)
                maxprc = Integer.parseInt(strMaxPrc)
            } catch (ne: NumberFormatException) {
                minprc = -1
                maxprc = -1
            }

            val fsb = StringBuilder()
            if (minprc > -1 && maxprc > -1) {
                fsb.append("<SELLPRC:gte:").append(strMinPrc).append("><SELLPRC:lte:").append(strMaxPrc).append(">")
            } else if (minprc > -1) {
                fsb.append("<SELLPRC:gte:").append(strMinPrc).append(">")
            } else if (maxprc > -1) {
                fsb.append("<SELLPRC:lte:").append(strMaxPrc).append(">")
            }
            return fsb.toString()
        }
    };

    abstract fun getFilter(parameter: Parameter): String
}
