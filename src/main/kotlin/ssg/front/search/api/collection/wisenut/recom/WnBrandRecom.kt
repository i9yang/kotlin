package ssg.front.search.api.collection.wisenut.recom

import QueryAPI510.Search
import org.slf4j.LoggerFactory
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.function.Pageable
import ssg.search.result.Brand
import java.util.*
import java.util.regex.Pattern

class WnBrandRecom : WnCollection(), Pageable {
    private val logger = LoggerFactory.getLogger(WnBrandRecom::class.java)

    override fun getName(parameter: Parameter): String {
        return "brandrecom"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "brandrecom"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("BRAND_IDS", "BRAND_NMS")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("QUERY")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val target = parameter.target
        val sort = parameter.sort ?: "best"
        var brandResultList = result.brandList
        // 가나다순 정렬 요청시
        // 한글 -> 영문 -> 특수문자 순으로 정렬요청
        if ((target.equals("mobile_dtl_brand", ignoreCase = true) || target.equals("mobile_brand_omni_dtl", ignoreCase = true)) && sort.equals("word", ignoreCase = true)) {
            if (brandResultList != null && brandResultList!!.size > 0) {
                val engList = arrayListOf<Brand>()
                val korList = arrayListOf<Brand>()
                val spList = arrayListOf<Brand>()
                val resultList = arrayListOf<Brand>()
                // 한글/영문/그외 별로 따로 담는다.
                for (b in brandResultList!!) {
                    if (b.brandNm.isNotBlank()) {
                        val brandNm1 = b.getBrandNm().substring(0, 1)
                        if (Pattern.matches("^[ㄱ-ㅎ가-힣]*$", brandNm1)) {
                            korList.add(b)
                        } else if (Pattern.matches("^[a-zA-Z]*$", brandNm1)) {
                            engList.add(b)
                        } else {
                            spList.add(b)
                        }
                    }
                }
                // Comparator
                val brandComparator = Comparator<Brand> { o1, o2 -> o1.brandNm.compareTo(o2.brandNm) }

                if (korList != null && korList.size > 0) {
                    Collections.sort(korList, brandComparator)
                    resultList.addAll(korList)
                }
                if (engList != null && engList.size > 0) {
                    Collections.sort(engList, brandComparator)
                    resultList.addAll(engList)
                }
                if (spList != null && spList.size > 0) {
                    Collections.sort(spList, brandComparator)
                    resultList.addAll(spList)
                }
                result.brandList = resultList
            }
        } else if (search.w3GetResultTotalCount(name) > 0) {
            // Brand Result 를 추천결과와 비교하기 위해 Map에 결과를 담는다.
            val newBrandList = ArrayList<Brand>()
            val brandIdMap = HashMap<String, Brand>()
            if (brandResultList != null && brandResultList!!.size > 0) {
                for (brand in brandResultList!!) {
                    brandIdMap[brand.getBrandId()] = brand
                }
            }
            val brandIds = search.w3GetField(name, "BRAND_IDS", 0)
            // 브랜드가 1개 이상
            if (brandIds.indexOf(",") > -1) {
                val arrBrandId = brandIds.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in arrBrandId.indices) {
                    // 추천결과에 나온 브랜드가 Brand 그룹핑 결과에 존재하는 경우
                    // 새로운 브랜드 그룹핑을 시작
                    if (brandIdMap.containsKey(arrBrandId[i])) {
                        val brand = brandIdMap[arrBrandId[i]]
                        if (i < 3) {
                            brand!!.setRecomYn("Y")
                        }
                        newBrandList.add(brand!!)
                        // 나중에 Merge 를 위해 신규로 추가한 브랜드는 원본에서 삭제한다.
                        for (j in brandResultList!!.indices) {
                            if (arrBrandId[i] == brandResultList!!.get(j).getBrandId()) {
                                brandResultList!!.drop(j)
                            }
                        }
                    }
                }
            } else {
                if (brandIdMap.containsKey(brandIds)) {
                    val brand = brandIdMap[brandIds]
                    brand!!.setRecomYn("Y")
                    newBrandList.add(brand)
                    // 나중에 Merge 를 위해 신규로 추가한 브랜드는 원본에서 삭제한다.
                    for (j in brandResultList!!.indices) {
                        if (brandIds == brandResultList!!.get(j).getBrandId()) {
                            brandResultList!!.drop(j)
                        }
                    }
                }
            }
            if (newBrandList.size > 0) {
                newBrandList.addAll(newBrandList.size, brandResultList!!)
                result.brandList = newBrandList
            }
        }
        return result
    }

    override fun pageVo(parameter: Parameter): PageVo {
        return PageVo(0, 1)
    }
}
