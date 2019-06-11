package ssg.front.search.api.core.constants

import ssg.front.search.api.builder.*
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.util.BeanUtil
import ssg.front.search.api.util.CollectionUtils

/**
 * 빌더가 1개인 경우 ex ) WN, ES, AD ...
 * 빌더가 1개이상인 경우 알파벳순, 언더스코어로 구분한다. ex ) AD_ES, ES_RC_WN, AD_ES_WN ...
 * 빌더를 선택하는데 있어 분기가 존재하는 경우 네이밍을 별도로 짓는다. MOBILE, PNSHOP
 */
enum class TargetGroup {
    AD {
        override fun getQueryBuilders(parameter: Parameter): List<SsgQueryBuilder> {
            return listOf(
                    BeanUtil.getBean(AdvertisingQueryBuilder::class.java)
            )
        }
    },
    ES {
        override fun getQueryBuilders(parameter: Parameter): List<SsgQueryBuilder> {
            return listOf(
                    BeanUtil.getBean(EsQueryBuilder::class.java)
            )
        }
    },
    RC {
        override fun getQueryBuilders(parameter: Parameter): List<SsgQueryBuilder> {
            return listOf(
                    BeanUtil.getBean(RecommendQueryBuilder::class.java)
                    )
        }
    },
    WN {
        override fun getQueryBuilders(parameter: Parameter): List<SsgQueryBuilder> {
            return listOf(
                    WisenutQueryBuilder()
            )
        }
    },
    RC_WN {
        override fun getQueryBuilders(parameter: Parameter): List<SsgQueryBuilder> {
            return listOf(
                    BeanUtil.getBean(RecommendQueryBuilder::class.java),
                    WisenutQueryBuilder()
            )
        }
    },
    WN_ES {
        override fun getQueryBuilders(parameter: Parameter): List<SsgQueryBuilder> {
            return listOf(
                    WisenutQueryBuilder(),
                    BeanUtil.getBean(EsQueryBuilder::class.java)
            )
        }
    },
    AD_RC_WN {
        override fun getQueryBuilders(parameter: Parameter): List<SsgQueryBuilder> {
            return listOf(
                    BeanUtil.getBean(AdvertisingQueryBuilder::class.java),
                    BeanUtil.getBean(RecommendQueryBuilder::class.java),
                    WisenutQueryBuilder()
            )
        }
    },
    MOBILE {
        override fun getQueryBuilders(parameter: Parameter): List<SsgQueryBuilder> {
            if (parameter.aplTgtMediaCd != null && parameter.aplTgtMediaCd == "20") {
                val isAdSearch = CollectionUtils.isAdSearch(parameter)

                return if (isAdSearch) {
                    listOf(
                            BeanUtil.getBean(AdvertisingQueryBuilder::class.java),
                            BeanUtil.getBean(RecommendQueryBuilder::class.java),
                            WisenutQueryBuilder()
                    )
                } else {
                    listOf(
                            BeanUtil.getBean(RecommendQueryBuilder::class.java),
                            WisenutQueryBuilder()
                    )
                }
            }
            return listOf(
                    WiseQueryBuilder()
            )
        }
    },
    PNSHOP {
        override fun getQueryBuilders(parameter: Parameter): List<SsgQueryBuilder> {
            if (parameter.siteNo != null && "6004,6009".contains(parameter.siteNo) &&
                    parameter.aplTgtMediaCd != null && parameter.aplTgtMediaCd == "20") {
                return listOf(
                        WisenutQueryBuilder()
                )
            }
            return listOf(
                    WiseQueryBuilder()
            )
        }
    },
    NONE {
        override fun getQueryBuilders(parameter: Parameter): List<SsgQueryBuilder> {
            return listOf()
        }
    }
    ;

    abstract fun getQueryBuilders(parameter: Parameter): List<SsgQueryBuilder>
}