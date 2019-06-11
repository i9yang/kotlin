package ssg.front.search.api.util

import QueryAPI510.Search
import com.google.common.base.Splitter
import com.google.common.collect.Lists
import com.google.common.collect.Maps
import org.apache.commons.lang3.StringUtils
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Item
import ssg.front.search.api.dto.result.Result
import ssg.search.result.*
import java.util.*

object ResultUtils {
    fun getSiteGroup(siteInfo: String, scomInfo: String): Map<String, String> {
        val mallCountMap = Maps.newHashMap<String, String>()
        run {
            val iter = Splitter.on("@").trimResults().split(siteInfo).iterator()
            while (iter.hasNext()) {
                val mallIds = iter.next().split("\\^".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                mallCountMap[mallIds[0]] = mallIds[2]
            }
        }
        val iter = Splitter.on("@").trimResults().split(scomInfo).iterator()
        while (iter.hasNext()) {
            val mallIds = iter.next().split("\\^".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (mallIds[0] == "Y") {
                mallCountMap["6005"] = mallIds[2]
            }
        }
        return mallCountMap
    }

    fun getSizeGroup(sizeInfo: String): List<Size> {
        var size: Size
        val sizeList = Lists.newArrayList<Size>()
        val iter = Splitter.on("@").trimResults().split(sizeInfo).iterator()
        while (iter.hasNext()) {
            size = Size()
            val sizeIds = iter.next().split("\\^".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (sizeIds.size > 2) {
                size.setSizeCd(sizeIds[0])
                size.setSizeNm(sizeIds[1])
                size.setSizeCount(sizeIds[2])
                sizeList.add(size)
            }
        }
        Collections.sort<Size>(sizeList, Comparator<Size> { c1, c2 ->
            try {
                val k1 = Integer.parseInt(c1.sizeCd)
                val k2 = Integer.parseInt(c2.sizeCd)
                if (k1 < k2) {
                    return@Comparator -1
                } else return@Comparator if (k1 > k2) {
                    1
                } else {
                    0
                }
            } catch (ne: NumberFormatException) {
                return@Comparator 0
            }
        })
        return sizeList
    }

    fun getBrandGroup(brandInfo: String): List<Brand> {
        var brand: Brand? = null
        val brandList = Lists.newArrayList<Brand>()
        val iter = Splitter.on("@").trimResults().split(brandInfo).iterator()
        while (iter.hasNext()) {
            brand = Brand()
            val brandIds = iter.next().split("\\^".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (brandIds.size > 2) {
                brand!!.setBrandId(brandIds[0])
                brand!!.setBrandNm(brandIds[1].replace("\\++".toRegex(), " "))
                brand!!.setItemCount(brandIds[2])
                brandList.add(brand)
            }
        }
        return brandList
    }

    fun getGlobalCategoryGroup(siteNo: String, selectedCtgId: String?, ctgLst: String, target: String): List<Category> {
        val ctgList = ArrayList<Category>()
        val ctgList2 = ArrayList<Category>()
        val ctgList3 = ArrayList<Category>()
        var ctgLv2Id = ""
        var ctgLv3Id = ""
        var selectedCtgLv = ""
        val iter = Splitter.on("@").trimResults().split(ctgLst).iterator()
        while (iter.hasNext()) {
            val ctgElement = iter.next()
            if (ctgElement != null && ctgElement != "") {
                var i = 0
                val c = Category()
                var ctgType = ""
                var ctgCls = ""
                var ctgLv = ""
                var priorCtgId = ""
                val ctgToken = Splitter.on("^").trimResults().split(ctgElement).iterator()
                while (ctgToken.hasNext()) {
                    val ctg = ctgToken.next()
                    if (ctg != null && ctg != "") {
                        // CTG_ID
                        if (i == 0) {
                            c.setCtgId(ctg)
                        } else if (i == 1) {
                            val ctgNms = ctg.split("\\__".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (ctgNms.size >= 6) {
                                ctgLv = ctgNms[1]
                                c.setCtgNm(ctgNms[0].replace("\\++".toRegex(), " "))
                                c.setCtgLevel(Integer.parseInt(ctgLv))
                                c.setSiteNo(ctgNms[2])
                                c.setPriorCtgId(ctgNms[5])
                                priorCtgId = ctgNms[5]
                                ctgCls = ctgNms[3]
                                ctgType = ctgNms[4]
                            }
                        } else if (i == 2) {
                            c.setCtgItemCount(Integer.parseInt(ctg))
                        }// ITEM_COUNT
                        // CTGNM__CTGLV__SITE__CLS__TYPE__PRIOR_DISP_CTG_ID
                    }
                    if (i > 0 && i % 2 == 0) {
                        i = 0
                    } else
                        i++
                }
                // 현재 넘어온 카테고리 ID 를 기준으로 레벨과 ID를 추적한다.
                if (selectedCtgId != null && selectedCtgId != "" && selectedCtgId == c.getCtgId()) {
                    selectedCtgLv = ctgLv
                    // 선택된 카테고리가 중카
                    if (selectedCtgLv == "2") {
                        ctgLv2Id = c.getCtgId()
                    } else if (selectedCtgLv == "3") {
                        ctgLv2Id = c.getPriorCtgId()
                        ctgLv3Id = c.getCtgId()
                    }// 선택된 카테고리가 소카
                }
                if (selectedCtgLv == "2" && selectedCtgId == c.getCtgId()) {
                    c.setSelectedArea("selected")
                }
                // 해외직구관 카테고리만 ADD 한다.
                if (siteNo == c.getSiteNo()) {
                    if (ctgCls == "20" && ctgType == "10" && ctgLv == "2" && priorCtgId == "6000015276") {
                        ctgList2.add(c)
                    } else if (ctgCls == "20" && ctgType == "10" && ctgLv == "3") {
                        ctgList3.add(c)
                    }
                }
            }
        }
        val totalCount = 0
        val selectedPriorCtgId = ""
        if (ctgList3 != null && ctgList3.size > 0 && ctgList3 != null && ctgList3.size > 0) {
            // CATEGORY LEVEL3 SORT
            Collections.sort<Category>(ctgList3) { c1, c2 ->
                val p1 = c1.getPriorCtgId()
                val p2 = c2.getPriorCtgId()
                p1.compareTo(p2)
            }
            if (target == "global_brand") {
                for (index in ctgList2.indices) {
                    val c2 = ctgList2[index]
                    ctgList.add(c2)
                    for (c3 in ctgList3) {
                        if (c2.getCtgId() == c3.getPriorCtgId()) {
                            ctgList.add(c3)
                            if (selectedCtgId == c3.getCtgId()) {
                                c2.setSelectedArea("selected")
                            }
                        }
                    }
                }
            }
        }
        return ctgList
    }

    fun getBrandCategoryGroup(siteNo: String, selectedCtgId: String?, ctgLst: String): List<Category> {
        val ctgList = ArrayList<Category>()
        val ctgList1 = ArrayList<Category>()
        val ctgList2 = ArrayList<Category>()
        val ctgList3 = ArrayList<Category>()

        var ctgLv1 = 1
        var ctgLv2 = 2
        val ctgLv3 = 3

        if (siteNo == "6001") {
            ctgLv1 = 1
            ctgLv2 = 2
        }

        val iter = Splitter.on("@").trimResults().split(ctgLst).iterator()
        while (iter.hasNext()) {
            val ctgElement = iter.next()
            if (ctgElement != null && ctgElement != "") {
                var i = 0
                val c = Category()
                var ctgType = ""
                var ctgCls = ""
                var ctgLv = ""
                val ctgToken = Splitter.on("^").trimResults().split(ctgElement).iterator()
                while (ctgToken.hasNext()) {
                    val ctg = ctgToken.next()
                    if (ctg != null && ctg != "") {
                        // CTG_ID
                        if (i == 0) {
                            c.setCtgId(ctg)
                        } else if (i == 1) {
                            val ctgNms = ctg.split("\\__".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (ctgNms.size >= 6) {
                                ctgLv = ctgNms[1]
                                c.setCtgNm(ctgNms[0].replace("\\++".toRegex(), " "))
                                c.setCtgLevel(Integer.parseInt(ctgLv))
                                c.setSiteNo(ctgNms[2])
                                c.setPriorCtgId(ctgNms[5])
                                ctgCls = ctgNms[3]
                                ctgType = ctgNms[4]
                            }
                        } else if (i == 2) {
                            c.setCtgItemCount(Integer.parseInt(ctg))
                        }// ITEM_COUNT
                        // CTGNM__CTGLV__SITE__CLS__TYPE__PRIOR_DISP_CTG_ID
                    }
                    if (i > 0 && i % 2 == 0) {
                        i = 0
                    } else
                        i++
                }
                // 현재의 사이트와 동일할 경우에만 add
                if (siteNo == c.getSiteNo()) {
                    if (ctgCls == "10" && ctgType == "10" && ctgLv == ctgLv1.toString()) {
                        ctgList1.add(c)
                    } else if (ctgCls == "10" && ctgType == "10" && ctgLv == ctgLv2.toString()) {
                        ctgList2.add(c)
                    } else if (ctgCls == "10" && ctgType == "10" && ctgLv == ctgLv3.toString() && siteNo == "6003") {
                        ctgList3.add(c)
                    }
                }
            }
        }
        if (ctgList1 != null && ctgList1.size > 0 && ctgList2 != null && ctgList2.size > 0) {
            // CATEGORY LEVEL1 SORT
            Collections.sort<Category>(ctgList1) { c1, c2 ->
                val d1 = c1.getCtgId()
                val d2 = c2.getCtgId()
                d1.compareTo(d2)
            }

            // CATEGORY LEVEL2 SORT
            Collections.sort<Category>(ctgList2) { c1, c2 ->
                val p1 = c1.getPriorCtgId()
                val p2 = c2.getPriorCtgId()
                p1.compareTo(p2)
            }

            // CATEGORY LEVEL3 SORT
            Collections.sort<Category>(ctgList3) { c1, c2 ->
                val p1 = c1.getPriorCtgId()
                val p2 = c2.getPriorCtgId()
                p1.compareTo(p2)
            }

            var totalCount = 0
            var selectedPriorCtgId: String? = ""        // 선택된 상위 카테고리를 찾는다.
            for (c1 in ctgList1) {
                if (selectedCtgId != null && selectedCtgId != "" && c1.getCtgId() == selectedCtgId) {
                    c1.setSelectedArea("selected")
                }
                totalCount += c1.getCtgItemCount()
                ctgList.add(c1)
                for (c2 in ctgList2) {
                    if (c1.getCtgId() == c2.getPriorCtgId()) {
                        if (selectedCtgId != null && selectedCtgId != "" && c2.getCtgId() == selectedCtgId) {
                            selectedPriorCtgId = c2.getPriorCtgId()
                        }
                        ctgList.add(c2)
                        if (ctgList3 != null && ctgList3.size > 0) {
                            for (c3 in ctgList3) {
                                if (c2.getCtgId() == c3.getPriorCtgId()) {
                                    if (selectedCtgId != null && selectedCtgId != "" && c3.getCtgId() == selectedCtgId) {
                                        selectedPriorCtgId = c3.getPriorCtgId()
                                    }
                                    ctgList.add(c3)
                                }
                            }
                        }
                    }
                }
            }
            // 선택된 카테고리 찾기
            if (selectedPriorCtgId != null && selectedPriorCtgId != "") {
                for (i in ctgList.indices) {
                    val c = ctgList[i]
                    if (c.getCtgId() == selectedPriorCtgId) {
                        c.setSelectedArea("selected")
                        ctgList[i] = c
                    }
                }
            }
            // SSG의 경우에는 전체 넣어주기
            if (siteNo == "6005") {
                val totCtg = Category()
                totCtg.setCtgId("0000000000")
                totCtg.setPriorCtgId("0000000000")
                totCtg.setCtgLevel(1)
                totCtg.setCtgNm("전체")
                totCtg.setCtgItemCount(totalCount)
                ctgList.add(0, totCtg)
            }
        }
        return ctgList
    }

    fun getNewBrandCategoryGroup(siteNo: String, selectedCtgId: String?, ctgLst: String): List<Category> {
        val ctgList = ArrayList<Category>()
        val ctgList1 = ArrayList<Category>()
        val ctgList2 = ArrayList<Category>()

        val ctgLv1 = 1
        val ctgLv2 = 2

        val iter = Splitter.on("@").trimResults().split(ctgLst).iterator()
        while (iter.hasNext()) {
            val ctgElement = iter.next()
            if (ctgElement != null && ctgElement != "") {
                var i = 0
                val c = Category()
                var ctgType = ""
                var ctgCls = ""
                var ctgLv = ""
                val ctgToken = Splitter.on("^").trimResults().split(ctgElement).iterator()
                while (ctgToken.hasNext()) {
                    val ctg = ctgToken.next()
                    if (ctg != null && ctg != "") {
                        // CTG_ID
                        if (i == 0) {
                            c.setCtgId(ctg)
                        } else if (i == 1) {
                            val ctgNms = ctg.split("\\__".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (ctgNms.size >= 6) {
                                ctgLv = ctgNms[1]
                                c.setCtgNm(ctgNms[0].replace("\\++".toRegex(), " "))
                                c.setCtgLevel(Integer.parseInt(ctgLv))
                                c.setSiteNo(ctgNms[2])
                                c.setPriorCtgId(ctgNms[5])
                                ctgCls = ctgNms[3]
                                ctgType = ctgNms[4]
                            }
                        } else if (i == 2) {
                            c.setCtgItemCount(Integer.parseInt(ctg))
                        }// ITEM_COUNT
                        // CTGNM__CTGLV__SITE__CLS__TYPE__PRIOR_DISP_CTG_ID
                    }
                    if (i > 0 && i % 2 == 0) {
                        i = 0
                    } else
                        i++
                }
                // 현재의 사이트와 동일할 경우에만 add
                if (siteNo == c.getSiteNo()) {
                    if (ctgCls == "10" && ctgType == "10" && ctgLv == ctgLv1.toString()) {
                        ctgList1.add(c)
                    } else if (ctgCls == "10" && ctgType == "10" && ctgLv == ctgLv2.toString()) {
                        ctgList2.add(c)
                    }
                }
            }
        }
        if (ctgList1 != null && ctgList1.size > 0 && ctgList2 != null && ctgList2.size > 0) {
            // CATEGORY LEVEL1 SORT
            Collections.sort<Category>(ctgList1) { c1, c2 ->
                val d1 = c1.getCtgId()
                val d2 = c2.getCtgId()
                d1.compareTo(d2)
            }

            // CATEGORY LEVEL2 SORT
            Collections.sort<Category>(ctgList2) { c1, c2 ->
                val p1 = c1.getPriorCtgId()
                val p2 = c2.getPriorCtgId()
                p1.compareTo(p2)
            }

            var totalCount = 0
            var selectedPriorCtgId: String? = ""        // 선택된 상위 카테고리를 찾는다.
            for (c1 in ctgList1) {
                if (selectedCtgId != null && selectedCtgId != "" && c1.getCtgId() == selectedCtgId) {
                    c1.setSelectedArea("selected")
                }
                totalCount += c1.getCtgItemCount()
                ctgList.add(c1)
                for (c2 in ctgList2) {
                    if (c1.getCtgId() == c2.getPriorCtgId()) {
                        if (selectedCtgId != null && selectedCtgId != "" && c2.getCtgId() == selectedCtgId) {
                            selectedPriorCtgId = c2.getPriorCtgId()
                        }
                        ctgList.add(c2)
                    }
                }
            }
            // 선택된 카테고리 찾기
            if (selectedPriorCtgId != null && selectedPriorCtgId != "") {
                for (i in ctgList.indices) {
                    val c = ctgList[i]
                    if (c.getCtgId() == selectedPriorCtgId) {
                        c.setSelectedArea("selected")
                        ctgList[i] = c
                    }
                }
            }

        }
        return ctgList
    }

    fun getItemResult(name: String, parameter: Parameter, result: Result, search: Search): Result {
        val count = search.w3GetResultCount(name)
        val itemList = Lists.newArrayList<Item>()
        val srchItemIds = StringBuilder()
        var item: Item
        for (i in 0 until count) {
            item = getItemSetting(search, name, i, parameter)
            val ids = StringBuilder().append(item.siteNo).append(":").append(item.itemId).append(":").append(item.salestrNo).append(",")
            if (srchItemIds.indexOf(ids.toString()) < 0) {
                srchItemIds.append(ids)
            }
            itemList.add(item)
        }
        // 실패검색어 추천상품을 위해
        if (name == "mall") {
            result.noResultItemRecomList = itemList
            result.noResultItemRecomIds = srchItemIds.toString()
            result.noResultItemCount = search.w3GetResultTotalCount(name)
        } else if (name.indexOf("book") > -1) {
            result.bookItemIds = srchItemIds.toString()
            result.bookCount = search.w3GetResultTotalCount(name)
            result.bookItemList = itemList
        } else {
            result.itemList = itemList
            result.itemCount = search.w3GetResultTotalCount(name)
            result.srchItemIds = srchItemIds.toString()
        }// ITEM LIST
        // BOOK LIST
        return result
    }


    fun getAdItemResult(name: String, parameter: Parameter, result: Result, search: Search): Result {
        val count = search.w3GetResultCount(name)
        val itemList = Lists.newArrayList<Item>()
        val srchItemIds = StringBuilder()
        var item: Item

        val page = parameter.pageToInt
        val adItemCount = result.advertisingCount
        val adSearchItemCount = parameter.adSearchItemCount
        val searchCount = Integer.parseInt(StringUtils.defaultIfEmpty(parameter.count, "40"))
        val aplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.aplTgtMediaCd, "10")
        //int adItemDispIndex =  StringUtils.equals(aplTgtMediaCd, "10") ? 4 : 3;
        val adItemDispIndex = 1

        var startInx = 0

        if (page > 1) {
            startInx = adSearchItemCount - adItemCount
        }

        for (i in startInx until count) {
            if (page == 1 && adItemCount > 0 && i == adItemDispIndex) {
                for (j in 0 until adItemCount) {
                    item = getAdvertisingItemSetting(result.advertisingList!!, j)

                    val ids = StringBuilder().append(item.siteNo).append(":").append(item.itemId).append(":").append(item.salestrNo).append(",")
                    if (srchItemIds.indexOf(ids.toString()) < 0) {
                        srchItemIds.append(ids)
                    }

                    itemList.add(item)
                }
            }

            item = getItemSetting(search, name, i, parameter)

            val ids = StringBuilder().append(item.siteNo).append(":").append(item.itemId).append(":").append(item.salestrNo).append(",")
            if (srchItemIds.indexOf(ids.toString()) < 0) {
                srchItemIds.append(ids)
            }
            itemList.add(item)

            if (itemList.size == searchCount) {
                break
            }
        }


        if (page == 1 && adItemCount > 0 && count <= adItemDispIndex) {
            for (j in 0 until adItemCount) {
                item = getAdvertisingItemSetting(result.advertisingList!!, j)

                val ids = StringBuilder().append(item.siteNo).append(":").append(item.itemId).append(":").append(item.salestrNo).append(",")
                if (srchItemIds.indexOf(ids.toString()) < 0) {
                    srchItemIds.append(ids)
                }

                itemList.add(item)
            }
        }

        // 실패검색어 추천상품을 위해
        if (name == "mall") {
            result.noResultItemRecomList = itemList
            result.noResultItemRecomIds = srchItemIds.toString()
            result.noResultItemCount = search.w3GetResultTotalCount(name)
        } else if (name.indexOf("book") > -1) {
            result.bookItemIds = srchItemIds.toString()
            result.bookCount = search.w3GetResultTotalCount(name)
            result.bookItemList = itemList
        } else {
            result.itemList = itemList
            result.itemCount = search.w3GetResultTotalCount(name) + adItemCount
            result.srchItemIds = srchItemIds.toString()
        }// ITEM LIST
        // BOOK LIST
        return result
    }

    fun getMobileBrandCategoryGroup(siteNo: String, selectedCtgId: String?, ctgLst: String): List<Map<String, List<Category>>> {
        val ctgList = ArrayList<Map<String, List<Category>>>()
        val ctgList1 = ArrayList<Category>()
        val ctgList2 = ArrayList<Category>()
        val iter = Splitter.on("@").trimResults().split(ctgLst).iterator()
        while (iter.hasNext()) {
            val ctgElement = iter.next()
            if (ctgElement != null && ctgElement != "") {
                var i = 0
                val c = Category()
                var ctgType = ""
                var ctgCls = ""
                var ctgLv = ""
                val ctgToken = Splitter.on("^").trimResults().split(ctgElement).iterator()
                while (ctgToken.hasNext()) {
                    val ctg = ctgToken.next()
                    if (ctg != null && ctg != "") {
                        // CTG_ID
                        if (i == 0) {
                            c.setCtgId(ctg)
                        } else if (i == 1) {
                            val ctgNms = ctg.split("\\__".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (ctgNms.size >= 6) {
                                ctgLv = ctgNms[1]
                                c.setCtgNm(ctgNms[0].replace("\\++".toRegex(), " "))
                                c.setCtgLevel(Integer.parseInt(ctgLv))
                                c.setSiteNo(ctgNms[2])
                                c.setPriorCtgId(ctgNms[5])
                                ctgCls = ctgNms[3]
                                ctgType = ctgNms[4]
                            }
                        } else if (i == 2) {
                            c.setCtgItemCount(Integer.parseInt(ctg))
                        }// ITEM_COUNT
                        // CTGNM__CTGLV__SITE__CLS__TYPE__PRIOR_DISP_CTG_ID
                    }
                    if (i > 0 && i % 2 == 0) {
                        i = 0
                    } else
                        i++
                }
                // 현재의 사이트와 동일할 경우에만 add
                if (siteNo == c.getSiteNo()) {
                    if (ctgCls == "10" && ctgType == "10" && ctgLv == "1") {
                        ctgList1.add(c)
                    } else if (ctgCls == "10" && ctgType == "10" && ctgLv == "2") {
                        ctgList2.add(c)
                    }
                }
            }
        }
        if (ctgList1 != null && ctgList1.size > 0 && ctgList2 != null && ctgList2.size > 0) {
            // CATEGORY LEVEL1 SORT
            Collections.sort<Category>(ctgList1) { c1, c2 ->
                val d1 = c1.getCtgId()
                val d2 = c2.getCtgId()
                d1.compareTo(d2)
            }

            // CATEGORY LEVEL2 SORT
            Collections.sort<Category>(ctgList2) { c1, c2 ->
                val p1 = c1.getPriorCtgId()
                val p2 = c2.getPriorCtgId()
                p1.compareTo(p2)
            }

            var selectedPriorCtgId: String? = ""        // 선택된 1 LV 카테고리를 찾는다.
            val ctgMap1 = Maps.newHashMap<String, List<Category>>()
            val t1 = ArrayList<Category>()
            val t2 = ArrayList<Category>()

            for (c1 in ctgList1) {
                if (selectedCtgId != null && selectedCtgId != "" && c1.getCtgId() == selectedCtgId) {
                    c1.setSelectedArea("selected")
                }
                t1.add(c1)
                for (c2 in ctgList2) {
                    if (c1.getCtgId() == c2.getPriorCtgId()) {
                        if (selectedCtgId != null && selectedCtgId != "" && c2.getCtgId() == selectedCtgId) {
                            selectedPriorCtgId = c2.getPriorCtgId()
                        }
                        t2.add(c2)
                    }
                }
            }
            ctgMap1["ctg1"] = t1
            ctgMap1["ctg2"] = t2

            val ctgM = HashMap<String, List<Category>>()
            // 선택된 카테고리 찾기
            if (selectedPriorCtgId != null && selectedPriorCtgId != "") {
                val ctg1 = ctgMap1["ctg1"]
                for (i in ctg1!!.indices) {
                    val c1 = ctg1!!.get(i)
                    if (c1.getCtgId() == selectedPriorCtgId) {
                        c1.setSelectedArea("selected")
                        t1[i] = c1
                    }
                }
                val ctg2 = ctgMap1["ctg2"]
                for (i in ctg2!!.indices) {
                    val c2 = ctg2!!.get(i)
                    if (c2.getCtgId() == selectedPriorCtgId) {
                        c2.setSelectedArea("selected")
                        t2[i] = c2
                    }
                }
            }
            //카테고리 전체 리스트로 묶기
            for (i in ctgList1.indices) {
                val c1 = ctgList1[i]
                val c1List = ArrayList<Category>()
                c1List.add(c1)
                for (c2 in ctgList2) {
                    if (c1.getCtgId() == c2.getPriorCtgId()) {
                        c1List.add(c2)
                    }
                }
                ctgM["ctg$i"] = c1List
                ctgList.add(ctgM)
            }
        }
        return ctgList
    }

    fun getBshopGroup(bshopInfo: String): List<Bshop> {
        val bshopList = ArrayList<Bshop>()
        val iter = Splitter.on("@").trimResults().split(bshopInfo).iterator()
        while (iter.hasNext()) {
            val bshopElement = iter.next()
            if (bshopElement != null && bshopElement != "" && bshopElement.indexOf("__") > -1) {
                var i = 0
                val c = Bshop()
                val bshopToken = Splitter.on("^").trimResults().split(bshopElement).iterator()
                while (bshopToken.hasNext()) {
                    val bshop = bshopToken.next()
                    if (bshop != null && bshop != "") {
                        // BSHOPID
                        if (i == 0) {
                            c.setBshopId(bshop)
                        } else if (i == 1) {
                            val bshopNms = bshop.split("\\__".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (bshopNms.size >= 5) {
                                c.setBshopTitleNm(bshopNms[0].replace("\\++".toRegex(), " "))
                                c.setBshopEngTitleNm1(bshopNms[1].replace("\\++".toRegex(), " "))
                                c.setBshopEngTitleNm2(bshopNms[2].replace("\\++".toRegex(), " "))
                                c.setRepBrandId(bshopNms[4])

                                var imgFileNm = StringUtils.defaultIfEmpty(bshopNms[3], "")
                                if (imgFileNm != "") imgFileNm = "http://static.ssgcdn.com$imgFileNm"
                                c.setImgFileNm(imgFileNm)
                            }
                        } else if (i == 2) {
                            c.setBshopItemCount(Integer.parseInt(bshop))
                        }// ITEM_COUNT
                        // BSHOPTITLE__BSHOPENGTITLE1__BSHOPENGTITLE2__IMGURL__REPBRANDID
                    }
                    if (i > 0 && i % 2 == 0) {
                        i = 0
                    } else
                        i++
                }
                bshopList.add(c)
            }
        }
        return bshopList
    }

    fun getMobileCategoryGroup(search: Search, name: String, idx: String, maxLevel: Int): List<MobileCategory> {
        var cList: List<MobileCategory> = ArrayList<MobileCategory>()
        val categoryMap = Maps.newHashMap<String, List<MobileCategory?>>()
        for (level in 1..maxLevel) {
            val categoryList = Lists.newArrayList<MobileCategory>()

            //1레벨부터 전체 카테고리 돌린다.(부모/자식 상관없이 일단 모든 카테고리 다 넣는다)
            for (i in 0 until search.w3GetCategoryCount(name, idx, level)) {

                //1레벨은 1레벨만   ( 6001^0006510000^생활용품/세제/제지 )
                //2레벨부터는 자기자신 포함 전 레벨을 다 가져옴 ( 6001^0006510000^생활용품/세제/제지:6001^0006510003^세탁/주방/생활 세제 )
                val s = search.w3GetCategoryName(name, idx, level, i)
                val t = s.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val categoryToken = t[level - 1].split("\\^".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                val category = MobileCategory()
                category.setSiteNo(categoryToken[0])
                category.setDispCtgId(categoryToken[1])
                category.setDispCtgNm(categoryToken[2])
                category.setItemCount(search.w3GetDocumentCountInCategory(name, idx, level, i).toString())
                category.setDispCtgLvl(level.toString())
                category.setHasChild("false")
                if (level > 1)
                    category.setPriorDispCtgId(t[level - 2].split("\\^".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
                else
                    category.setPriorDispCtgId("")
                categoryList.add(category)
            }

            // CATEGORY SORT(상품 많은순)
            if (categoryList != null && categoryList.size > 0) {
                Collections.sort<MobileCategory>(categoryList) { c1, c2 ->
                    if (Integer.parseInt(c1.getItemCount()) > Integer.parseInt(c2.getItemCount())) {
                        -1
                    } else if (Integer.parseInt(c1.getItemCount()) < Integer.parseInt(c2.getItemCount())) {
                        1
                    } else {
                        0
                    }
                }
            }
            categoryMap[level.toString()] = categoryList
        }

        // Set 한 데이터의 참조를 가지고 부모/자식관계를 생성한다.
        for (level in maxLevel downTo 2) {
            for (prior in categoryMap[(level-1).toString()]!!.iterator()){
                for (target in categoryMap[level.toString()]!!.iterator()) {
                    if (target != null) {
                        if (prior != null) {
                            if (target.getPriorDispCtgId() == prior.getDispCtgId()) {
                                prior.setHasChild("true")
                                prior.add(target)
                            }
                        }
                    }
                }
            }
        }
        cList = categoryMap["1"] as List<MobileCategory>
        return cList
    }

    fun getAdvertisingItemSetting(advertisingList: List<Advertising>, i: Int): Item {
        val item = Item()

        val advertising = advertisingList[i]

        val strAdvertAcctId = advertising.getAdvertAcctId()
        val strAdvertBidId = advertising.getAdvertBidId()
        val strSiteNo = advertising.getSiteNo()
        val strItemId = advertising.getItemId()
        val strItemNm = advertising.getItemNm()
        val strSellprc = advertising.getSellprc()
        val strItemRegDivCd = advertising.getItemRegDivCd()
        val strShppTypeDtlCd = advertising.getShppTypeDtlCd()
        val strExusItemDivCd = advertising.getExusItemDivCd()
        val strExusItemDtlCd = advertising.getExusItemDtlCd()
        val strShppMainCd = advertising.getShppMainCd()
        val strShppMthdCd = advertising.getShppMthdCd()
        val advertBilngTypeCd = advertising.getAdvertBilngTypeCd()        // 10 CPT / 20 CPC
        val advertKindCd = advertising.getAdvertKindCd()            // 10 딜광고 / 20 검색광고 / 30 외부광고
        val advertExtensTeryDivCd = advertising.getAdvertExtensTeryDivCd()

        val strSalestrNo = "6005"

        item.salestrNo = strSalestrNo
        item.siteNo = strSiteNo
        item.itemId = strItemId
        item.itemNm = strItemNm
        item.advertAcctId = strAdvertAcctId
        item.advertBidId = strAdvertBidId
        item.sellprc = strSellprc
        item.itemRegDivCd = strItemRegDivCd
        item.shppTypeDtlCd = strShppTypeDtlCd
        item.exusItemDivCd = strExusItemDivCd
        item.exusItemDtlCd = strExusItemDtlCd
        item.shppMainCd = strShppMainCd
        item.shppMthdCd = strShppMthdCd
        item.advertBilngTypeCd = advertBilngTypeCd
        item.advertKindCd = advertKindCd
        item.advertExtensTeryDivCd = advertExtensTeryDivCd

        return item
    }

    fun getItemSetting(search: Search, name: String, i: Int, parameter: Parameter): Item {
        val item = Item()

        val strItemId = search.w3GetField(name, "ITEM_ID", i)
        val strSiteNo = search.w3GetField(name, "SITE_NO", i)
        val strItemNm = search.w3GetField(name, "ITEM_NM", i)
        val sellprc = search.w3GetField(name, "SELLPRC", i)
        val shppTypeCd = search.w3GetField(name, "SHPP_TYPE_CD", i)
        val shppTypeDtlCd = search.w3GetField(name, "SHPP_TYPE_DTL_CD", i)
        val itemRegDivCd = search.w3GetField(name, "ITEM_REG_DIV_CD", i)
        var strSalestrNo = ""

        var salestrLst = search.w3GetField(name, "SALESTR_LST", i)
        val exusItemDivCd = search.w3GetField(name, "EXUS_ITEM_DIV_CD", i)
        val exusItemDtlCd = search.w3GetField(name, "EXUS_ITEM_DTL_CD", i)
        val shppMainCd = search.w3GetField(name, "SHPP_MAIN_CD", i)
        val shppMthdCd = search.w3GetField(name, "SHPP_MTHD_CD", i)
        // 상품의 이빨이 빠졌을 때 점포값이 필수인 것들이 있기 때문에 강제로  점포 매핑작업을 한다.
//        val userInfo = parameter.userInfo!!
//        if (strSiteNo == "6009" && itemRegDivCd == "30") {
//            if (salestrLst.indexOf("0001") > -1) {
//                var idx = 0
//                salestrLst = salestrLst.replace("0001,", "").trim { it <= ' ' }
//                salestrLst = salestrLst.replace("0001", "").trim { it <= ' ' }
//                val st = StringTokenizer(salestrLst, " ")
//                while (st.hasMoreTokens()) {
//                    if (idx > 1) break
//                    strSalestrNo = st.nextToken().replace("D", "")
//                    idx++
//                }
//                item.sellSalestrCnt = 1
//            } else {
//                strSalestrNo = salestrLst.replace("\\p{Space}".toRegex(), "").replace("D", "")
//                strSalestrNo = salestrLst.replace("\\p{Space}".toRegex(), "").replace("Y", "")    //백화점쓱배송점포처리
//                item.sellSalestrCnt = 1
//            }
//        } else if (strSiteNo == "6001" && itemRegDivCd == "20") {
//            strSalestrNo = userInfo.getEmSaleStrNo()
//            item.sellSalestrCnt = 1
//        } else if (strSiteNo == "6002" && itemRegDivCd == "20") {
//            strSalestrNo = userInfo.getTrSaleStrNo()
//            item.sellSalestrCnt = 1
//        } else if (strSiteNo == "6003" && itemRegDivCd == "20") {
//            strSalestrNo = userInfo.getBnSaleStrNo()
//            item.sellSalestrCnt = 1
//        } else {
//            strSalestrNo = "6005"
//            item.sellSalestrCnt = 1
//        }// 그외 이/트/분 상품의 경우에는 거점점포의 데이터를 넣어준다.
        item.salestrNo = strSalestrNo
        item.siteNo = strSiteNo
        item.itemId = strItemId
        item.itemNm = strItemNm

        item.sellprc = sellprc
        item.shppTypeCd = shppTypeCd
        item.shppTypeDtlCd = shppTypeDtlCd
        item.itemRegDivCd = itemRegDivCd
        item.exusItemDivCd = exusItemDivCd
        item.exusItemDtlCd = exusItemDtlCd
        item.shppMainCd = shppMainCd
        item.shppMthdCd = shppMthdCd

        if (name.indexOf("book") > -1) {
            item.authorNm = search.w3GetField(name, "AUTHOR_NM", i)
            item.trltpeNm = search.w3GetField(name, "TRLTPE_NM", i)
            item.pubscoNm = search.w3GetField(name, "PUBSCO_NM", i)
        }

        return item
    }

    fun getSellprcGroupping(sellprcLst: String): List<Prc> {
        try {
            val prcGroupList = Lists.newArrayList<Prc>()
            val sellprcAList = Lists.newArrayList<Sellprc>()
            val sellprcBList = Lists.newArrayList<Sellprc>()
            var sellprcResultList: List<Sellprc> = Lists.newArrayList<Sellprc>()

            var sellprc: Sellprc? = null
            var sellprcBCount = 0

            val iter = Splitter.on("@").trimResults().split(sellprcLst).iterator()
            while (iter.hasNext()) {
                sellprc = Sellprc()
                val sellprcs = iter.next().split("\\^".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (sellprcs.size > 2) {
                    sellprc!!.setPrcCd(sellprcs[0])
                    sellprc!!.setSizeCount(Integer.parseInt(sellprcs[2]))

                    if (sellprcs[0] != null && sellprcs[0].endsWith("A")) {
                        sellprc!!.setPrc(Integer.parseInt(sellprcs[0].replace("A", "")) * 1000)
                        sellprcAList.add(sellprc)
                    } else if (sellprcs[0] != null && sellprcs[0].endsWith("B")) {
                        sellprc!!.setPrc(Integer.parseInt(sellprcs[0].replace("B", "")) * 10000)
                        sellprcBList.add(sellprc)
                        sellprcBCount += sellprc!!.getSizeCount()
                    }
                }
            }

            // 만원대가 하나이고 그 구간이 만원인경우 천원대 사용
            if (sellprcBList.size == 1 && sellprcBList[0].getPrc() == 10000) {
                Collections.sort<Sellprc>(sellprcAList, Comparator<Sellprc> { c1, c2 ->
                    try {
                        val k1 = c1.getPrc()
                        val k2 = c2.getPrc()
                        if (k1 < k2) {
                            return@Comparator -1
                        } else return@Comparator if (k1 > k2) {
                            1
                        } else {
                            0
                        }
                    } catch (ne: NumberFormatException) {
                        return@Comparator 0
                    }
                })

                sellprcResultList = sellprcAList

            } else {
                Collections.sort<Sellprc>(sellprcBList, Comparator<Sellprc> { c1, c2 ->
                    try {
                        val k1 = c1.getPrc()
                        val k2 = c2.getPrc()
                        if (k1 < k2) {
                            return@Comparator -1
                        } else return@Comparator if (k1 > k2) {
                            1
                        } else {
                            0
                        }
                    } catch (ne: NumberFormatException) {
                        return@Comparator 0
                    }
                })

                sellprcResultList = sellprcBList
            }


            var avgCount = 0
            var groupSumCout = 0
            var minprc = 0
            var maxprc = 0

            if (sellprcResultList.size > 5) {
                avgCount = sellprcBCount / 5
            }

            for (i in sellprcResultList.indices) {
                val sellprcResult = sellprcResultList[i]

                if (groupSumCout == 0) {
                    minprc = maxprc
                }

                groupSumCout += sellprcResult.getSizeCount()

                if (groupSumCout >= avgCount) {
                    maxprc = sellprcResult.getPrc()

                    val prc = Prc()
                    prc.setMinPrc(minprc)
                    prc.setMaxPrc(maxprc)
                    prcGroupList.add(prc)

                    groupSumCout = 0
                } else if (sellprcResultList.size == i + 1) {    //마지막일경우
                    maxprc = sellprcResult.getPrc()

                    val prc = Prc()
                    prc.setMinPrc(minprc)
                    prc.setMaxPrc(maxprc)
                    prcGroupList.add(prc)
                }
            }

            return prcGroupList
        } catch (e: Exception) {
            return Lists.newArrayList<Prc>()
        }

    }

    fun getAdvertisingBanrSetting(advertisingList: List<BanrAdvertising>?): List<Banr> {
        val adBanrList = Lists.newArrayList<Banr>()

        if (advertisingList != null && advertisingList.size > 0) {
            for (banrAdvertising in advertisingList) {
                val banr = Banr()

                banr.setAdvertAcctId(banrAdvertising.getAdvertAcctId())
                banr.setAdvertBidId(banrAdvertising.getAdvertBidId())
                banr.setAdvertBilngTypeCd(banrAdvertising.getAdvertBilngTypeCd())
                banr.setAdvertKindCd(banrAdvertising.getAdvertKindCd())
                banr.setLinkUrl(banrAdvertising.getLinkUrl())
                banr.setImgFileNm(banrAdvertising.getImgFileNm())
                banr.setBanrRplcTextNm(banrAdvertising.getBanrRplcTextNm())
                banr.setPopYn(banrAdvertising.getPopYn())
                banr.setAdvertExtensTeryDivCd(banrAdvertising.getAdvertExtensTeryDivCd())

                adBanrList.add(banr)
            }
        }

        return adBanrList
    }
}