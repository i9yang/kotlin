package ssg.front.search.api.dto

data class Parameter(
        var userInfo: FrontUserInfo = FrontUserInfo(),
        var rplcCacheKey: String? = null,

        var shrtcCacheKey: String? = null,
        var srchKwdCacheKey: String? = null,
        var mbrKwdCacheKey: String? = null,
        var obanjangCacheKey: String? = null,
        var newsCacheKey: String? = null,
        var spPriceCacheKey: String? = null,
        var itemCacheKey: String? = null,
        var optnCacheKey: String? = null,
        var brandCacheKey: String? = null,
        var bshopCacheKey: String? = null,
        var srchPopKwdCacheKey: String? = null,

        // 엔진에만 존재하고 상품의 SQL에는 결과가 없는 경우 노출여부 -> 초기값이 true 이고 꼭 노출하고 싶지 않을 경우에만 false
        var outofstockDisplayYn: Boolean? = true,
        var bookFlag: String? = null,
        var bestFlag: String? = null,

        //상시배너엔진분리로인한 상시배너 호출여부컬럼
        var isBanrEverSearch : Boolean? = false,

        // 식품상품군여부(삼성냉장고에서 사용)
        var foodGroupYn: String? = null,

        var isAdSearch: Boolean? = false,  //파라메터가 Y인경우도 해당사이트가 아니면 false
        var itemDeduplicationYn: String? = "N",        //내취에 중복을 상품에서 제거여부

        var adYn: String? = "N",    //파라메터로 광고여부를 전달 받는다
        var tagYn: String? = "N",    //파라메터로 태그노출여부를 전달 받는다

        //레시피용,
        var vodYn: String? = null,

        var srchFailYn: String? = null,
        var recommendYn: String? = null,
        var useYn: String? = null,

        var mobileApiYn: String? = "N",

        var aplTgtMediaCd: String? = null,

        var siteShppcstPlcyDivCd: String? = null,

        var perdcSalestrNo: String? = null,

        var webDvicDivCd: String? = null,

        // FilterSet
        var brand: String? = null,

        var size: String? = null,
        var color: String? = null,
        var benefit: String? = null,
        var cls: String? = null,
        var shpp: String? = null,
        var venId: String? = null,
        var splVenId: String? = null,
        var lrnkSplVenId: String? = null,
        var sizeclip: String? = null,
        var pickuSalestr: String? = null,
        var bshopId: String? = null,

        // Min/ Max
        var minPrc: String? = null,
        var maxPrc: String? = null,

        // EMART MALL FILTER SITE
        var filterSiteNo: String? = null,
        var filter: String? = null,

        // 검색어 가이드 전용 파라메터 var srchItemIds: String? = null,
        var srchRecipeIds: String? = null,
        var srchPnshopIds: String? = null,
        var srchSpPriceItemIds: String? = null,
        var guideItemIds: String? = null,
        var srchRecomItemIds: String? = null,

        val LOAD_LEVEL_GENERAL: String = "10",
        val LOAD_LEVEL_WARNING: String = "20",
        val LOAD_LEVEL_LIMIT: String = "30",
        val LOAD_LEVEL_EXCEPT_AD_GENERAL: String = "40",
        val LOAD_LEVEL_EXCEPT_AD_WARNING: String = "50",
        val LOAD_LEVEL_EXCEPT_AD_LIMIT: String  = "60",
        var loadLevel: String = "10",

        /*
            Category Param
         */
        // Category FilterSet

        var ctgId: String? = null,
        var ctgLv: String? = null,
        var ctgLast: String? = null,
        var parentCtgId: String? = null,
        // Category Theme
        var themeYn: String? = null,
        // Category CheckBox
        var ctgIds: String? = null,
        var themeCtgIds: String? = null,
        // Category Display Info
        var ctgDispInfo: String? = null,

        // 전시 파라메터
        var dispCtgId: String? = null,
        var dispCtgSubIds: String? = null,

        /*
            Brand Param
        */
        var brandId : String? = null,

        /*
            Starfield Param
        */
        var offlineStrId: String? = null,
        var offlineBldgId: String? = null,
        var offlineFloId: String? = null,
        var sfStrLclsCtgId: String? = null,
        var sfStrMclsCtgId: String? = null,

        /*
            howdy Param
        */
        var jpage: String? = null,
        var jcount: String? = null,

        /*
            내취타취
        */
        var tasteItemIds: String? = null,
        var taste: String? = null,

        var url: String = "dev-search.ssglocal.com",
        var port: String = "7000",
        var target: String = "",

        var query: String? = null,

        var count: String = "40",

        var page: String = "1",

        var mbrPrefix: String? = null,

        var display: String? = null,
        var viewType: String? = null,

        var include: String? = null,
        var exclude: String? = null,



        // Typo Error
        var typoErr: String? = null,
        var oriQueryYn: String? = null,
        var oriQuery: String? = null,
        var recomQuery: String? = null,

        // HighLight
        var highlight: String? = null,
        var salestrNo: String? = null,       // 영업점
        var ItemIds: String? = null,

        // 정기배송
        var perdcRsvtShppPsblYn: String? = null,

        // 클럽회원Id
        var mbrClubId: String? = null,

        // 모바일 유입경로
        var inflow: String? = null,

        var siteNo: String = "6005",

        // 치환 키워드
        var replaceQuery: String? = null,
        var frgShppPsblYn: String? = null,



        // 최근 검색어 seq
        var srchSeq: String? = null,

        // 기획전 구성여부 ( BRAND_SHOP 에서 배너의 존재유무를 나타냄 )
        var pnshopYn: String? = null,

        //적용범위 대상(sfc)
        var aplRngTgtCd: String? = null,

        //오반장,해바 구분
        var spType: String? = null,

        //쇼핑매거진 타입 D324 10:매거진, 20:매장습격 30:생활의발견
        var shpgMgzTypeCd: String? = null,

        //큐레이션 관련
        //다면분류
        var clsFilter: String? = null,
        var curaId: String? = null,

        var prefixFilter: String? = null,
        var srchInspStatCd: String? = null,
        var srchStrtRegDts: String? = null,
        var srchEndRegDts: String? = null,

        //searchVersion
        var srchVer: Double? = 1.0,
        var apiVer: String? = null,

        var srchModId: String? = null,

        var itemCacheExpireTime: String? = "300",

        var itemImgSize: String? = null,

        var categoryCollectionName: String? = "item",

        // 사이즈등의 키워드관련 사용여부를 조회할때 쓰는 SRCH_OPTN 테이블의 속성값
        var srchOptnTypeCd: String? = null,



        //모바일 앱 구분
        var mobileAppType: String? = null,

        var adSearchItemCount : Int = 2,

        // 묶음배송
        var grpAddrId: String? = null,
        var shppcstId: String? = null,
        var itemSiteNo: String? = null,

        //히스토리상품관련 표준카테고리
        var stdCtgIds: String? = null,

        var testRequest: String? = null
){
    var pageToInt = Integer.parseInt(this.page)

    var sort: String? = "best"
        set(value) {
            if (!value.isNullOrEmpty()) field = value
        }

}