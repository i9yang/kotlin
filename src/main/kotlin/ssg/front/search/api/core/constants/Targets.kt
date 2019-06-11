package ssg.front.search.api.core.constants

import com.google.common.collect.Lists
import ssg.front.search.api.collection.ad.AdBanr
import ssg.front.search.api.collection.ad.AdCpc
import ssg.front.search.api.collection.ad.AdCpcExt
import ssg.front.search.api.collection.es.book.EsBookItem
import ssg.front.search.api.collection.es.book.EsBookMallCount
import ssg.front.search.api.collection.es.brand.EsBrandBookCtgIdGroup
import ssg.front.search.api.collection.es.brand.EsBrandCtgIdGroup
import ssg.front.search.api.collection.es.bshop.EsBshopCtgIdGroup
import ssg.front.search.api.collection.es.bshop.EsBshopItem
import ssg.front.search.api.collection.es.bundle.EsBundleBookCtgIdGroup
import ssg.front.search.api.collection.es.bundle.EsBundleCtgIdGroup
import ssg.front.search.api.collection.es.common.*
import ssg.front.search.api.collection.es.global.EsGlobalBrandIdGroup
import ssg.front.search.api.collection.es.global.EsGlobalCtgIdGroup
import ssg.front.search.api.collection.es.global.EsGlobalItem
import ssg.front.search.api.collection.rsearch.recom.MyTaste
import ssg.front.search.api.collection.rsearch.recom.PublicTaste
import ssg.front.search.api.collection.rsearch.search.EsBanr
import ssg.front.search.api.collection.rsearch.search.EsSpell
import ssg.front.search.api.collection.rsearch.search.EsSrchwdrl
import ssg.front.search.api.collection.rsearch.search.EsVirtualCategory
import ssg.front.search.api.collection.wisenut.brand.WnBookBrandDisp
import ssg.front.search.api.collection.wisenut.brand.WnBrandDisp
import ssg.front.search.api.collection.wisenut.brand.WnBrandMaster
import ssg.front.search.api.collection.wisenut.brand.WnMobileBrandDisp
import ssg.front.search.api.collection.wisenut.bshop.*
import ssg.front.search.api.collection.wisenut.contents.*
import ssg.front.search.api.collection.wisenut.disp.*
import ssg.front.search.api.collection.wisenut.global.*
import ssg.front.search.api.collection.wisenut.quality.WnSearchQuality
import ssg.front.search.api.collection.wisenut.recom.WnBrandRecom
import ssg.front.search.api.collection.wisenut.recom.WnCategoryRecom
import ssg.front.search.api.collection.wisenut.search.*
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.Starfield


