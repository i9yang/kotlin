package ssg.front.search.api.dto

class FrontUserInfo {
    var mbrId: String? = null
        set(mbrId) {
            if (!mbrId.isNullOrEmpty()) {
                field = mbrId
                userId = mbrId
            }
        }               // 회원 id
    var mbrType: String = "B2C"             // 회원 타입 ( B2C || B2E )
    var mbrCoId: String = "000000"             // B2C인 경우 제휴 업체 아이디, B2E인 경우 회원사 아이디
    var emplYn: String? = null                  // 임직원 여부
    var userId: String? = null
    var deviceCd: String? = null                // 회원 접속 장비 정보  Z021
    var deviceDivCd: String? = null             // 10-pc,20-mobile,30-app
    var chnlId: String = ""

    var emSaleStrNo: String = "2034"        // 이마트거점점포
    var trSaleStrNo: String = "2154"        // 트레이더스거점포
    var bnSaleStrNo: String = "2160"        // 분스거점점포
    var emRsvtShppPsblYn: String? = null     // 예약 배송 여부  - 이마트
    var trRsvtShppPsblYn: String? = null     // 예약 배송 여부  - 트레이더스
    var emDualSaleStrNo: String? = null
        get() = if ("0000" == field) {
            if ("2439" == emSaleStrNo) {
                "2034"
            } else if ("2449" == emSaleStrNo) {
                "2037"
            } else {
                field
            }
        } else {
            field
        } // 이중화점포번호
    var hwSaleStrNo: String = "2468"
    var hwRsvtShppPsblYn: String = "N"

    var ga: String? = null
    var itemSiteNo: String? = null
    var mobileAppNo: String? = null

    var bnRsvtShppPsblYn: String? = null
}

