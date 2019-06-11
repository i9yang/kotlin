package ssg.front.search.api.collection.wisenut.search

import QueryAPI510.Search
import ssg.front.search.api.base.Sort
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Filters
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.core.constants.Sorts
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.FilterVo
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.dto.vo.SortVo
import ssg.front.search.api.function.Filterable
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.function.Sortable
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.Book
import java.util.*

class WnBook : WnCollection(), Prefixable, Pageable, Filterable, Sortable {

    override fun getName(parameter: Parameter): String {
        return "book"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "book"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("SITE_NO", "ITEM_ID", "ITEM_NM", "DISP_CTG_LCLS_ID", "DISP_CTG_LCLS_NM", "DISP_CTG_MCLS_ID", "DISP_CTG_MCLS_NM", "DISP_CTG_SCLS_ID", "DISP_CTG_SCLS_NM", "DISP_CTG_DCLS_ID", "DISP_CTG_DCLS_NM", "AUTHOR_NM", "TRLTPE_NM", "PUBSCO_NM", "SELLPRC", "SHPP_TYPE_DTL_CD", "FXPRC", "SALESTR_LST", "ITEM_REG_DIV_CD")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID", "ITEM_NM", "ITEM_SRCHWD_NM", "MDL_NM", "ISBN", "BOOK_ENG_NM", "ORTITL_NM", "SUBTITL_NM", "AUTHOR_NM", "TRLTPE_NM", "PUBSCO_NM")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val bookList = ArrayList<Book>()
        var book: Book
        val count = search.w3GetResultCount(name)
        val bookItemIds = StringBuilder()
        for (i in 0 until count) {
            book = Book()
            val strSiteNo = search.w3GetField(name, "SITE_NO", i)
            val strItemId = search.w3GetField(name, "ITEM_ID", i)
            var salestrLst = search.w3GetField(name, "SALESTR_LST", i)
            val itemRegDivCd = search.w3GetField(name, "ITEM_REG_DIV_CD", i)
            var strSalestrNo = ""

            book.setSiteNo(strSiteNo)
            book.setItemId(strItemId)
            book.setItemNm(search.w3GetField(name, "ITEM_NM", i))
            book.setAuthorNm(search.w3GetField(name, "AUTHOR_NM", i))
            book.setTrltpeNm(search.w3GetField(name, "TRLTPE_NM", i))
            book.setPubscoNm(search.w3GetField(name, "PUBSCO_NM", i))
            book.setFxprc(search.w3GetField(name, "FXPRC", i))
            //            book.setDispCtgLclsId(search.w3GetField(name,"DISP_CTG_LCLS_ID",i));
            //            book.setDispCtgLclsNm(search.w3GetField(name,"DISP_CTG_LCLS_NM",i));
            //            book.setDispCtgMclsId(search.w3GetField(name,"DISP_CTG_MCLS_ID",i));
            //            book.setDispCtgMclsNm(search.w3GetField(name,"DISP_CTG_MCLS_NM",i));
            //            book.setDispCtgSclsId(search.w3GetField(name,"DISP_CTG_SCLS_ID",i));
            //            book.setDispCtgSclsNm(search.w3GetField(name,"DISP_CTG_SCLS_NM",i));
            //            book.setDispCtgDclsId(search.w3GetField(name,"DISP_CTG_DCLS_ID",i));
            //            book.setDispCtgDclsNm(search.w3GetField(name,"DISP_CTG_DCLS_NM",i));
            book.setSellprc(search.w3GetField(name, "SELLPRC", i))
            book.setShppTypeDtlCd(search.w3GetField(name, "SHPP_TYPE_DTL_CD", i))

            // 상품의 이빨이 빠졌을 때 점포값이 필수인 것들이 있기 때문에 강제로  점포 매핑작업을 한다.
            val userInfo = parameter.userInfo
            if (strSiteNo == "6009" && itemRegDivCd == "30") {
                if (salestrLst.indexOf("0001") > -1) {
                    var idx = 0
                    salestrLst = salestrLst.replace("0001", "").trim({ it <= ' ' })
                    salestrLst = salestrLst.replace("0001,", "").trim({ it <= ' ' })
                    val st = StringTokenizer(salestrLst, " ")
                    while (st.hasMoreTokens()) {
                        if (idx > 1) break
                        strSalestrNo = st.nextToken().replace("D", "")
                        idx++
                    }
                } else {
                    strSalestrNo = salestrLst.replace("\\p{Space}".toRegex(), "").replace("D", "")
                    strSalestrNo = salestrLst.replace("\\p{Space}".toRegex(), "").replace("Y", "")    //백화점쓱배송점포처리
                }
            } else if (strSiteNo == "6001" && itemRegDivCd == "20") {
                strSalestrNo = userInfo.emSaleStrNo
            } else if (strSiteNo == "6002" && itemRegDivCd == "20") {
                strSalestrNo = userInfo.trSaleStrNo
            } else if (strSiteNo == "6003" && itemRegDivCd == "20") {
                strSalestrNo = userInfo.bnSaleStrNo
            } else {
                strSalestrNo = "6005"
            }// 그외 이/트/분 상품의 경우에는 거점점포의 데이터를 넣어준다.
            book.setSalestrNo(strSalestrNo)
            val ids = StringBuilder().append(strSiteNo).append(":").append(strItemId).append(":").append(strSalestrNo).append(",")
            if (bookItemIds.indexOf(ids.toString()) < 0) {
                bookItemIds.append(ids)
            }
            bookList.add(book)
        }
        result.bookItemIds = bookItemIds.toString()
        result.bookList = bookList
        result.bookCount = search.w3GetResultTotalCount(name)
        return result
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX, Prefixes.FILTER_SITE_NO, Prefixes.SALESTR_LST, Prefixes.MBR_CO_TYPE, Prefixes.SRCH_CTG_ITEM_PREFIX, Prefixes.BRAND_ID, Prefixes.DEVICE_CD, Prefixes.SRCH_PSBL_YN, Prefixes.SCOM_EXPSR_YN
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb, 1)
    }

    override fun pageVo(parameter: Parameter): PageVo {
        var strPage = parameter.page ?: "1"
        var strCount = parameter.count ?: ""
        // Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
        if (strCount == "" || strCount == "0") {
            strCount = "40"
        }
        if (parameter.target.equals("all", ignoreCase = true)) {
            strPage = "1"
            strCount = "5"
        }
        return CollectionUtils.getPageInfo(strPage, strCount)
    }

    override fun filterVo(parameter: Parameter): FilterVo {
        return FilterVo(Filters.PRC_FILTER.getFilter(parameter))
    }

    override fun sortVo(parameter: Parameter): SortVo {
        var sorts : Sorts? = Sorts.BEST
        var strSort = parameter.sort!!
        var sortList = arrayListOf<Sort?>()
        try {
            sorts = Sorts.valueOf(strSort.toUpperCase())
        } catch (e: IllegalArgumentException) {}

        if (sorts!! == Sorts.BEST) {
            sortList.add(Sorts.WEIGHT.getSort(parameter))
            sortList.add(Sorts.RANK.getSort(parameter))
            sortList.add(Sorts.THRD.getSort(parameter))
            sortList.add(Sorts.REGDT.getSort(parameter))
            sortList.add(Sorts.PRCASC.getSort(parameter))
        } else {
            sortList.add(sorts.getSort(parameter))
        }

        var notNullList = sortList.filter { it != null } as ArrayList<Sort>
        return SortVo(notNullList)
    }

}
