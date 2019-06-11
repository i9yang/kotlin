package ssg.front.search.api.dto.result

data class Item(
        var docid: String? = null,
        var siteNo: String? = null,
        var itemId: String? = null,
        var itemNm: String? = null,
        var dispCtgId: String? = null,
        var dispCtgLclsId: String? = null,
        var dispCtgMclsId: String? = null,
        var dispCtgSclsId: String? = null,
        var dispCtgDclsId: String? = null,
        var dispCtgLclsNm: String? = null,
        var dispCtgMclsNm: String? = null,
        var dispCtgSclsNm: String? = null,
        var dispCtgDclsNm: String? = null,
        var temDispCtgLclsId: String? = null,
        var temDispCtgLclsNm: String? = null,
        var temDispCtgMclsId: String? = null,
        var temDispCtgMclsNm: String? = null,
        var temDispCtgSclsId: String? = null,
        var temDispCtgSclsNm: String? = null,
        var temDispCtgDclsId: String? = null,
        var temDispCtgDclsNm: String? = null,
        var obanjangYn: String? = null,
        var newsYn: String? = null,
        var spPriceYn: String? = null,
        var sellprc: String? = null,
        var shppTypeCd: String? = null,
        var shppTypeDtlCd: String? = null,
        var salestrLst: String? = null,
        var salestrNo: String? = null,
        var sellSalestrCnt: Int = 0,
        var itemRegDivCd: String? = null,
        var exusItemDivCd: String? = null,
        var exusItemDtlCd: String? = null,
        var shppMthdCd: String? = null,
        var shppMainCd: String? = null,
        var authorNm: String? = null,
        var trltpeNm: String? = null,
        var pubscoNm: String? = null,
        var fxprc: String? = null,
        var pickuItemYn: String? = null,
        var srchInspStatCd: String? = null,
        var advertAcctId: String? = null, // 광고계좌ID
        var advertBidId: String? = null, // 광고입찰ID
        var advertBilngTypeCd: String? = null, // 10 CPT / 20 CPC
        var advertKindCd: String? = null, // 10 딜광고 / 20 검색광고 / 30 외부광고
    var advertExtensTeryDivCd: String? = null
)
