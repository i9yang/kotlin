package ssg.front.search.api.collection.wisenut.contents

import QueryAPI510.Search
import ssg.front.search.api.base.Sort
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Sorts
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.FilterVo
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.dto.vo.SortVo
import ssg.front.search.api.function.*
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.Event

class WnEvent: WnCollection(), Prefixable, Filterable, Pageable, Sortable, Highlightable {

    override fun getName(parameter: Parameter): String {
        return "event"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "event"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("SITE_NO", "PROM_ID", "PROM_NM", "PROM_ENFC_STRT_DTS", "PROM_ENFC_END_DTS", "PROM_TYPE_CD", "EVNT_TYPE_CD", "OFFER_KIND_CD", "LINK_URL")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("PROM_ID", "PROM_NM", "BANR_CNTT")
    }

    override fun filterVo(parameter: Parameter): FilterVo {
        // NOT IN 을 이용해서 제외조건을 건다.
        val userInfo = parameter.userInfo
        val filter = StringBuilder()
        val q = StringBuilder()
        // B2C, B2E
        val type = userInfo.mbrType
        val coId = userInfo.mbrCoId
        val chnl = userInfo.chnlId
        if (type != null && coId != null) {
            if (type == "B2C") {
                q.append("B2C_").append(coId)
            } else if (type == "B2E") {
                q.append("B2E_").append(coId)
            }
        }
        if (chnl != null && chnl != "") {
            if (q != null && q.toString() != "") q.append(" ")
            q.append("CHNL_").append(chnl)
        }
        filter.append("<EXCP_TYPE_LST:notin:").append(q.toString()).append(">")
        return FilterVo(filter.toString())
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        val userInfo = parameter.userInfo
        val prefix = StringBuilder()
        // SITE_NO
        val siteNo = parameter.siteNo ?: ""
        if (siteNo != "") {
            prefix.append("<SITE_NO:contains:").append(siteNo).append(">")
        }
        // DISP_DVIC_CD
        val deviceCd = parameter.aplTgtMediaCd ?: ""
        if (deviceCd != "") {
            prefix.append("<DISP_DVIC_CD:contains:").append(deviceCd).append(">")
        }
        // B2C, B2E
        val type = userInfo.mbrType
        val coId = userInfo.mbrCoId
        if (type != null && coId != null) {
            if (type == "B2C") {
                prefix.append("<APL_B2C_LST:contains:").append(coId).append(">")
            } else if (type == "B2E") {
                prefix.append("<APL_B2E_LST:contains:B2E|").append(coId).append(">")
            }
        }
        // APL_CHNL_LST
        val chnl = userInfo.chnlId
        if (chnl != null && chnl != "") {
            prefix.append("<APL_CHNL_LST:contains:ALL|").append(chnl).append(">")
        } else {
            prefix.append("<APL_CHNL_LST:contains:ALL>")
        }
        return PrefixVo(prefix.toString(), 1)
    }

    override fun pageVo(parameter: Parameter): PageVo {
        val strTarget = parameter.target.toLowerCase()

        var strPage = parameter.page
        var strCount = parameter.count
        // Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
        if (strTarget.equals("all", ignoreCase = true)) {
            strPage = "1"
            strCount = "5"
        } else if (strCount == "" || strCount == "0") {
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
        val eventList = arrayListOf<Event>()
        val count = search.w3GetResultCount(name)
        var event: Event
        for (i in 0 until count) {
            event = Event()
            event.setPromId(search.w3GetField(name, "PROM_ID", i))
            event.setPromNm(search.w3GetField(name, "PROM_NM", i).replace("<!HS>".toRegex(), "<strong>").replace("<!HE>".toRegex(), "</strong>"))
            event.setPromEnfcStrtDts(search.w3GetField(name, "PROM_ENFC_STRT_DTS", i))
            event.setPromEnfcEndDts(search.w3GetField(name, "PROM_ENFC_END_DTS", i))
            event.setSiteNo(search.w3GetField(name, "SITE_NO", i))
            event.setOfferKindCd(search.w3GetField(name, "OFFER_KIND_CD", i))
            event.setPromTypeCd(search.w3GetField(name, "PROM_TYPE_CD", i))
            event.setEvntTypeCd(search.w3GetField(name, "EVNT_TYPE_CD", i))
            event.setLinkUrl(search.w3GetField(name, "LINK_URL", i))
            eventList.add(event)
        }
        result.eventCount = search.w3GetResultTotalCount(name)
        result.eventList = eventList
        return result
    }
}
