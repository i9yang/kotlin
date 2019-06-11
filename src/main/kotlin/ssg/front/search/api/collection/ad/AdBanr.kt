package ssg.front.search.api.collection.ad

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.google.common.collect.Lists
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import ssg.front.common.logging.MonitorInformation
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.search.result.BanrAdvertising

class AdBanr: AdCollection() {
    override fun getUrlPath(parameter: Parameter): String {
        return "/addpapi/dp/daDispCall.ssg"
    }

    override fun getQuery(parameter: Parameter): List<NameValuePair> {
        val params = Lists.newArrayList<NameValuePair>()

        params.add(BasicNameValuePair("advertKindCd", "50"))

        /*
	     * 광고쪽요청으로 하드코딩 advertAcctGrpId 여기만 사용하므로 하드코딩, 다른곳 사용시 enum으로 빼야함
	     * 10000053: 이마트몰
         * 10000054: SSG.COM
         * 10000055: 신세계몰
         * 10000056: 신세계백화점
	     */
        var advertAcctGrpId = when (parameter.siteNo) {
            "6001" -> "10000053"
            "6005" -> "10000054"
            "6004" -> "10000055"
            "6009" -> "10000056"
            else -> ""
        }

        params.add(BasicNameValuePair("advertAcctGrpId", advertAcctGrpId))
        params.add(BasicNameValuePair("advertQuery", parameter.query))
        params.add(BasicNameValuePair("siteNo", parameter.siteNo))
        params.add(BasicNameValuePair("deviceCd", parameter.aplTgtMediaCd))

        return params
    }

    override fun getResult(searchResult: String, name: String, parameter: Parameter, result: Result) {
        var advertisingList = Lists.newArrayList<BanrAdvertising>()
        var advertBanrCnt = 0

        val mapper = ObjectMapper()
        val json = mapper.readTree(searchResult)
        val resCode = json.get("res_code").textValue()

        if ("200" == resCode && json.get("advertDispTgtCnt").intValue() > 0) {
            advertBanrCnt = json.get("advertDispTgtCnt").intValue()
            val arr = json.get("dispTgtAccumDtoList") as ArrayNode

            for (o in arr) {
                val banrAdvertising = BanrAdvertising()

                banrAdvertising.advertAcctId = o.get("advertAcctId").textValue()
                banrAdvertising.advertBidId = o.get("advertBidId").textValue()
                banrAdvertising.advertBilngTypeCd = o.get("advertBilngTypeCd").textValue()
                banrAdvertising.advertKindCd = o.get("advertKindCd").textValue()
                banrAdvertising.query = o.get("query").textValue()
                banrAdvertising.siteNo = o.get("siteNo").textValue()
                banrAdvertising.linkUrl = o.get("linkUrl").textValue()
                banrAdvertising.imgFileNm = o.get("imgFileNm").textValue()
                banrAdvertising.banrRplcTextNm = o.get("banrRplcTextNm").textValue()
                banrAdvertising.popYn = o.get("popYn").textValue()
                banrAdvertising.advertExtensTeryDivCd = ""

                advertisingList.add(banrAdvertising)
            }
        } else if ("200" != resCode) {
            MonitorInformation.LOG.error("DP001_SMS_GROUP001 BanrAdvertising result fail message {} : " + json.get("res_message"))
        }

        result.banrAdvertisingCount = advertBanrCnt
        result.banrAdvertisingList = advertisingList
    }
}