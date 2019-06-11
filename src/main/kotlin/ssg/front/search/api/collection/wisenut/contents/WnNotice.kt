package ssg.front.search.api.collection.wisenut.contents

import QueryAPI510.Search
import ssg.front.search.api.base.Sort
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Sorts
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.dto.vo.SortVo
import ssg.front.search.api.function.Highlightable
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.function.Sortable
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.Notice

class WnNotice : WnCollection(), Pageable, Prefixable, Highlightable, Sortable {

    override fun getName(parameter: Parameter): String {
        return "notice"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "notice"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("POSTNG_ID", "POSTNG_TITLE_NM", "WRT_DATE", "SITE_NO", "SITE_NM", "NEWIMG", "PAGENO")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("POSTNG_TITLE_NM", "SITE_NM", "POSTNG_CNTT")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        val prefix = StringBuilder()
        //모바일,pc
        val aplTgtMediaCd = parameter.aplTgtMediaCd
        if (aplTgtMediaCd != null) {
            prefix.append("<DVIC_DIV_CD:contains:").append(aplTgtMediaCd).append(">")
        }
        return PrefixVo(prefix.toString(), 1)
    }

    override fun pageVo(parameter: Parameter): PageVo {
        val strPage = parameter.page ?: "1"
        var strCount = parameter.count ?: ""
        // Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
        if (strCount == "" || strCount == "0") {
            strCount = "5"
        }
        return CollectionUtils.getPageInfo(strPage, strCount)
    }

    override fun sortVo(parameter: Parameter): SortVo {
        val sortList = arrayListOf<Sort?>()
        sortList.add(Sorts.WRT_DATE.getSort(parameter))
        var notNullList = sortList.filter { it != null } as ArrayList<Sort>
        return SortVo(notNullList)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val NoticeList = arrayListOf<Notice>()
        var notice: Notice
        val count = search.w3GetResultCount(name)
        for (i in 0 until count) {
            notice = Notice()
            notice.setPostngId(search.w3GetField(name, "POSTNG_ID", i))
            notice.setPostngTitleNm(search.w3GetField(name, "POSTNG_TITLE_NM", i).replace("<!HS>".toRegex(), "<strong>").replace("<!HE>".toRegex(), "</strong>"))
            notice.setWrtDate(search.w3GetField(name, "WRT_DATE", i))
            notice.setSiteNo(search.w3GetField(name, "SITE_NO", i))
            notice.setSiteNm(search.w3GetField(name, "SITE_NM", i))
            notice.setNewImg(search.w3GetField(name, "NEWIMG", i))
            notice.setPageNo(search.w3GetField(name, "PAGENO", i))
            NoticeList.add(notice)
        }
        result.noticeCount = search.w3GetResultCount(name)
        result.noticeList = NoticeList
        return result
    }

}