enum class Targets(val targetGroup: TargetGroup) {
    // 임시 추가
    ALL(TargetGroup.AD_RC_WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            val strSiteNo = parameter.siteNo

            // 검색광고관련추가
            val isAdSearch = CollectionUtils.isAdSearch(parameter)
            parameter.isAdSearch = isAdSearch

            //상시배너조회추가
            parameter.isBanrEverSearch = true

            if (strSiteNo == "6100") {
                var collectionList = arrayListOf(
                        WnItem(),
                        WnItemGroup(),
                        WnItemCategory(),
                        WnLifeMagazine(),
                        EsBanr()
                )

                if (parameter.LOAD_LEVEL_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_LIMIT == parameter.loadLevel
                        || parameter.LOAD_LEVEL_EXCEPT_AD_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT == parameter.loadLevel) {
                    collectionList.clear()

                    collectionList = arrayListOf(
                            WnItem(),
                            WnItemGroup(),
                            WnItemCategory()
                    )
                }

                return collectionList

            } else if (strSiteNo == "6001" || strSiteNo == "6002") {
                var collectionList = arrayListOf(
                        WnCategoryRecom(),
                        WnItem(),
                        WnItemGroup(),
                        WnItemGroupCategory(),
                        WnBrandRecom(),
                        WnBrandRecom(),
                        WnMall(),
                        WnPostng(),
                        EsSrchwdrl(),
                        WnEvent(),
                        EsSpell(),
                        EsBanr(),
                        WnPnshop(),
                        WnTrecipe(),
                        EsVirtualCategory(),
                        WnFaq(),
                        WnBook(),
                        WnRecom(),
                        WnFamSite()
                )

                // 내취타취추가
                val isRecommendSearch = CollectionUtils.isRecommendSearch(parameter)
                if (isRecommendSearch) {
                    if (parameter.userInfo.mbrId!!.isNotBlank()) {
                        collectionList.add(MyTaste())
                    }

                    collectionList.add(PublicTaste())
                }

                if (parameter.LOAD_LEVEL_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_LIMIT == parameter.loadLevel
                        || parameter.LOAD_LEVEL_EXCEPT_AD_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT == parameter.loadLevel) {
                    collectionList.clear()

                    collectionList = arrayListOf(
                            WnItem(),
                            WnItemGroup(),
                            WnItemCategory(),
                            WnMall(),
                            WnBook()
                    )
                }

                // 검색광고관련추가
                if (isAdSearch && strSiteNo == "6001" && parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL != parameter.loadLevel
                        && parameter.LOAD_LEVEL_EXCEPT_AD_WARNING != parameter.loadLevel && parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT != parameter.loadLevel) {
                    collectionList.add(0, AdCpc())
                    collectionList.add(AdBanr())
                }

                return collectionList

            } else if (strSiteNo == "6004") {
                var collectionList = arrayListOf(
                        WnCategoryRecom(),
                        WnItem(),
                        WnItemGroup(),
                        WnItemGroupCategory(),
                        WnBrandRecom(),
                        WnMall(),
                        WnPostng(),
                        EsSrchwdrl(),
                        WnEvent(),
                        EsSpell(),
                        EsBanr(),
                        WnPnshop(),
                        WnPnshopSd(),
                        EsVirtualCategory(),
                        WnFaq(),
                        WnBook(),
                        WnRecom(),
                        WnFamSite()
                )

                // 내취타취추가
                val isRecommendSearch = CollectionUtils.isRecommendSearch(parameter)
                if (isRecommendSearch) {
                    if (parameter.userInfo.mbrId!!.isNotBlank()) {
                        collectionList.add(MyTaste())
                    }

                    collectionList.add(PublicTaste())
                }

                if (parameter.LOAD_LEVEL_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_LIMIT == parameter.loadLevel
                        || parameter.LOAD_LEVEL_EXCEPT_AD_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT == parameter.loadLevel) {
                    collectionList.clear()

                    collectionList = arrayListOf(
                            WnItem(),
                            WnItemGroup(),
                            WnItemCategory(),
                            WnMall(),
                            WnBook()
                    )
                }

                // 검색광고관련추가
                if (isAdSearch && parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL != parameter.loadLevel
                        && parameter.LOAD_LEVEL_EXCEPT_AD_WARNING != parameter.loadLevel && parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT != parameter.loadLevel) {
                    collectionList.add(0, AdCpc())
                    collectionList.add(AdBanr())
                }

                return collectionList

            } else if (strSiteNo == "6009") {
                var collectionList = arrayListOf(
                        WnCategoryRecom(),
                        WnItem(),
                        WnItemGroup(),
                        WnItemGroupCategory(),
                        WnBrandRecom(),
                        WnMall(),
                        WnPostng(),
                        EsSrchwdrl(),
                        WnEvent(),
                        EsSpell(),
                        EsBanr(),
                        WnPnshop(),
                        EsVirtualCategory(),
                        WnFaq(),
                        WnRecom(),
                        WnFamSite()
                )

                // 내취타취추가
                val isRecommendSearch = CollectionUtils.isRecommendSearch(parameter)
                if (isRecommendSearch) {
                    if (parameter.userInfo.mbrId!!.isNotBlank()) {
                        collectionList.add(MyTaste())
                    }

                    collectionList.add(PublicTaste())
                }

                if (parameter.LOAD_LEVEL_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_LIMIT == parameter.loadLevel
                        || parameter.LOAD_LEVEL_EXCEPT_AD_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT == parameter.loadLevel) {
                    collectionList.clear()

                    collectionList = arrayListOf(
                            WnItem(),
                            WnItemGroup(),
                            WnItemGroupCategory(),
                            WnMall()
                    )
                }

                // 검색광고관련추가
                if (isAdSearch && parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL != parameter.loadLevel
                        && parameter.LOAD_LEVEL_EXCEPT_AD_WARNING != parameter.loadLevel && parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT != parameter.loadLevel) {
                    collectionList.add(AdBanr())
                }

                return collectionList

            } else if (strSiteNo == "6005") {
                var collectionList = arrayListOf(
                        WnCategoryRecom(),
                        WnItem(),
                        WnItemGroup(),
                        WnItemGroupCategory(),
                        WnBrandRecom(),
                        WnMall(),
                        WnPostng(),
                        WnLifeMagazine(),
                        WnEvent(),
                        WnIssueTheme(),
                        EsSrchwdrl(),
                        EsSpell(),
                        EsBanr(),
                        WnPnshop(),
                        WnPnshopSd(),
                        WnTrecipe(),
                        EsVirtualCategory(),
                        WnFaq(),
                        WnBook(),
                        WnRecom(),
                        WnStarfield(),
                        WnFamSite()
                )

                // 내취타취추가
                val isRecommendSearch = CollectionUtils.isRecommendSearch(parameter)
                if (isRecommendSearch) {
                    if (parameter.userInfo.mbrId!!.isNotBlank()) {
                        collectionList.add(MyTaste())
                    }

                    collectionList.add(PublicTaste())
                }

                if (parameter.LOAD_LEVEL_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_LIMIT == parameter.loadLevel
                        || parameter.LOAD_LEVEL_EXCEPT_AD_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT == parameter.loadLevel) {
                    collectionList.clear()

                    collectionList = arrayListOf(
                            WnItem(),
                            WnItemGroup(),
                            WnItemGroupCategory(),
                            WnMall(),
                            WnBook()
                    )
                }

                // 검색광고관련추가
                if (isAdSearch && parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL != parameter.loadLevel
                        && parameter.LOAD_LEVEL_EXCEPT_AD_WARNING != parameter.loadLevel && parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT != parameter.loadLevel) {
                    collectionList.add(0, AdCpc())
                    collectionList.add(AdBanr())
                }

                return collectionList
            } else if (strSiteNo == "6200" || strSiteNo == "6003" || strSiteNo == "6300") {
                var collectionList = arrayListOf(
                        WnCategoryRecom(),
                        WnItem(),
                        WnItemGroup(),
                        WnItemGroupCategory(),
                        WnEvent(),
                        WnBrandRecom(),
                        WnMall(),
                        WnPostng(),
                        EsSpell(),
                        EsBanr(),
                        WnPnshop(),
                        EsVirtualCategory(),
                        WnFaq(),
                        WnRecom()
                )

                if (parameter.LOAD_LEVEL_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_LIMIT == parameter.loadLevel
                        || parameter.LOAD_LEVEL_EXCEPT_AD_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT == parameter.loadLevel) {
                    collectionList.clear()

                    collectionList = arrayListOf(
                            WnItem(),
                            WnItemGroup(),
                            WnItemGroupCategory(),
                            WnMall()
                    )
                }

                return collectionList
            } else if (strSiteNo == "6400") {
                var collectionList = arrayListOf(
                        EsSpell(),
                        EsBanr(),
                        WnStarfield()
                )

                if (parameter.LOAD_LEVEL_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_LIMIT == parameter.loadLevel
                        || parameter.LOAD_LEVEL_EXCEPT_AD_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT == parameter.loadLevel) {
                    collectionList.clear()
                    collectionList = arrayListOf(
                            WnStarfield()
                    )
                }

                return collectionList
            }

            return listOf(
                    WnCategoryRecom(),
                    WnItem(),
                    WnItemGroup(),
                    WnItemGroupCategory(),
                    WnBrandRecom(),
                    WnMall(),
                    WnPostng(),
                    WnLifeMagazine(),
                    WnEvent(),
                    WnIssueTheme(),
                    EsSrchwdrl(),
                    EsSpell(),
                    EsBanr(),
                    WnPnshop(),
                    WnPnshopSd(),
                    EsVirtualCategory(),
                    WnFaq(),
                    WnBook(),
                    WnRecom()
            )
        }
    },
    ITEM(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            val isAdSearch = CollectionUtils.isAdSearch(parameter)
            parameter.isAdSearch = isAdSearch

            val collectionList = arrayListOf<Any>(
                    WnItem()
            )

            // 검색광고관련추가
            if (isAdSearch && parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL != parameter.loadLevel
                    && parameter.LOAD_LEVEL_EXCEPT_AD_WARNING != parameter.loadLevel
                    && parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT != parameter.loadLevel
            ) {
                collectionList.add(0, AdCpc())
            }

            return collectionList
        }
    },
    CATEGORY(TargetGroup.NONE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            val strSiteNo = parameter.siteNo

            // 검색광고관련추가
            //원래 카테고리는 광고상품을 노출하지 않지만 pc에서 뒤로 가기 할경우 타겟을 카테고리로 던지는 경우 때문에 추가
            var isAdSearch = false

            if (parameter.ctgId!!.isNotBlank()) {
                isAdSearch = CollectionUtils.isAdSearch(parameter)
                parameter.isAdSearch = isAdSearch
            }

            if (strSiteNo == "6100") {

                return arrayListOf(
                        WnItem(),
                        WnItemGroup(),
                        WnItemCategory(),
                        WnLifeMagazine()
                )
            } else {
                val collectionList = arrayListOf<Any>(
                        WnCategoryRecom(),
                        WnItem(),
                        WnItemGroup(),
                        WnItemGroupCategory(),
                        WnBrandRecom()
                )

                // 검색광고관련추가
                if (isAdSearch && parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL != parameter.loadLevel
                        && parameter.LOAD_LEVEL_EXCEPT_AD_WARNING != parameter.loadLevel && parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT != parameter.loadLevel) {
                    collectionList.add(0, AdCpc())
                    collectionList.add(AdBanr())
                }

                return collectionList
            }
        }
    },
    BOOK(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnBook(),
                    WnBookCategory(),
                    WnBookGroup(),
                    WnItemMall(),
                    EsSrchwdrl(),
                    EsSpell()
            )
        }
    },
    //2019년 1월에 강업후 삭제예정
    MOBILE_ALL(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            // 검색광고관련추가
            val isAdSearch = CollectionUtils.isAdSearch(parameter)
            parameter.isAdSearch = isAdSearch

            //상시배너조회추가
            parameter.isBanrEverSearch = true

            val strSiteNo = parameter.siteNo

            // 스타필드 조회시에는 바로 리턴
            if (strSiteNo == "6400") {
                var returnList= arrayListOf(
                        EsSpell(),
                        EsBanr(),
                        WnStarfield()
                )

                if (parameter.LOAD_LEVEL_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_LIMIT == parameter.loadLevel
                        || parameter.LOAD_LEVEL_EXCEPT_AD_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT == parameter.loadLevel) {
                    returnList.clear()
                    returnList = arrayListOf(
                            WnStarfield()
                    )
                }

                return returnList
            }

            var returnList = arrayListOf(
                    WnItem(),
                    WnItemGroup(),
                    WnMall(),
                    EsSrchwdrl(),
                    EsSpell(),
                    EsBanr(),
                    WnStore(),
                    WnMobilePnshop(),
                    WnLifeMagazine(),
                    WnIssueTheme(),
                    WnBrandMaster()
            )

            if (strSiteNo == "6001" || strSiteNo == "6004" || strSiteNo == "6005") {
                returnList.add(WnBook())

            }
            if (strSiteNo == "6001" || strSiteNo == "6005") {
                returnList.add(WnTrecipe())
            }
            if (strSiteNo == "6001" || strSiteNo == "6004" || strSiteNo == "6009" || strSiteNo == "6005") {
                returnList.add(EsVirtualCategory())
            }
            if (strSiteNo == "6005") {
                returnList.add(Starfield())
            }

            if (parameter.LOAD_LEVEL_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_LIMIT == parameter.loadLevel
                    || parameter.LOAD_LEVEL_EXCEPT_AD_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT == parameter.loadLevel) {
                returnList.clear()

                returnList = arrayListOf(
                        WnItem(),
                        WnMall()
                )
            }

            // 검색광고관련추가
            if (isAdSearch && parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL != parameter.loadLevel
                    && parameter.LOAD_LEVEL_EXCEPT_AD_WARNING != parameter.loadLevel && parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT != parameter.loadLevel) {
                //상품
                if (strSiteNo == "6001" || strSiteNo == "6004" || strSiteNo == "6005") {
                    returnList.add(0, AdCpc())
                }

                //배너
                if (strSiteNo == "6001" || strSiteNo == "6004" || strSiteNo == "6009" || strSiteNo == "6005") {
                    returnList.add(AdBanr())
                }
            }

            return returnList
        }
    },
    MOBILE(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            // 검색광고관련추가
            val isAdSearch = CollectionUtils.isAdSearch(parameter)
            parameter.isAdSearch = isAdSearch

            //상시배너조회추가
            parameter.isBanrEverSearch = true

            val strSiteNo = parameter.siteNo ?: "6005"
            var returnList= arrayListOf<Any>()

            //스타필드 조회시에는 바로 리턴
            if (strSiteNo == "6400") {
                returnList = arrayListOf(
                        EsSpell(),
                        EsBanr(),
                        WnStarfield()
                )

                if (parameter.LOAD_LEVEL_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_LIMIT == parameter.loadLevel
                        || parameter.LOAD_LEVEL_EXCEPT_AD_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT == parameter.loadLevel) {
                    returnList.clear()
                    returnList = arrayListOf(
                            WnStarfield()
                    )
                }
                return returnList
            }

            returnList = arrayListOf(
                    WnItem(),
                    WnItemCategory(),
                    WnItemGroup(),
                    WnItemMall(),
                    EsSrchwdrl(),
                    EsSpell(),
                    EsBanr(),
                    EsVirtualCategory()
            )
            if (strSiteNo == "6001") {
                returnList.add(WnBook())
                returnList.add(WnFamSite())
            }
            if (strSiteNo == "6004") {
                returnList.add(WnBook())
                returnList.add(WnPnshop())
                returnList.add(WnPnshopSd())
                returnList.add(WnFamSite())
            } else if (strSiteNo == "6005") {
                returnList.add(WnBook())
                returnList.add(WnPnshop())
                returnList.add(WnPnshopSd())
                returnList.add(WnFamSite())
            } else if (strSiteNo == "6009") {
                returnList.add(WnPnshopSd())
                returnList.add(WnFamSite())
            }

            if (parameter.LOAD_LEVEL_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_LIMIT == parameter.loadLevel
                    || parameter.LOAD_LEVEL_EXCEPT_AD_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT == parameter.loadLevel) {
                returnList.clear()

                returnList = arrayListOf(
                        WnItem(),
                        WnItemCategory(),
                        WnItemGroup(),
                        WnItemMall()
                )
            }

            // 검색광고관련추가
            if (isAdSearch && parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL != parameter.loadLevel
                    && parameter.LOAD_LEVEL_EXCEPT_AD_WARNING != parameter.loadLevel && parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT != parameter.loadLevel) {
                //상품
                if (strSiteNo == "6001" || strSiteNo == "6004" || strSiteNo == "6005") {
                    returnList.add(0, AdCpc())
                }

                //배너
                if (strSiteNo == "6001" || strSiteNo == "6004" || strSiteNo == "6009" || strSiteNo == "6005") {
                    returnList.add(AdBanr())
                }
            }

            return returnList
        }
    },
    //2019년 1월에 강업후 삭제예정
    MOBILE_DTL(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnCategoryRecom(),
                    WnItemCommCategory(),
                    WnItemGroup(),
                    WnItemMall(),
                    WnBrandRecom()
            )
        }
    },
    //변경예정 2019년 1월 강업후 삭제 MOBILE_BRAND_OMNI_DTL 
    MOBILE_DTL_BRAND(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnItemGroup(),
                    WnBrandRecom()
            )
        }
    },
    MOBILE_ITEM(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            val isAdSearch = CollectionUtils.isAdSearch(parameter)
            parameter.isAdSearch = isAdSearch

            val returnList = arrayListOf<Any>(
                    WnItem()
            )

            // 검색광고관련추가
            if (isAdSearch && parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL != parameter.loadLevel
                    && parameter.LOAD_LEVEL_EXCEPT_AD_WARNING != parameter.loadLevel && parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT != parameter.loadLevel) {
                returnList.add(0, AdCpc())
            }

            return returnList
        }
    },
    //2019년 1월에 강업후 삭제예정
    MOBILE_RECOM_ITEM(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return Lists.newArrayList()
        }
    },
    MOBILE_NORESULT(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnItemMall())
        }
    },
    //2019년 1월에 강업후 삭제예정
    MOBILE_BOOK(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnBook(),
                    WnBookCategory(),
                    WnBookGroup(),
                    WnItemMall(),
                    EsSrchwdrl(),
                    EsSpell()
            )
        }
    },
    MOBILE_BRAND_ALL(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnDisp(),
                    WnMobileBrandDisp(),
                    WnDispCommBrandGroup()
            )
        }
    },
    MOBILE_BRAND(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnDisp(),
                    WnMobileBrandDisp()
            )
        }
    },
    MOBILE_BRAND_ITEM(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnDisp())
        }
    },
    MOBILE_BRAND_DTL(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnMobileBrandDisp(),
                    WnDispCommBrandGroup()
            )
        }
    },
    //2019년 1월에 강업후 삭제예정
    MOBILE_PNSHOP(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnMobilePnshop())
        }
    },
    DISP(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            // 부하 LEVEL에 따라 getCollection 조절
            if (parameter.loadLevel == parameter.LOAD_LEVEL_WARNING || parameter.loadLevel == parameter.LOAD_LEVEL_EXCEPT_AD_WARNING) {
                return listOf(
                        WnDisp(),
                        WnDispItemGroup()
                )
            } else if (parameter.loadLevel == parameter.LOAD_LEVEL_LIMIT || parameter.loadLevel == parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT) {
                return listOf(
                        WnDisp()
                )
            }

            val strSiteNo = parameter.siteNo ?: "6005"
            val returnList = arrayListOf(
                    WnDisp(),
                    WnDispItemGroup(),
                    WnBookDisp()
            )
            if (strSiteNo == "6005") {
                returnList.add(WnDispMall())
                returnList.add(WnBookDispMall())
            }
            return returnList
        }
    },
    DISP_ITEM(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnDisp())
        }
    },
    DISP_DTL(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnDispCommItemGroup())
        }
    },
    //  페레가모 등의 특정 샵
    SHOP(TargetGroup.NONE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnItem())
        }
    },
    SPSHOP(TargetGroup.NONE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnItem())
        }
    },
    FAQ(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnFaq())
        }
    },
    RECIPE(TargetGroup.NONE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnRecipe())
        }
    },
    RECOM(TargetGroup.NONE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnRecom())
        }
    },
    PNSHOP(TargetGroup.PNSHOP) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            val strSiteNo = parameter.siteNo ?: "6005"
            val returnList = arrayListOf<Any>()
            if ("6004,6005".contains(strSiteNo)) {
                returnList.add(WnPnshop())
                returnList.add(WnPnshopSd())
            } else if (strSiteNo == "6009") {
                returnList.add(WnPnshopSd())
            }
            return returnList
        }
    },
    BRAND_DISP(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            if (parameter.loadLevel == parameter.LOAD_LEVEL_WARNING || parameter.loadLevel == parameter.LOAD_LEVEL_EXCEPT_AD_WARNING) {
                return listOf(
                        WnDisp()
                )
            } else if (parameter.loadLevel == parameter.LOAD_LEVEL_LIMIT || parameter.loadLevel == parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT) {
                return listOf(
                        WnDisp()
                )
            }
            val strSiteNo = parameter.siteNo ?: "6005"
            val returnList = arrayListOf(
                    WnDisp(),
                    WnBookDisp(),
                    WnBrandDisp(),
                    WnBookBrandDisp()
            )
            if (strSiteNo == "6005") {
                returnList.add(WnDispMall())
                returnList.add(WnBookDispMall())
            }
            return returnList
        }
    },
    PARTNER(TargetGroup.NONE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnItem())
        }
    },
    VIRTUAL(TargetGroup.NONE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnItem())
        }
    },
    GLOBAL(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnGlobalItemCategory(), WnGlobalGroupCategory(), WnGlobalItem())
        }
    },
    GLOBAL_ITEM(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnGlobalGroupCategory(), WnGlobalItem())
        }
    },
    GLOBAL_CATEGORY(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnGlobalDispItemCategory(), WnGlobalDispGroupCategory())
        }
    },
    GLOBAL_BRAND(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnGlobalDispItemBrand(), WnGlobalDispGroupBrand())
        }
    },
    BSHOP(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnBshopItemCategory(),
                    WnBshopItem(),
                    WnBshopGroup(),
                    WnBshop()
            )
        }
    },
    BSHOP_BRAND(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnBshop(), WnBshopGroup())
        }
    },
    BSHOP_ITEM(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnBshopItem())
        }
    },
    BSHOP_DISP_ITEM(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnBshopDispItem())
        }
    },
    BSHOP_DISP(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnBshopDispGroupCategory(),
                    WnBshopDispItem()
            )
        }
    },
    ISSUETHEME(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnIssueTheme())
        }
    },
    ES_DISP(TargetGroup.ES){
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            val strSiteNo = parameter.siteNo
            var returnList = mutableListOf(
                    EsItem(),
                    EsBookItem(),
                    EsSellPrcLstGroup(),
                    EsSizeLstGroup(),
                    EsBrandIdGroup()
            )

            if (strSiteNo == "6005") {
                returnList.add(EsMallCount())
                returnList.add(EsBookMallCount())
            }

            return returnList
        }
    },
    ES_DISP_ITEM(TargetGroup.ES) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(EsItem())
        }
    },
    ES_SPCSHOP(TargetGroup.ES) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            var esItem = EsItem()
            esItem.indexName = "spcshop"

            var esSizeLstGroup = EsSizeLstGroup()
            esSizeLstGroup.indexName = "spcshop"

            var esSellPrcLstGroup = EsSellPrcLstGroup()
            esSellPrcLstGroup.indexName = "spcshop"

            var esBrandIdGroup = EsBrandIdGroup()
            esBrandIdGroup.indexName = "spcshop"

            var esSellLstPrcGroup = EsSellPrcLstGroup()
            esSellLstPrcGroup.indexName = "spcshop"

            return mutableListOf(
                    esItem,
                    esSizeLstGroup,
                    esSellPrcLstGroup,
                    esBrandIdGroup,
                    esSellLstPrcGroup
            )
        }
    },
    ES_BRAND_DISP(TargetGroup.ES) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            var strSiteNo = parameter.siteNo

            var returnList = mutableListOf(
                    EsItem(),
                    EsBookItem(),
                    EsBrandCtgIdGroup(),
                    EsBrandBookCtgIdGroup(),
                    EsSellPrcLstGroup()
            )

            if (strSiteNo == "6005") {
                returnList.add(EsMallCount())
                returnList.add(EsBookMallCount())
            }

            return returnList;
        }
    },
    ES_GLOBAL_CATEGORY(TargetGroup.ES) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(EsGlobalItem(), EsGlobalBrandIdGroup())
        }
    },
    ES_GLOBAL_BRAND(TargetGroup.ES) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(EsGlobalItem(), EsGlobalCtgIdGroup())
        }
    },
    ES_BSHOP_DISP_ITEM(TargetGroup.ES) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(EsBshopItem())
        }
    },
    ES_BSHOP_DISP(TargetGroup.ES) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(EsBshopCtgIdGroup(), EsBshopItem())
        }
    },
    ES_BUNDLE(TargetGroup.ES){
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return mutableListOf(
                    EsItem(),
                    EsBookItem(),
                    EsBundleCtgIdGroup(),
                    EsBundleBookCtgIdGroup(),
                    EsBrandIdGroup(),
                    EsSellPrcLstGroup()
            )
        }
    },
    SRCH_QUAL_ITEM(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnSearchQuality())
        }
    },
    //변경예정 2019년 1월 강업후 삭제 CHAT_GIFT_OMNI_ALL 
    CHAT_GIFT_ALL(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnItem(),
                    WnItemGroup(),
                    EsSrchwdrl(),
                    EsSpell(),
                    WnCategoryRecom(),
                    EsVirtualCategory(),
                    WnBook()
            )
        }
    },
    //변경예정 2019년 1월 강업후 삭제 CHAT_GIFT_OMNI_ITEM
    CHAT_GIFT_ITEM(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnItem(),
                    WnBook()
            )
        }
    },
    //변경예정 2019년 1월 강업후 삭제 CHAT_GIFT_OMNI_DTL
    CHAT_GIFT_DTL(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnCategoryRecom(),
                    WnItemCommCategory(),
                    WnItemGroup(),
                    WnItemMall(),
                    WnBrandRecom()
            )
        }
    },
    //변경예정 2019년 1월 강업후 삭제 CHAT_OMNI_ALL 
    CHAT_SEARCH_ALL(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnItem(),
                    WnItemGroup(),
                    EsSrchwdrl(),
                    EsSpell(),
                    WnMobilePnshop(),
                    WnLifeMagazine(),
                    WnIssueTheme(),
                    WnTrecipe(),
                    WnCategoryRecom(),
                    EsVirtualCategory(),
                    WnBook()
            )
        }
    },
    //변경예정 2019년 1월 강업후 삭제 CHAT_OMNI_ITEM
    CHAT_SEARCH_ITEM(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnItem(),
                    WnBook()
            )
        }
    },
    //변경예정 2019년 1월 강업후 삭제 CHAT_OMNI_DTL
    CHAT_SEARCH_DTL(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnCategoryRecom(),
                    WnItemCommCategory(),
                    WnItemGroup(),
                    WnItemMall(),
                    WnBrandRecom()
            )
        }
    },
    CHAT_VEN_ITEMS(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnItem(),
                    WnItemCommCategory()
            )
        }
    },
    CHAT_OMNI_ALL(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            val returnList = arrayListOf(
                    WnItem(),
                    WnItemGroup(),
                    EsSrchwdrl(),
                    EsSpell(),
                    WnMobilePnshop(),
                    WnLifeMagazine(),
                    WnTrecipe(),
                    EsVirtualCategory(),
                    WnBook(),
                    WnBrandMaster(),
                    WnStarfield()
            )

            // 내취타취추가
            val isRecommendSearch = CollectionUtils.isRecommendSearch(parameter)
            if (isRecommendSearch) {
                if (parameter.userInfo.mbrId!!.isNotBlank()) {
                    returnList.add(MyTaste())
                }
                returnList.add(PublicTaste())
            }

            return returnList
        }
    },
    CHAT_GIFT_OMNI_ALL(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnItem(),
                    WnItemGroup(),
                    WnBook()
            )
        }
    },
    CHAT_OMNI_ITEM(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnItem(),
                    WnBook()
            )
        }
    },
    CHAT_GIFT_OMNI_ITEM(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnItem(),
                    WnBook()
            )
        }
    },
    CHAT_OMNI_DTL(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnCategoryRecom(),
                    WnItemCommCategory(),
                    WnItemGroup(),
                    WnItemMall(),
                    WnBrandRecom()
            )
        }
    },
    CHAT_GIFT_OMNI_DTL(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnCategoryRecom(),
                    WnItemCommCategory(),
                    WnItemGroup(),
                    WnItemMall(),
                    WnBrandRecom()
            )
        }
    },
    // CHAT과 GIFT관련 신규 추가 끝
    LIFEMAGAZINE(TargetGroup.NONE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnLifeMagazine()
            )
        }
    },
    TRECIPE(TargetGroup.NONE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnTrecipe()
            )
        }
    },
    TASTE(TargetGroup.RC){
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            val userInfo = parameter.userInfo
            return if (userInfo.mbrId!!.isNotBlank()) {
                listOf(
                        MyTaste(),
                        PublicTaste()
                )
            } else listOf(
                    PublicTaste()
            )
        }
    },
    BRANDMASTER(TargetGroup.WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnBrandMaster())
        }
    },
    MOBILE_TASTE(TargetGroup.RC_WN) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            val collectionList = arrayListOf<Any>(
                    EsVirtualCategory()
            )

            // 내취타취추가
            val isRecommendSearch = CollectionUtils.isRecommendSearch(parameter)

            if (isRecommendSearch) {
                if (parameter.userInfo.mbrId!!.isNotBlank()) {
                    collectionList.add(MyTaste())
                }

                collectionList.add(PublicTaste())
            }

            return collectionList
        }
    },
    AD_CPC(TargetGroup.AD) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(AdCpc())
        }
    },
    AD_CPC_EXT(TargetGroup.AD) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(AdCpcExt())
        }
    },
    STARFIELD(TargetGroup.NONE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(WnStarfield())
        }
    },
    MOBILE_OMNI_ALL(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            // 검색광고관련추가
            val isAdSearch = CollectionUtils.isAdSearch(parameter)
            parameter.isAdSearch = isAdSearch

            //상시배너조회추가
            parameter.isBanrEverSearch = true

            val strSiteNo = parameter.siteNo ?: "6005"

            // 스타필드 조회시에는 바로 리턴
            if (strSiteNo == "6400") {
                var returnList: MutableList<Any> = arrayListOf(
                        EsSpell(),
                        EsBanr(),
                        WnStarfield()
                )

                if (parameter.LOAD_LEVEL_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_LIMIT == parameter.loadLevel
                        || parameter.LOAD_LEVEL_EXCEPT_AD_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT == parameter.loadLevel) {
                    returnList.clear()
                    returnList = arrayListOf(
                            WnStarfield()
                    )
                }

                return returnList
            }

            var returnList: MutableList<Any> = arrayListOf(
                    WnItem(),
                    WnItemGroup(),
                    WnMall(),
                    EsSpell(),
                    EsSrchwdrl(),
                    EsBanr(),
                    WnMobilePnshop(),
                    WnLifeMagazine(),
                    WnBrandMaster()
            )

            if (strSiteNo == "6001" || strSiteNo == "6004" || strSiteNo == "6005") {
                returnList.add(WnBook())
            }
            if (strSiteNo == "6001" || strSiteNo == "6005") {
                returnList.add(WnTrecipe())
            }
            if (strSiteNo == "6001" || strSiteNo == "6004" || strSiteNo == "6009" || strSiteNo == "6005") {
                returnList.add(EsVirtualCategory())
            }
            if (strSiteNo == "6005") {
                returnList.add(WnStarfield())
            }

            // 내취타취추가
            val isRecommendSearch = CollectionUtils.isRecommendSearch(parameter)
            if (isRecommendSearch) {
                if (parameter.userInfo.mbrId!!.isNotBlank()) {
                    returnList.add(MyTaste())
                }

                returnList.add(PublicTaste())
            }

            if (parameter.LOAD_LEVEL_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_LIMIT == parameter.loadLevel
                    || parameter.LOAD_LEVEL_EXCEPT_AD_WARNING == parameter.loadLevel || parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT == parameter.loadLevel) {
                returnList.clear()

                returnList = arrayListOf(
                        WnItem(),
                        WnMall()
                )
            }

            // 검색광고관련추가
            if (isAdSearch && parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL != parameter.loadLevel
                    && parameter.LOAD_LEVEL_EXCEPT_AD_WARNING != parameter.loadLevel && parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT != parameter.loadLevel) {
                //상품
                if (strSiteNo == "6001" || strSiteNo == "6004" || strSiteNo == "6005") {
                    returnList.add(0, AdCpc())
                }

                //배너
                if (strSiteNo == "6001" || strSiteNo == "6004" || strSiteNo == "6009" || strSiteNo == "6005") {
                    returnList.add(AdBanr())
                }
            }

            return returnList
        }
    },
    MOBILE_OMNI_ITEM(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            val isAdSearch = CollectionUtils.isAdSearch(parameter)
            parameter.isAdSearch = isAdSearch

            var returnList = arrayListOf<Any>(
                    WnItem()
            )

            // 검색광고관련추가
            if (isAdSearch && !parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL.equals(parameter.loadLevel)
                    && !parameter.LOAD_LEVEL_EXCEPT_AD_WARNING.equals(parameter.loadLevel) && !parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT.equals(parameter.loadLevel)) {
                returnList.add(0, AdCpc())
            }

            return returnList
        }
    },
    MOBILE_OMNI_DTL(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnCategoryRecom(),
                    WnItemCommCategory(),
                    WnItemGroup(),
                    WnItemMall(),
                    WnBrandRecom()
            )
        }
    },
    MOBILE_OMNI_BOOK(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnBook()
            )
        }
    },
    MOBILE_BRAND_OMNI_DTL(TargetGroup.MOBILE) {
        override fun getCollectionSet(parameter: Parameter): List<Any> {
            return listOf(
                    WnItemGroup(),
                    WnBrandRecom()
            )
        }
    },
    ;

    abstract fun getCollectionSet(parameter: Parameter): List<Any>

}