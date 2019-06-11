package ssg.front.search.api.collection.ad

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.google.common.collect.Lists
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import ssg.front.common.logging.MonitorInformation
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.search.result.Advertising

class AdCpcExt : AdCollection() {
    override fun getUrlPath(parameter: Parameter): String {
        return "/addpapi/dp/cpcExtensDispCall.ssg"
    }

    override fun getQuery(parameter: Parameter): List<NameValuePair> {
        val params = Lists.newArrayList<NameValuePair>()

        params.add(BasicNameValuePair("advertKindCd", "20"))
        params.add(BasicNameValuePair("advertQuery", parameter.query))
        params.add(BasicNameValuePair("siteNo", parameter.siteNo))
        params.add(BasicNameValuePair("returnTypeCd", "10"))
        params.add(BasicNameValuePair("stdCtgId", parameter.stdCtgIds ?: ""))
        params.add(BasicNameValuePair("advertExtensTeryDivCd", "20"))    //광고확장영역 20:검색결과없음영역

        return params
    }

    override fun getResult(searchResult: String, name: String, parameter: Parameter, result: Result) {
        val advertisingList = Lists.newArrayList<Advertising>()
        var advertItemCnt = 0

        val mapper = ObjectMapper()
        val json = mapper.readTree(searchResult)
        val resCode = json.get("res_code").textValue()

        if ("200" == resCode && json.get("advertDispTgtCnt").intValue() > 0) {
            advertItemCnt = json.get("advertDispTgtCnt").intValue()
            val arr = json.get("dispTgtAccumDtoList") as ArrayNode

            for (o in arr) {
                val advertising = Advertising()

                advertising.advertAcctId = o.get("advertAcctId").textValue()
                advertising.advertBidId = o.get("advertBidId").textValue()
                advertising.advertBilngTypeCd = o.get("advertBilngTypeCd").textValue()
                advertising.advertKindCd = o.get("advertKindCd").textValue()
                advertising.siteNo = o.get("siteNo").textValue()
                advertising.itemId = o.get("itemId").textValue()
                advertising.itemNm = o.get("itemNm").textValue()
                advertising.sellprc = o.get("sellprc").textValue()
                advertising.itemRegDivCd = o.get("itemRegDivCd").textValue()
                advertising.shppTypeDtlCd = o.get("shppTypeDtlCd").textValue()
                advertising.exusItemDivCd = o.get("exusItemDivCd").textValue()
                advertising.exusItemDtlCd = o.get("exusItemDtlCd").textValue()
                advertising.shppMainCd = o.get("shppMainCd").textValue()
                advertising.shppMthdCd = o.get("shppMthdCd").textValue()
                advertising.dispOrdr = o.get("advertDispTgtCnt").textValue()
                advertising.advertExtensTeryDivCd = o.get("advertExtensTeryDivCd").textValue()

                advertisingList.add(advertising)
            }
        } else if ("200" != resCode) {
            MonitorInformation.LOG.error("DP001_SMS_GROUP001 AdCpcExt result fail message {} : " + json.get("res_message"))
        }

        result.advertisingCount = advertItemCnt
        result.advertisingList = advertisingList
    }
}